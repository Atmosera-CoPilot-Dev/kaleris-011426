# .NET / C#

Below are prompts for a C# .NET developer focused on security. Select the most relevant ones for your needs and let Copilot do the rest.

## Prompt Ladder

0. **I am a seasoned .NET professional with average security knowledge. Review the following prompt suggestions in the context of security and provide a concise explanation of their purpose and how they help improve security practices for .NET developers using C#.**

1. **How do nullable reference types act as a semantic safety net in C# and what are best practices for enabling them across a solution?**

2. **Explain how to configure .editorconfig for Roslyn analyzer severity mapping (error/warn/silent/none) and selectively use /warnaserror for security categories.**

3. **Describe the difference between solution-wide and project-level analyzer configuration, including performance trade-offs in large multi-target solutions.**

4. **Provide an expert-level breakdown of the built-in Roslyn analyzer ecosystem for C# (CA*, IDE*, security-oriented rules), including AnalysisLevel (latest vs preview).**

5. **Produce a comparative evaluation of supplemental .NET static/security analyzers that emit SARIF (SecurityCodeScan, DevSkim, CodeQL C# queries, Semgrep .NET rules, SonarC#, Snyk Code, commercial SAST): detection domains, false-positive patterns, licensing/operational cost, and recommended layering order.**

6. **List precise steps to integrate SecurityCodeScan into a multi-target (net6.0, net8.0) solution: PackageReference pinning, analyzer inclusion, conditional TreatWarningsAsErrors for only security rule IDs, validating analyzer execution, and ensuring IDE + CI parity.**

7. **Detail a hardened procedure to configure Roslyn analyzers for high-fidelity SARIF output: SDK pinning, Directory.Build.props, granular severity tuning, reproducible builds, msbuild SARIF logging, summary generation, and baseline diffing.**

8. **Design a polyglot GitHub Actions workflow merging C# (Roslyn + SecurityCodeScan) with ESLint and Pylint SARIF: matrix, caching, incremental scans, scheduled full scan, concurrency cancellation, gating, and artifact retention.**

9. **Explain end-to-end SARIF ingestion into GitHub Code Scanning: fingerprint construction, path normalization, baseline/new/reopened alert semantics, root causes of dropped results, verification, and deduplication validation.**

10. **Describe a policy enforcement script (jq/minimal C#) that normalizes SARIF severities, correlates with a baseline, supports waiver metadata, fails on new High/Critical or expired waivers, and emits actionable summaries with remediation guidance.**
