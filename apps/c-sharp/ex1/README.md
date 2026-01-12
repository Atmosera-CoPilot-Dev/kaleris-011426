## Singleton Logger Exercise


## Current Naive Implementation Description
The current `Logger` class is a singleton that provides a simple logging mechanism by appending messages to a text file. It has the following characteristics:
* Non-thread-safe
* No disposal or resource management
* Opens file for every write (inefficient)
* No formatting, timestamps, or log levels
* No configuration or dependency injection seam
* No validation or error handling

* You will evolve it step by step. Each phase has: (1) Goal, (2) Prompts, (3) Acceptance. Do not skip phases.

There is an existing NUnit test project with a single test that verifies singleton identity. You will expand this test suite as you progress.
---
### Phase 0: Understand Baseline
Goal: Be sure you know exactly what the naive code does and its risks.
Prompts:
1. "Explain how the current Logger singleton works and list all design flaws (threading, IO, testability, performance, extensibility)."
2. "List failure modes if multiple threads call Log simultaneously."
Acceptance: Written notes enumerating at least 6 concrete limitations.

---
### Phase 1: Basic Tests
Goal: Establish guardrail tests before refactoring.
Tests to add:
1. Singleton identity (two calls to `Instance` are same reference)
2. Single message is appended to file
3. Multiple sequential messages preserve order
Prompts:
1. "Generate NUnit tests for singleton identity and single message logging using a temporary test file path." (You may temporarily hardcode or copy file then refactor for configurability later.)
Acceptance: Tests red/green on naive implementation (all green except ordering not yet robust if you haven't added it).

---
### Phase 2: Configurable Output
Goal: Allow supplying log file path (constructor or settable property) while preserving singleton semantics (design decision!).
Prompts:
1. "Propose two approaches to make log file path configurable while keeping a test seam (static factory vs lazy + init)."
2. "Refactor Logger to allow setting path before first use; reject late changes." 
Acceptance: Tests updated to use isolated temp file. Changing path after first Log has no effect (documented or throws).

---
### Phase 3: Resource Management & Performance
Goal: Keep file open, add flushing/disposing pattern.
Prompts:
1. "Add StreamWriter with lazy creation and implement IDisposable correctly (idempotent Dispose)."
2. "Add a finalizer? Evaluate necessity and recommend yes/no." 
Acceptance: Stream not reopened each write (verify by counting file handle opens if instrumented / reasoning). Add test ensuring Dispose closes file (log after dispose should throw).

---
### Phase 4: Thread Safety
Goal: Make logging safe under concurrent calls.
Prompts:
1. "Show two thread-safety strategies (lock vs concurrent queue + background writer) and compare trade-offs."
2. "Implement simplest `lock` based solution around shared StreamWriter."
Acceptance: Concurrency test: spawn N threads each writing M lines; assert total lines == N*M and no interleaving corruption (basic length check).

---
### Phase 5: Log Levels & Formatting
Goal: Add `Info`, `Warning`, `Error` (enum) plus timestamp.
Prompts:
1. "Extend Logger with LogLevel enum and a single internal write method."
2. "Add ISO-8601 UTC timestamp formatting pattern."
Acceptance: Log lines start with `[UTC-TIMESTAMP] [LEVEL] message`. Tests assert pattern via regex.

---
### Phase 6: Error Handling & Validation
Goal: Robustness around bad input and IO exceptions.
Prompts:
1. "Add guard: null/whitespace messages rejected with ArgumentException."
2. "Wrap write in try/catch and surface custom LoggerIOException if disk write fails."
Acceptance: Tests for null message, simulated IO exception (e.g., open file with exclusive lock) verifying custom exception.

---
### Phase 7: Dependency Injection Friendly Design
Goal: Provide interface abstraction.
Prompts:
1. "Create ILogger interface exposing level-based logging; adapt concrete Logger."
2. "Refactor singleton access to optional registration via factory or DI container example."
Acceptance: Production still can use singleton; tests can new up an instance or use a stub.

---
### Phase 8: Asynchronous Logging (Optional Advanced)
Goal: Non-blocking log calls.
Prompts:
1. "Refactor to queue log entries and process with a single background Task + channel."
2. "Ensure graceful shutdown (Flush + Dispose awaiting queue drain)."
Acceptance: Stress test with high volume: all lines written; dispose drains queue.

---
### Phase 9: Documentation & Cleanup
Goal: Professional polish.
Prompts:
1. "Generate XML docs for public API including thread-safety notes and exceptions."
2. "Produce README snippet summarizing usage and extension points."
Acceptance: Clean build, no undocumented public members, tests passing.

---
### Advanced Enhancements (Optional)
* Log rotation (max size / daily)
	- Prompt: "Propose a size-based rotation with timestamped archive names (no external deps)."
* Structured logging (key=value pairs)
	- Prompt: "Refactor Log signature to accept context key/value pairs and format deterministically."
* JSON sink + pluggable appenders
	- Prompt: "Design an ILogSink interface and add a JSON file sink alongside the existing text sink."
* Metrics counters (messages/sec)
	- Prompt: "Add lightweight counters (total, per level, messages/sec) and expose a snapshot method."

---
Proceed in order; each phase builds on prior work.