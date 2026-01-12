# Scenario: Healthcare Mini-Normalizer Specification (Modern C++)

## Objective
Design and implement a focused modern C++ (C++20 or later) single-process console application (built with `g++` or `clang++`) that ingests a single CSV (mixed VISIT + LAB rows), produces canonical domain structs, performs layered validation + terminology mapping, applies simple name masking for privacy, tracks core metrics, and prints a concise summary. Provide a lean doctest test suite (5–6 tests). Entirely in‑memory; single streaming pass over one input file—no databases, frameworks, or external deps beyond doctest (header-only) and the C++ standard library.

Target: ~90 minutes for an experienced engineer leveraging Copilot in VS Code effectively.

## Scope Overview 
- Streaming line processing (`std::ifstream` + `std::getline`) — no full file load
- Layered validation (syntax → semantic) with categorized rejection reasons (enum)
- Name privacy via masking only (first alphanumeric char + `***`)
- Small configuration struct (file path + thresholds only)
- Pluggable terminology mapper (abstract base class or callable) + default in-memory implementation
- Minimal logging abstraction (info / warn / error) — simple stdout/stderr lines
- Large file guardrails (soft warning + hard stop line thresholds)
- Validation rules: structural, MRN presence/format, date parsing, future date, lab numeric, name length
- Core metrics only (no timing / memory sampling)
- Tests (doctest) focus on rejection rules, mapping, masking, duplicates, unmapped counting
- Excluded: hashing, advanced runtime metrics, memory sampling, detailed raw line storage

## Core Data Models (DO NOT ADD EXTRAS)
Represent as simple `struct`s in a header (e.g., `models.hpp`). Prefer standard types (avoid custom wrappers). Use `std::string` for text, `std::optional<double>` for lab numeric value if desired (or sentinel), and `std::chrono::sys_days` / `std::chrono::year_month_day` for dates (or fallback to storing as ISO string if time-constrained). IDs can use `std::string` with generated UUID v4 (lightweight function) or simplified unique counter if time tight.

- Patient: `mrn`, `name`, `maskedName`, `dob` (date)
- Visit: `visitId` (UUID string), `mrn`, `visitDate`, `reasonCodeOriginal`, `reasonCodeMapped`
- LabResult: `resultId` (UUID string), `mrn`, `testCodeOriginal`, `testCodeMapped`, `value` (double or `std::optional<double>`), `collectedDate`
- Rejection: `lineNumber`, `reasonCode` (enum)
- Metrics: `totalLines`, `totalPatients`, `visitCount`, `labCount`, `rejectedCount`, `unmappedCodesCount`

Keep models minimal—no extra diagnostic fields or inheritance.

## File Format
CSV columns (7):
`RecordType,MRN,PatientName,DOB,Date,Code,Value`

Types:
- VISIT: `Date` = VisitDate, `Code` = ReasonCode, `Value` ignored
- LAB: `Date` = CollectedDate, `Code` = TestCode, `Value` = numeric lab value

## Validation (Layered)
Order matters (stop at first failure per line to keep logic simple):
1. Structural: correct column count (7) → reject `Structural`
2. Missing MRN (after trim) → `MissingMRN`
3. MRN regex invalid (`^[A-Za-z0-9_-]{1,32}$`) → `InvalidMRNFormat`
4. Name length > 128 → `NameTooLong`
5. Date parse failure (DOB or event date) → `InvalidDate`
6. Future DOB or event date (> current UTC date) → `FutureDate`
7. RecordType not VISIT/LAB → `UnknownRecordType`
8. LAB value present but non-numeric → `InvalidLabValue`
9. Optional warning: empty Code (if implemented) → `WarnEmptyCode` (not a rejection)

Each rejection produces one `Rejection` entry and increments `rejectedCount`.

## Terminology Mapping (Pluggable)
Abstract interface (header-only):
```cpp
struct ITerminologyMapper {
  virtual ~ITerminologyMapper() = default;
  virtual std::string mapReason(const std::string& original, const std::function<void()>& unmappedCb) = 0;
  virtual std::string mapTest(const std::string& original, const std::function<void()>& unmappedCb) = 0;
};

struct DefaultTerminologyMapper : ITerminologyMapper {
  std::unordered_map<std::string, std::string> reasonMap{ {"R001","Annual"}, {"R002","FollowUp"} };
  std::unordered_map<std::string, std::string> testMap{ {"GLU","LOINC-GLU"}, {"HCT","LOINC-HCT"} };
  std::string mapReason(const std::string& original, const std::function<void()>& unmappedCb) override {
    auto it = reasonMap.find(original);
    if (it == reasonMap.end()) { unmappedCb(); return "Unknown"; }
    return it->second;
  }
  std::string mapTest(const std::string& original, const std::function<void()>& unmappedCb) override {
    auto it = testMap.find(original);
    if (it == testMap.end()) { unmappedCb(); return ""; } // empty means unmapped
    return it->second;
  }
};
```
Unmapped callback increments `unmappedCodesCount`.

## Privacy
- Masking only: if name has at least one alphanumeric char, take the first such char + `***`; if empty/null produce single `*`.
- No hashing—keeps implementation trivial.

## Configuration Struct
```cpp
struct NormalizerConfig {
    std::string dataFilePath;
    int softLineWarningThreshold = 100'000;
    int hardLineStopThreshold    = 1'000'000;
};
```

## Processing Flow (Streaming)
1. Open `std::ifstream` and iterate with `std::getline` (optionally skip BOM / header if present).
2. Increment `totalLines` for each non-empty line processed.
3. Split line (manual parse or simple CSV split assuming no quoted commas) into 7 columns.
4. Validate in listed order → on failure append `Rejection` & continue.
5. Upsert Patient keyed by trimmed MRN (`std::unordered_map<std::string, Patient>`). Apply masking once.
6. Map codes (reason/test) with mapper; unmapped callback increments `unmappedCodesCount`.
7. Instantiate Visit or LabResult; increment `visitCount` or `labCount`.
8. Enforce thresholds: if `totalLines == softLineWarningThreshold` log warn; if exceed hard threshold: log error and break.
9. At end: print summary line + patient list (optional) + optionally grouped rejections.

## Metrics Summary Line
Exact format (single line):
`Lines=X Patients=Y Visits=V Labs=L Rejected=R Unmapped=U`

## Logging (Minimal)
Interface (abstract base) example:
```cpp
struct ILogger {
  virtual ~ILogger() = default;
  virtual void info(const std::string& msg) = 0;
  virtual void warn(const std::string& msg) = 0;
  virtual void error(const std::string& msg) = 0; // keep simple
};

struct StdLogger : ILogger {
  void info(const std::string& m) override  { std::cout << "[INFO] "  << m << '\n'; }
  void warn(const std::string& m) override  { std::cout << "[WARN] "  << m << '\n'; }
  void error(const std::string& m) override { std::cerr << "[ERROR] " << m << '\n'; }
};
```
Log startup, soft threshold reached, first few rejections (optional), and summary.

## Error Resilience
- Invalid lines → rejection (no crash)
- Missing file → log error + return non-zero exit code (`main`)

## Tests (Target 5–6) with doctest
Place tests in `tests/` linking a single translation unit that includes doctest main, or define `DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN` in one file.

Suggested cases:
1. Missing MRN → MissingMRN rejection
2. Unknown reason code → mapped `Unknown` + unmapped increment
3. Unknown test code → unmapped increment (empty mapped string)
4. Masking edge cases (normal name, single char, empty)
5. Future date → FutureDate rejection
6. Invalid lab numeric value → InvalidLabValue (or duplicate MRN counting once)
7. Duplicate MRN not double-counted (optional swap-in)

## Time Allocation (Suggested)
- 10 min: Models + config + mapper/logger interfaces
- 15 min: Parser + validation helpers
- 15 min: Mapping + patient registry + masking
- 15 min: Orchestrator + metrics + summary + logging
- 20 min: doctest tests
- 5 min: Cleanup / README notes

## Acceptance Criteria
MUST:
- Print summary line with all six core metrics
- At least 3 distinct rejection reasons possible
- Unmapped reason & test both increment `unmappedCodesCount`
- `maskedName` never equals original name (except trivial single-char where still differs by asterisks)
- Duplicate MRNs counted once
- Single pass streaming over file
- ≥5 doctest tests all pass

SHOULD:
- Pluggable mapper abstraction present
- Soft/hard thresholds honored

MAY:
- Warning counter (if `WarnEmptyCode` implemented)
- Simple interactive loop (e.g., press key to list patients)

## Non-Goals
- Hashing / cryptographic functions
- Runtime duration or memory sampling metrics
- Raw line storage in `Rejection`
- Dependency injection frameworks / async / external packages

## Copilot Prompt Sequence
The ordered prompt sequence has been centralized in the project README under
"Suggested Copilot Prompt Sequence" to keep a single authoritative version.
Refer there while building the solution.

## README Sections
Overview | Input Format | Validation Rules | Terminology Mapping | Privacy | Metrics | How to Build/Run | Tests | Extension Ideas

## Reflection Prompts
- Which validation rule caught the most test cases?
- What would you add first if hashing were reintroduced?
- How would you extend to support a second file type?

---
Focused scope: normalization, validation, mapping, masking, core metrics—nothing more.
