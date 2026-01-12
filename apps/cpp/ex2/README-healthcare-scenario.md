# Healthcare Mini-Normalizer Challenge (Modern C++ Focused Build)

## Overview
Implement a focused modern C++ (C++20+) console application that streams a single CSV of mixed VISIT and LAB records, validates each line, normalizes into canonical structs, applies minimal terminology mapping, masks patient names, tracks core counts, and prints a concise summary. Single pass, in-memory, no database or external dependencies (besides the header-only doctest framework for tests).

## Core Goals
- Streaming parse (`std::ifstream` + `std::getline`) – no full file load
- Canonical structs: Patient, Visit, LabResult, Rejection, MetricsContainer
- Layered validation with rejection reasons (enum class)
- Terminology mapping (reason + test) with unmapped counter
- Privacy: simple deterministic masking only (first alphanumeric char + ***)
- Core metrics only (lines, patients, visits, labs, rejections, unmapped)
- Robust to malformed lines (reject & continue)

## Input Format
`RecordType,MRN,PatientName,DOB,Date,Code,Value`
- VISIT: Date = VisitDate, Code = ReasonCode, Value ignored
- LAB: Date = CollectedDate, Code = TestCode, Value = numeric lab value

Clarifications:
- Date format: strictly ISO `YYYY-MM-DD`; any other format rejects with `InvalidDate`.
- Header: assume no header. If the first non-empty line starts with `RecordType,MRN,PatientName,` treat it as a header and skip it (does not increment metrics).
- Blank / whitespace-only lines: skip entirely (do not increment any metrics, including `totalLines`).
- CSV parsing: simple split on commas into exactly 7 tokens; no quoted fields are supported.

## Canonical Models
- Patient: mrn, name, maskedName, dob
- Visit: visitId (UUID string), mrn, visitDate, reasonCodeOriginal, reasonCodeMapped
- LabResult: resultId (UUID string), mrn, testCodeOriginal, testCodeMapped, value (double / optional), collectedDate
- Rejection: lineNumber, reasonCode (enum)
- MetricsContainer: totalLines, totalPatients, visitCount, labCount, rejectedCount, unmappedCodesCount

## Validation Rules (Order)
1. Structural column count (must be 7) → Structural
2. Missing MRN (trimmed empty) → MissingMRN
3. MRN regex invalid (`^[A-Za-z0-9_-]{1,32}$`) → InvalidMRNFormat
4. Name length > 128 → NameTooLong
5. Date parse failure (DOB or event) → InvalidDate
6. Future DOB or event date (> today UTC) → FutureDate
7. RecordType not VISIT/LAB → UnknownRecordType
8. LAB numeric parse failure → InvalidLabValue
9. (Optional) Empty Code warning → WarnEmptyCode (not a rejection)

## Terminology Mapping
Abstract interface example:
```cpp
struct ITerminologyMapper {
    virtual ~ITerminologyMapper() = default;
    virtual std::string mapReason(const std::string&, const std::function<void()>&) = 0;
    virtual std::string mapTest(const std::string&, const std::function<void()>&) = 0;
};
```
Defaults:
- Reasons: R001→Annual, R002→FollowUp else "Unknown" (+ unmapped)
- Tests: GLU→LOINC-GLU, HCT→LOINC-HCT else unmapped (mapped empty string)

## Privacy (Masking Only)
Rule: first alphanumeric char + ***; empty/null → *.

## Metrics Summary Line
`Lines=X Patients=Y Visits=V Labs=L Rejected=R Unmapped=U`

## Processing Flow
1. Stream lines from `std::ifstream`.
2. If line is a header (see above) skip; if blank after trim skip. Otherwise increment `totalLines`.
3. Parse → validate (ordered rules).
4. On rejection: record & continue.
5. Upsert patient (by trimmed MRN) with masking once.
6. Map codes (reason/test) with unmapped callback incrementing counter.
7. Create Visit or LabResult; increment counts.
8. Threshold checks (soft warn, hard stop).
9. Print summary + optional patient list + optional rejection grouping.

Additional semantics:
- `totalLines` counts only non-blank, non-header lines encountered (accepted or rejected).
- A rejected line still contributes to `totalLines` and increments `rejectedCount`.
- Upon exceeding the hard line threshold, stop reading further lines immediately and proceed to print the summary.
- Patient DOB conflicts (same MRN, different DOB) are ignored after the first insertion (first wins).
- Each unmapped code occurrence increments `unmappedCodesCount` (not distinct values).

## Configuration (Minimal)
```cpp
struct NormalizerConfig {
    std::string dataFilePath;
    int softLineWarningThreshold = 100'000;
    int hardLineStopThreshold    = 1'000'000;
};
```

## Acceptance Criteria
Must:
- Summary line prints all six metrics
- ≥3 rejection reasons reachable
- Unmapped reason + test increment counter
- Masked name differs from original
- Duplicate MRN counted once
- Single pass processing
- ≥5 passing doctest tests

Should:
- Mapper via interface
- Thresholds honored (soft warning logged, hard stop enforced)

May:
- Warning counter if implementing WarnEmptyCode
- Simple interactive query loop

## Test Suggestions (doctest)
1. Missing MRN → MissingMRN
2. Unknown reason code → Unknown + unmapped increment
3. Unknown test code → unmapped increment
4. Masking edge cases (normal, single char, empty)
5. Future date → FutureDate
6. Invalid lab value → InvalidLabValue (or duplicate MRN consolidation)

## Suggested Copilot Prompt Sequence
1. "Generate C++20 structs for Patient, Visit, LabResult, Rejection, MetricsContainer."
2. "Create ITerminologyMapper abstract base + default implementation using unordered_map."
3. "Implement parseLine returning optional parsed struct or rejection reason enum." 
4. "Add validation helpers: structural column count, MRN regex, date parsing, future date guard, lab numeric parsing." 
5. "Implement masking utility and patient upsert map keyed by MRN (store masked once)." 
6. "Write processLines orchestrator: stream ifstream, validate, map, record metrics, log summary." 
7. "Add doctest cases: missing MRN, unknown reason/test mapping, masking edge cases, future date, invalid lab value." 

## Extension Ideas (After Core)
- JSON metrics export
- Rejections CSV
- Progress output every N lines
- Additional mapping sets via configuration

## How to Build & Run
Assume source layout:
```
src/
  main.cpp
  models.hpp
  normalizer.cpp
  normalizer.hpp
  terminology.hpp
tests/
  tests.cpp (defines DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN)
```

Build (example):
```
g++ -std=c++20 -O2 -Wall -Wextra -Isrc -Ithird_party/doctest -c src/normalizer.cpp
g++ -std=c++20 -O2 -Wall -Wextra -Isrc -c src/main.cpp
g++ -o normalizer main.o normalizer.o
```

Run:
```
./normalizer data.csv
```

Tests (single TU example):
```
g++ -std=c++20 -O2 -Wall -Wextra -Isrc -Ithird_party/doctest tests/tests.cpp src/normalizer.cpp -o tests && ./tests
```

## Reflection Prompts
- Which validation caught the most issues?
- What would you reintroduce first for richer analytics (hashing, timing, memory)?
- Next step to add a second file type?

---
Keep scope strict: normalization, mapping, masking, metrics counts.
