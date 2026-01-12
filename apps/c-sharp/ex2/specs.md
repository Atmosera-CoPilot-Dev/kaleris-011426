# Logger Enhancement Design Specification

Version: 1.0
Status: Draft (Authoritative for this exercise)
Target Framework: .NET 8

## 1. Overview
This specification defines the required structural and behavioral enhancements to evolve a naive singleton `Logger` into a configurable, testable, thread-safe, and optionally asynchronous logging component. It provides phase-by-phase guidance, public API contracts, quality attributes, and a conformance checklist for use by both the student and GitHub Copilot.

## 2. Goals
- Preserve backward capability for simplest usage (`Logger.Instance.Log("msg")`).
- Introduce progressive improvements (configurability, performance, correctness) in isolated phases.
- Maintain single-responsibility focus: writing timestamped log entries to a file (core scope). 
- Provide deterministic, testable behavior under concurrency.

### 2.1 Non-Goals (Current Phase)
- Structured JSON logging (optional enhancement only).
- External dependency injection container integration (beyond simple factory seam).
- Log rotation, compression, network sinks (optional ideas only).
- Cross-process synchronization.

## 3. Quality Attributes
| Attribute | Target | Rationale |
|-----------|--------|-----------|
| Thread Safety | All public methods safe after Phase 4 | Prevent data corruption under concurrency |
| Performance | Single file open, amortized O(1) writes | Avoid repeated open/close overhead |
| Reliability | Defensive validation + custom exception | Clearer diagnostics & testability |
| Testability | Deterministic file path + injectable clock (optional) | Predictable assertions |
| Extensibility | Interface + sink abstraction (optional) | Future transports |
| Maintainability | SOLID alignment (SRP, DIP) | Eases iterative evolution |

## 4. Component Overview
- Logger (Concrete singleton implementation)
- ILogger (Interface abstraction introduced Phase 7)
- LogLevel (Enum introduced Phase 5)
- (Optional) ILogSink + FileLogSink / JsonFileLogSink (Advanced)
- (Optional) Async background writer infrastructure (Channel or BlockingCollection)

## 5. Phased Enhancement Roadmap
Each phase builds on prior phases and has explicit acceptance criteria.

### Phase 0: Baseline Understanding
Deliverable: Written analysis of existing flaws (?6 distinct issues: thread-safety, resource churn, no validation, race conditions, lack of abstraction, timestamp absence, silent failures, test coupling).

### Phase 1: Basic Tests
Add NUnit tests:
1. Singleton identity.
2. Single message appended.
3. Sequential ordering of multiple messages (same file) — allow simple chronological append assertion.
Acceptance: Tests pass on naive code (expect ordering holds under sequential use).

### Phase 2: Configurable Output Path
Add ability to set log file path before first write.
Options (document chosen):
- Static initialization method (idempotent).
- Lazy property set (throws if set after first write).
Acceptance:
- Tests create isolated temp files.
- Attempted late path change throws or is ignored (document behavior consistently).

### Phase 3: Resource Management
Introduce a persistent `StreamWriter` (append mode) with lazy creation.
- Implement `IDisposable` (idempotent; safe to call multiple times).
- No finalizer unless unmanaged handles added (document decision: likely omit).
Acceptance:
- After first write, subsequent writes reuse stream.
- Logging after dispose throws `ObjectDisposedException` (or custom) – tested.

### Phase 4: Thread Safety
Add a private lock object or switch to queue-based pattern (choose simplest: lock).
Acceptance:
- Concurrency test (N tasks × M messages) results in exactly N*M lines; no partial interleaving.
- No deadlocks (test completes within timeout).

### Phase 5: Log Levels & Formatting
Add `LogLevel { Info, Warning, Error }`.
Formatting: `[YYYY-MM-DDTHH:MM:SS.fffZ] [LEVEL] message`
- Timestamp in UTC (ISO 8601, `o` or custom fractional milliseconds 3 digits).
- Provide `Log(string message, LogLevel level = LogLevel.Info)` plus convenience `Info/Warning/Error` methods (optional).
Acceptance:
- Regex test ensures pattern.
- Level appears correctly for each call.

### Phase 6: Validation & Error Handling
Guards:
- Reject null/whitespace message -> `ArgumentException`.
Error wrapping:
- IO failures wrapped in `LoggerIOException : IOException` (custom).
Acceptance:
- Tests assert exceptions on invalid input & simulated IO fault.

### Phase 7: Abstraction / DI Friendliness
Introduce `ILogger`:
```csharp
public interface ILogger {
  void Log(string message, LogLevel level = LogLevel.Info);
  void Info(string message);
  void Warning(string message);
  void Error(string message);
}
```
Singleton remains accessible via `Logger.Instance` but constructor may become internal/protected to restrict uncontrolled creation (or keep public for tests with a separate factory method). Provide a static `Logger.Configure(LoggerOptions options)` pre-first-use.
Acceptance:
- Tests can substitute a stub implementation of `ILogger`.
- Backward usage unchanged.

### Phase 8 (Optional Advanced): Asynchronous Logging
Strategy choices (document selected):
- `Channel<string>` with background consumer.
- `BlockingCollection<string>`.
Requirements:
- Non-blocking `Log` enqueues.
- Background task flushes entries using existing formatting + levels.
- `Dispose` gracefully drains remaining entries (bounded wait) before closing stream.
- Cancellation token or timeout to avoid indefinite block on shutdown.
Acceptance:
- Stress test (high volume) ensures all lines drained after dispose.

### Phase 9: Documentation & Polish
- XML docs for all public members.
- README usage examples (sync + optional async mode).
Acceptance: No undocumented public API warnings (treat as quality goal).

### Advanced Enhancements (Optional)
A. Log Rotation (size or daily) — design only or prototype.
B. Structured Logging key=value pairs.
C. Pluggable sinks (`ILogSink`).
D. Metrics snapshot (total messages, per-level counts, throughput).
Each optional feature must remain additive and not break prior tests.

## 6. Public API (Target State After Phase 7)
```csharp
public enum LogLevel { Info, Warning, Error }

public sealed class Logger : ILogger, IDisposable {
  public static Logger Instance { get; } // Lazy singleton
  public static void Configure(LoggerOptions options); // Must be called before first Instance access or first Log

  public void Log(string message, LogLevel level = LogLevel.Info);
  public void Info(string message);
  public void Warning(string message);
  public void Error(string message);

  public void Dispose();
}

public sealed class LoggerOptions {
  public string FilePath { get; set; } = "log.txt"; // Must be non-empty
  public bool UseAsyncMode { get; set; } // Optional Phase 8
  public int? AsyncQueueCapacity { get; set; } // Optional bound
  public bool ThrowOnLateConfigure { get; set; } = true; // Behavior toggle
}
```
Notes:
- `Configure` semantics: If called after singleton materialization:
  - If `ThrowOnLateConfigure == true` => throw InvalidOperationException
  - Else ignore (no partial reconfiguration)

## 7. Thread Safety Model
- All public methods safe concurrently after Phase 4.
- Implementation uses a private `readonly object _sync = new();` guarding: stream creation, writes, disposal state changes.
- Async mode: only the enqueue path uses synchronization suitable for the chosen structure (Channel is already thread-safe).

## 8. Error Handling & Exceptions
| Scenario | Exception | Notes |
|----------|-----------|-------|
| Null/whitespace message | ArgumentException | Parameter name: message |
| File path invalid during configure | ArgumentException / Path-related | Validate early |
| Late configure (if enforced) | InvalidOperationException | Clear message including current state |
| IO write failure | LoggerIOException | Inner exception preserved |
| Logging after dispose | ObjectDisposedException | Standard pattern |

## 9. Formatting Rules
Line = `[TimestampUTC] [LEVEL] message` + newline (`Environment.NewLine`).
- Timestamp: `DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ")` (explicit pattern; avoid culture variance).
- No trailing spaces.
- Messages not trimmed automatically (validation rejects blank, but internal spacing preserved).

## 10. Configuration Rules
- File path must be absolute or relative valid path; create directories if necessary (optional extension).
- If file does not exist, create; if exists, append.
- No truncation on startup.
- Async mode: defaults to unbounded queue unless capacity set.

## 11. Disposal Semantics
- `Dispose` flushes and closes stream (sync mode).
- Async mode: signal completion, drain queue, then close stream.
- Multiple Dispose calls safe (guards with `_disposed` flag).

## 12. Testing Strategy
| Phase | Test Focus | Key Assertions |
|-------|------------|----------------|
| 1 | Identity, Append, Order | Same reference, file exists, order preserved |
| 2 | Config Path | Pre-config path used; late change fails |
| 3 | Disposal | Post-dispose log throws; file flushed |
| 4 | Concurrency | Line count exact; no garbled lines (regex) |
| 5 | Format & Levels | Regex match per line; proper level tokens |
| 6 | Validation & IO | Exceptions for bad input; custom exception wrap |
| 7 | Abstraction | Replace with stub in test; interface compiles |
| 8 | Async (opt) | High-volume drain completeness on dispose |
| 9 | Docs | (Manual) All public members documented |

Helper Recommendations:
- Use temp directory: `Path.Combine(Path.GetTempPath(), Guid.NewGuid().ToString("N"), "test.log")`.
- Provide utility to read all lines and assert counts.
- For simulated IO failure: open file with `FileShare.None` in another handle (platform differences considered) or inject a failing sink (advanced).

## 13. Metrics (Optional Advanced)
Add counters stored in `ConcurrentDictionary<LogLevel,int>` or atomic structure. Expose `LoggerSnapshot GetSnapshot()` with immutable counts and start time. Ensure snapshot retrieval lock-free or minimal lock (read under lock is acceptable if contention low).

## 14. Conformance Checklist
Before marking a phase complete:
- [ ] Public API matches spec for that phase.
- [ ] Tests for new behavior exist and pass.
- [ ] Previous phases' tests still green.
- [ ] Thread-safety invariants unaffected.
- [ ] No undocumented breaking changes.
- [ ] XML docs updated (Phase ?9).

## 15. Future Extension Hooks (Design Notes)
- Rotation: inject strategy interface `IRotationPolicy` evaluated before each write.
- Multiple sinks: adapt composite pattern (`ILogger` sends to each `ILogSink`).
- Structured logging: `Log(string message, LogLevel level, IReadOnlyDictionary<string, object>? context)` formatting deterministic: key order alphabetical.

## 16. Glossary
- Baseline Logger: Original naive singleton using per-call file open.
- Sink: Destination that persists log entries (file, JSON, etc.).
- Drain: Process of flushing queued log entries before shutdown.

## 17. Acceptance for Exercise Completion
Minimum acceptable delivery ends at Phase 7 with all mandatory tests green and spec-conformant formatting + thread safety. Optional phases may be included if fully tested and non-breaking.

---
End of Specification.
