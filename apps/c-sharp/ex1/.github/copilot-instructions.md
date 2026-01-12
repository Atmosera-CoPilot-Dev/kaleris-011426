# Copilot Instructions

---
designation: csharp-dotnet
applyTo: "**/*.cs"
description: Core C#/.NET 8 standards (condensed ~50%).
---

### Runtime
- Target: .NET 8 (`net8.0`), C# 12, nullable enabled.
- Use modern features when they simplify (primary constructors, file-scoped namespaces).

### Essentials
- Header comment + XML docs for public APIs (concise summaries).
- Naming: PascalCase types/methods; camelCase locals/parameters; `_camelCase` private readonly; ALL_CAPS constants.
- Favor immutability (`readonly`, records, init setters); avoid shared mutable statics.
- Use `var` when type obvious; be explicit when not.
- Keep methods single-purpose; extract helpers early.
- Dispose resources (`IDisposable` / `using` statements); no leaks.
- Handle `null` explicitly; avoid `!` except with rationale.

### Errors & Logging
- Catch specific exceptions; never empty catches.
- Clear exception messages (cause + expectation).
- Wrap I/O (`IOException`, `UnauthorizedAccessException`, etc.).
- Use `ILogger` / structured logging; no sensitive data.

### Performance & Safety
- Avoid needless allocations; consider `Span<T>` in hot paths.
- Validate inputs early (guard clauses) and design for thread-safety (immutability or `lock`).

### Testing (NUnit)
- Mirror namespaces; name tests `<TypeName>Tests.cs` or scenario.
- Use `Assert.That`; prefer `TestCase` for params.
- Deterministic: control randomness, no external network/time reliance.
- Clean side effects in `[SetUp]/[TearDown]`.
- Aim ≥80% coverage; run `dotnet test` (add Coverlet if configured).

### Build & Tooling
- Minimal `.csproj`; remove unused refs.
- Enable analyzers `<EnableNETAnalyzers>true</EnableNETAnalyzers>`; treat warnings seriously.
- Central style via `.editorconfig` when needed.

### Documentation
- Update README for new public APIs or behavioral changes.
- Provide concise usage snippets in XML docs or README.

### Security
- No secrets/PII in logs.
- Validate and sanitize file paths; use `Path.Combine`.


### Quick Commands
```bash
dotnet build
dotnet test
```




