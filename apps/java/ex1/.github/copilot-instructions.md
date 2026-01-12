---
applyTo: "**/src/main/java/**/*.java"
---

# Copilot Instructions
Coding instructions for this Java repository

## Java Source Files
Use a modern supported JDK. Avoid relying on preview features unless explicitly enabled in the build.

### General Guidelines
- Add a brief file header comment describing purpose
- Use descriptive package structure under com.example
- Favor immutability; use records for simple immutable data carriers
- Use sealed classes/interfaces for restricted hierarchies when appropriate
- Prefer var for local variables where type is clear
- Use text blocks for multi-line strings
- Apply pattern matching (instanceof / switch) instead of manual casts
- Follow standard naming: Classes PascalCase, methods/fields camelCase, constants UPPER_SNAKE_CASE
- Keep methods focused; ≤30 lines target
- Line length ≤120 chars
- Avoid deprecated APIs; prefer java.time, java.util.concurrent, java.nio
- No trailing whitespace; end files with newline

### Error Handling & Logging
- Fail fast with clear messages
- Catch only specific exceptions; avoid broad catch(Exception)
- Wrap lower-level exceptions with meaningful context (use custom unchecked exceptions sparingly)
- Use SLF4J parameterized logging: logger.info("Processing id {}", id);
- Do not swallow exceptions; log at error level before rethrow if not handled
- Use try-with-resources for Closeable/AutoCloseable
- Validate inputs early (Objects.requireNonNull, custom validation)

### Concurrency (Producer/Consumer Focus)
- Prefer high-level concurrency utilities (BlockingQueue, ExecutorService, CompletableFuture)
- For producer-consumer flows use: BlockingQueue<E> or Flow API when stream-like
- Avoid manual Thread management; use Executors or virtual threads when available for scalability
- Guard shared mutable state; prefer immutable objects and thread-safe collections
- Document thread-safety in class Javadoc (@ThreadSafe / @NotThreadSafe tags if used)

### Performance
- Avoid premature optimization; measure with JMH for critical paths
- Minimize synchronization scope
- Prefer bulk operations over per-element locks
- Use StringBuilder for manual concatenation in loops

### Documentation
- Provide Javadoc for all public classes, methods, and records
- Document assumptions, thread-safety, nullability (use @Nullable/@NonNull if annotations available)
- Include usage examples for complex APIs

### Testing Conventions
- Framework: JUnit 5 (Jupiter)
- Location: src/test/java mirrors src/main/java packages
- Class naming: <ClassName>Test
- Use @Test, @ParameterizedTest where suitable; @Nested for grouping
- Assertions: org.junit.jupiter.api.Assertions (or AssertJ if added)
- Mocking: Mockito (avoid over-mocking; prefer real objects for simple cases)
- Deterministic tests only (no sleeps; use Awaitility if async timing needed)
- Run: mvn test
- Coverage: JaCoCo via mvn verify (target ≥80% line coverage)
- Avoid relying on test execution order

### Build & Run
- Build: mvn clean verify
- Run app (example Main class): mvn exec:java -Dexec.mainClass="com.example.Main"
- Produce jar: mvn clean package (artifact in target/)
- Enable preview features (if needed): add --enable-preview to compiler & runtime configs in pom.xml

### Static Analysis
- Use Maven plugins (if configured): SpotBugs, Checkstyle, PMD
- Resolve all high/medium findings before merge
- Treat compiler warnings as errors when feasible

### Dependency Management
- Keep versions in pom.xml properties for easy upgrades
- Prefer BOM imports for aligned versions
- Remove unused dependencies; avoid duplications
- No snapshot dependencies for production branches

### SQL Files
**/*.sql (SQLite syntax)
- Uppercase SQL keywords
- Comment complex queries
- Use explicit JOIN syntax
- Qualify all column references with table names
- Prefer prepared statements in Java code (no string concatenation for SQL)

### Configuration & Properties
- Externalize configurable values (e.g., application.properties)
- Do not commit secrets; use environment variables or placeholders

### Commit Message Guidelines
- Subject: ≤50 chars, imperative mood (e.g., "Implement blocking queue")
- Blank line between subject and body
- Body (optional): wrap at 72 chars; explain rationale & context
- Avoid vague subjects ("Update code"); be specific
- Reference issue/task IDs when applicable

### Branching / Workflow
- Use feature branches; keep main branch stable
- Rebase (not merge) main into feature branches before PR
- Small, focused PRs (≤ ~400 lines diff preferred)

### Code Review Expectations
- Ensure tests pass locally before PR
- Provide rationale for non-trivial design choices in PR description
- Address all review comments or justify alternatives

### Security
- Validate external inputs
- Avoid reflection unless necessary
- Keep dependencies updated to patch CVEs

### Style Enforcement
- If a style config (e.g., Checkstyle/Spotless) exists, run: mvn spotless:apply (or configured plugin)
- CI must fail on style violations

### Example Test Command
- mvn clean test
- mvn verify (includes integration tests & coverage if configured)

### Example Coverage Report
- After mvn verify inspect: target/site/jacoco/index.html

## Notes
- Prefer features only if supported by the configured JDK; annotate usage if enabling any preview features
- Keep producer-consumer components small, testable, and well-documented
