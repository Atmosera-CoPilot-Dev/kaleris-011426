# Bank Transaction Monitoring System – Modern C++ (VS Code + CMake)

This README is intentionally free of any .NET / Visual Studio / NUnit assumptions. The exercise targets modern C++20 (or later), built with g++ (or clang++) using CMake inside VS Code. Unit tests use the DocTest framework.

---
## Goal (High-Level)
Build an in-memory, thread-safe transaction monitoring core featuring:
- Multiple account types with distinct balance rules
- Atomic, invariant-preserving deposits, withdrawals, and transfers
- Immutable append-only transaction ledger
- Pluggable suspicious activity rules + detector + review workflow
- Daily reporting + audit trail
- Fast DocTest coverage (< 5 s)

---
## Architecture Targets
Core library (static): `banking_core` providing domain models, services, repositories, rules, and reporting. Tests depend only on public headers. Optional demo application may link against the same library.

---
## Recommended Directory Layout
```
repo/
   CMakeLists.txt
   src/
      accounts/
      transactions/
      suspicious/
      reporting/
      audit/
      settings/
      repositories/
      common/
   tests/
      doctest/ (or external/)
      accounts_tests.cpp
      transactions_tests.cpp
      suspicious_tests.cpp
      reporting_tests.cpp
      concurrency_tests.cpp
   demo/ (optional)
```

---
## Key Domain Elements
- Money representation: `int64_t` cents (avoid floating point).
- Time: `std::chrono::system_clock::time_point` (UTC assumed).
- IDs: UUID strings or lightweight custom 128-bit type. Account numbers: atomic monotonic 64-bit integer.
- Thread safety: per-account `std::mutex`; deterministic ordering for multi-account operations (sort IDs before locking); ledger protected by its own mutex.

---
## Core Components
| Component | Responsibility |
|-----------|----------------|
| BankAccount (abstract) | Holds balance; enforces constraints via protected delta application |
| CheckingAccount | No overdraft allowed |
| SavingsAccount | Minimum balance rule |
| BusinessAccount | Overdraft up to configured negative limit |
| TransactionProcessor | Orchestrates deposit/withdraw/transfer; maintains ledger; triggers detection |
| ISuspiciousRule | Strategy interface for rule evaluation |
| LargeAmountRule | Fires on amount strictly greater than threshold |
| RapidFireRule | Fires when count within rolling window >= threshold |
| SuspiciousActivityDetector | Applies rules to successful transactions; stores records; review API |
| DailySummaryService | Aggregates counts + per-account-type net deltas for a UTC day |
| IAuditLog / InMemoryAuditLog | Append-only audit trail |
| MonitoringSettings | Central configuration (thresholds & limits) |
| IAccountRepository / InMemoryAccountRepository | Manage account lifecycle |

---
## Configuration (MonitoringSettings)
```
struct MonitoringSettings {
   int64_t largeAmountThreshold;      // cents, trigger if tx.amount > this
   int rapidFireCount;                // threshold count (>= triggers)
   std::chrono::milliseconds rapidFireWindow; // rolling window
   int64_t businessOverdraftLimit;    // absolute permitted negative (cents)
   int64_t savingsMinimumBalance;     // lowest allowed post-op balance (cents)
};
```

---
## Public API Notes
- Operations may be synchronous; concurrency safety is required (no data races under parallel invocation).
- Validation: reject zero/negative external amounts before mutating state.
- Every operation (deposit / withdraw / transfer) produces exactly one immutable Transaction record: `Status=Succeeded` or `Status=Rejected`.
- Suspicious evaluation occurs only after a successful transaction is appended (never for rejected transactions).
- Review of a suspicion record is deliberately idempotent: first call sets review metadata; subsequent calls return success without altering existing review fields.
- Ledger snapshot method returns a deep copy (value semantics); callers cannot mutate internal storage.

### Audit Actions (Required Minimum)
| Action | When Emitted |
|--------|--------------|
| AccountCreated | After repository adds a new account |
| AccountClosed (optional) | If closure feature implemented |
| TransactionProcessed | After each operation (succeeded or rejected) |
| SuspicionRaised | For each rule-triggered suspicion record |
| SuspicionReviewed | When a record is first reviewed |

### Suspicion Severity Policy
- Each rule supplies a recommended `Severity` (e.g., `LargeAmountRule` => configurable or default Medium; `RapidFireRule` => Medium/High based on excess over threshold). If a rule does not specify, default to `Severity::Medium`.

### Rule Evaluation Context
- Rules receive the triggering transaction plus a bounded, recent window of prior transactions (e.g., last N or those within `rapidFireWindow`) filtered to the same source account when appropriate. Implementation detail: retain either a time-ordered ring buffer or use ledger scan optimized by timestamp cutoff.

---
## Invariants
1. Global Balance Conservation: Sum(all balances) after any sequence of successful transfers equals initial sum plus net deposits minus withdrawals.
2. No Partial Transfers: Either both source and target balances change or neither.
3. Ledger Immutability: Once a transaction object is stored, its fields are never mutated.
 4. Single Transaction Per Operation: Exactly one ledger entry per public API invocation.

---
## Ordered Implementation Prompts (Use with Copilot Incrementally)
1. Create `monitoring_settings.hpp` with central config struct.
2. Scaffold `bank_account.hpp` (abstract) + enum `AccountType`; add protected `apply_delta` enforcing per-derived rules.
3. Implement `checking_account`, `savings_account`, `business_account` with rule logic.
4. Implement repository interface + `in_memory_account_repository` (unordered_map + shared_mutex + atomic counter).
5. Add immutable `transaction.hpp` (struct + enums for type & status).
6. Define `itransaction_processor` interface.
7. Implement `transaction_processor` with ordered locking for transfers (sort two IDs) + ledger vector + snapshot.
8. Add suspicious rule abstractions & records; implement `large_amount_rule` and `rapid_fire_rule`.
9. Implement `suspicious_activity_detector` with thread-safe storage and review function.
10. Add audit interfaces + in-memory implementation + instrumentation in processor.
11. Implement daily summary model + service computing counts & net deltas per AccountType.
12. Write DocTest cases for account rule enforcement and error paths.
13. Add rule trigger & non-trigger tests (edge thresholds).
14. Add concurrency stress test (multiple threads transferring; assert invariants).
15. Add reporting aggregation test.
16. Add Doxygen comments to public headers.
17. Refine validations (common guard helpers) and finalize.

---
## Testing (DocTest)
Test categories:
1. Account Rules (Savings min, Business overdraft boundary, Checking no overdraft)
2. Transactions (successful + rejected paths)
3. Suspicious Rules (LargeAmount > threshold only; RapidFire triggers at >= N)
4. Concurrency (parallel transfers maintain invariant & produce no duplicate IDs)
5. Reporting (daily summary counts + per-type deltas)

Target total runtime: < 5 seconds.

For deterministic time-based rule tests, optionally inject a test clock functor returning controlled timestamps instead of using `system_clock::now()` directly.

---
## Sample CMake Root (Illustrative)
```
cmake_minimum_required(VERSION 3.22)
project(banking LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(banking_core
   src/accounts/bank_account.cpp
   # add other implementation files
)
target_include_directories(banking_core PUBLIC src)

add_executable(banking_tests
   tests/accounts_tests.cpp
   tests/transactions_tests.cpp
   tests/suspicious_tests.cpp
   tests/reporting_tests.cpp
   tests/concurrency_tests.cpp
)
target_link_libraries(banking_tests PRIVATE banking_core)
target_compile_definitions(banking_tests PRIVATE DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN)

enable_testing()
add_test(NAME banking_tests COMMAND banking_tests)
```

Add `doctest.h` under `tests/` or vendored location. Optionally integrate with vcpkg or FetchContent.

---
## Concurrency Strategy
- Per-account mutex for balance operations.
- For transfers: collect pointers/IDs, sort, lock in order with `std::scoped_lock` or `std::lock` then individual `std::unique_lock`s.
- Ledger append uses separate mutex to limit critical section scope.

---
## Error Handling Guidelines
- Invalid amount (<= 0): throw `std::invalid_argument`.
- Account not found: throw `std::runtime_error` (or custom exception).
- Business overdraft exceeded / savings min breach / checking overdraft attempt: transaction recorded with Status=Rejected and reason string.

---
## Daily Summary Logic
Input: snapshot of transactions for a UTC date. Compute:
- TotalTransactions
- SucceededCount / RejectedCount
- SuspiciousCount (total suspicion records created that day)
- Net deltas: sum of signed balance changes grouped by AccountType.

---
## Quality Checklist
- [ ] No data races (use Thread Sanitizer optionally)
- [ ] Deterministic lock ordering across multi-account operations
- [ ] Money arithmetic uses integer cents only
- [ ] Invariants asserted in tests (global sum, no partial transfer)
- [ ] Suspicious thresholds strictly respected (>, >=)
- [ ] Doxygen comments on public headers
- [ ] Test runtime < 5 s
- [ ] No PII in audit or suspicion records

---
## Stretch Ideas
- Pluggable clock abstraction for deterministic time in tests
- Additional rule: cumulative amount volume in window
- Simple JSON export of ledger & summary
- Demo TUI/CLI printing daily summary

---
## Design Trade-Off Notes
- Chose integer cents to avoid precision loss; scaling factor (100) documented centrally.
- UUID generation can be stubbed with incremental hex values for deterministic tests.
- Simplicity favored over lock-free structures; contention expected to be low.

---
## Getting Started (VS Code)
1. Install extensions: CMake Tools, C/C++.
2. Configure a kit (g++ or clang++): Command Palette → CMake: Select Configure Preset / Kit.
3. Configure & build (CMake: Configure, then CMake: Build).
4. Run tests: CMake: Run Tests or execute `banking_tests` binary.
5. Iterate using the ordered implementation prompts above.

---
## License / Usage
Use freely for educational / practice purposes. Replace or extend components as needed for further experimentation.

---
