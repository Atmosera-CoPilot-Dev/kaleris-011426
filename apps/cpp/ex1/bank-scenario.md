# Bank Transaction Monitoring System – Specification (Modern C++)
---

## 1. Scope

Deliver an in-memory, thread-safe transaction monitoring core in modern C++ (C++20 or later) that:
- Manages multiple account types with distinct balance rules.
- Processes deposits, withdrawals, and transfers atomically and safely under concurrency.
- Records an immutable transaction ledger (append-only, snapshot exposure only).
- Detects suspicious activity via pluggable rule objects.
- Produces a minimal daily summary and maintains an audit trail.
- Is fully covered by fast DocTest unit tests (< 5 s total wall time on a typical developer laptop).

All development is assumed in VS Code using g++ (or clang++) via CMake.

---

## 2. Domain Model Requirements

### Accounts
- Abstract base class `BankAccount` with fields:
  - `AccountId` (UUID or 128-bit value represented as std::string / custom struct)
  - `AccountNumber` (monotonically increasing 64-bit integer, atomic generator)
  - `AccountType` (enum: Checking, Savings, Business)
  - `Balance` (stored as `std::int64_t` representing minor currency units, e.g. cents)
- Derived types:
  - `CheckingAccount`: no overdraft (reject if withdrawal/transfer exceeds available balance).
  - `SavingsAccount`: enforce configured minimum balance (post-operation balance >= minimum threshold).
  - `BusinessAccount`: allow overdraft up to configured negative limit (invariant: `Balance >= -OverdraftLimit`).
- Thread-safe balance mutation: protect with an internal `std::mutex`. No torn reads or lost updates.
- Unique account numbers: strictly increasing using `std::atomic<long long>`.
- Monetary amounts are non-negative inputs; all operations reject zero or negative transaction amounts (except internal deltas applying negative for withdrawals). Represented externally as signed 64-bit cents.

### Transactions
- Transaction struct fields (all immutable after construction):
  - `TransactionId` (UUID/string)
  - `UtcTimestamp` (`std::chrono::system_clock::time_point` assumed UTC)
  - `Type` (enum: Deposit, Withdrawal, Transfer)
  - `Amount` (int64_t cents, absolute amount involved)
  - `SourceAccountId` (may be empty for deposits)
  - `TargetAccountId` (present for transfers; empty otherwise)
  - `Status` (enum: Succeeded, Rejected)
  - `FailureReason` (optional string; empty if succeeded)
- Ledger: append-only container (e.g., `std::vector<Transaction>` guarded by a mutex). Provide a snapshot method that returns a copy; no direct mutating access.
- Transfers debit source and credit target atomically; never leave partial state.

### Suspicious Activity
- Interface: `ISuspiciousRule` (pure virtual) exposing:
  - `ruleId()` (string or UUID)
  - `description()`
  - `evaluate(const Transaction& tx, std::span<const Transaction> recent)` -> optional/boolean trigger.
- Baseline rule implementations:
  1. `LargeAmountRule`: fires when `tx.Amount > threshold` (strictly greater).
  2. `RapidFireRule`: fires when count of transactions from same source within a rolling time window reaches or exceeds a configured count (`>=`).
- Detector (`SuspiciousActivityDetector`):
  - Evaluates each successful transaction after it is committed to the ledger.
  - Emits `SuspicionRecord` containing: `TransactionId`, `RuleId`, `CreatedUtc`, `Severity` (enum: Low, Medium, High), and optional review fields (`ReviewedUtc`, `Analyst`, `ResolutionNote`).
  - Supports querying open (unreviewed) and all records.
  - Supports marking a record reviewed; second/subsequent calls are idempotent (no error, no change after first).

### Reporting & Audit
- Daily summary (UTC day boundary) includes:
  - `TotalTransactions`
  - `SucceededCount`
  - `RejectedCount`
  - `SuspiciousCount` (total suspicion records created that day, irrespective of review status)
  - Net balance deltas per `AccountType` (sum of applied signed deltas for that type)
- Audit entries (append-only):
  - Fields: `UtcTime`, `Actor` ("system" for internal), `Action`, `EntityType`, `EntityId`, `Metadata` (key/value map or JSON string)
  - Actions: `AccountCreated`, `AccountClosed` (if implemented), `TransactionProcessed`, `SuspicionRaised`, `SuspicionReviewed`

---

## 3. Functional Requirements

1. Ability to create accounts (factory or repository method). Return handle/ID; lookup by `AccountId`.
2. `deposit` operation:
   - Reject zero/negative external amount.
   - On success: update balance, record transaction, write audit.
3. `withdraw` operation:
   - Reject zero/negative external amount.
   - Enforce account-specific constraints.
4. `transfer` operation:
   - Reject zero/negative amount.
   - Apply source constraints same as withdrawal.
   - Apply atomically: both balance changes succeed or rollback (single transaction record).
5. Suspicious evaluation occurs only for succeeded transactions, after ledger append.
6. Marking suspicion reviewed sets review metadata exactly once (subsequent calls: no-op documented).
7. Public API may be synchronous; optional async wrappers (e.g., using `std::async`) acceptable but must not introduce data races. Emphasis is on thread-safe reentrancy rather than asynchronous I/O.

---

## 4. Non-Functional Requirements

- Concurrency:
  - No data races under high parallel mixed operations (validated with stress test transferring among many accounts).
  - Deterministic multi-account lock ordering (e.g., sort pointers or IDs then lock via `std::scoped_lock` and `std::lock`).
  - Invariant: Sum of all balances after any sequence of successful operations = initial sum + deposits − withdrawals (transfers conserve global sum).
- Time:
  - All timestamps taken with `std::chrono::system_clock::now()` (assumed UTC).
- Configuration:
  - Central `MonitoringSettings` struct: `largeAmountThreshold`, `rapidFireCount`, `rapidFireWindow`, `businessOverdraftLimit`, `savingsMinimumBalance` (all strongly typed: amounts in cents, window as `std::chrono::milliseconds`).
- Safety / Validation:
  - Validate inputs before side effects; throw `std::invalid_argument` (or dedicated exception types) for invalid amounts or missing accounts.
  - No sensitive personal data stored or logged.
  - Audit & suspicion records hold IDs only.
- Testability:
  - Deterministic logic; optional injection of a clock functor (`std::function<time_point()>`) for tests if needed.
- Extensibility:
  - New suspicious rules added by implementing `ISuspiciousRule` without modifying the processor.
  - Potential future persistence achievable by swapping in repository interfaces.

---

## 5. Acceptance Criteria

- Operations are callable concurrently without undefined behavior.
- Savings withdrawal that would drop below minimum balance is rejected.
- Business overdraft permitted only within configured negative limit; exceeding attempt rejected.
- Checking overdraft always rejected.
- Transfer operations preserve global sum invariant.
- Suspicious rules fire only under documented thresholds (LargeAmount: strictly >; RapidFire: >= count in window).
- All timestamps use system clock in UTC assumption.
- Central configuration struct holds all thresholds / limits.
- No lost or duplicated ledger entries; each public operation yields at most one transaction record.
- Daily summary aggregates counts and per-account-type net deltas accurately for the given UTC date.
- Audit log includes all required actions with correct entity references.
- DocTest unit tests complete in < 5 s and cover:
  - Account rule enforcement
  - Concurrency invariant (parallel transfers)
  - Suspicious rule trigger and non-trigger scenarios
  - Reporting aggregation accuracy
- Public types and members documented with Doxygen comments.

---

## 6. Out of Scope (Baseline)
- Persistent storage (disk, database)
- Authentication / authorization
- Multi-currency or FX handling
- Interest / fees
- External logging infrastructure
- Cancellation tokens / cooperative cancellation (optional stretch feature)
- UI / network API layer

---

## 7. Glossary
- Overdraft Limit: Maximum absolute negative balance permitted for Business accounts.
- Minimum Balance: Lowest permissible post-operation balance for Savings accounts.
- Net Balance Delta (per account type): Sum of signed balance changes for accounts of that type in the reporting window.

---

## 8. References
- Implementation guidance & ordered prompts: see `README-bank-scenario.md`.
- Testing conventions: DocTest (separate test target).

---
