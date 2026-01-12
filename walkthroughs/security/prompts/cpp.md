# C++ Security

Below are 10 useful Copilot chat prompts for a C++ developer focused on security.

1. **Provide a sample PR template that ties together MISRA C++ rules, cybersecurity controls, and static analysis output to strengthen code quality and security posture.**

2. **How can Copilot be systematically prompted to generate compliant code, reference specific rules, and align with PR and static analysis pipelines for security and safety standards?**

3. **Provide a comparative overview of key C++ static and security-focused analysis tools (Cppcheck, Clang-Tidy, Clang Static Analyzer, sanitizers, fuzzers, CodeQL, SARIF-producing linters, commercial options). When do I apply each for security vs safety vs quality vs compliance?**

4. **How do I implement Cppcheck in VS Code with: a tasks.json integration, MISRA C++ (2008/2023) addons (if available), inline and external suppressions, handling third-party headers efficiently?**

5. **How do I generate SARIF from both Cppcheck and Clang-Tidy, merge the results, and successfully upload them to GitHub Code Scanning (including severity mapping, baseline strategy, and troubleshooting missing alerts)?**

6. **Provide an advanced GitHub Actions workflow for a CMake-based C++ project that: runs matrix builds, caches dependencies and compile_commands.json, executes Cppcheck (security + MISRA profile), Clang-Tidy (selected security/cert checks), optional CodeQL, enforces fail-on-severity thresholds, and publishes SARIF + logs.**

7. **How do I enforce and reconcile MISRA C++ compliance alongside modern C++ features (move semantics, constexpr, templates) and compare/align MISRA vs CERT C++ vs AUTOSAR C++14 rules for a safety-critical codebase?**

8. **Show how to configure hardened and instrumented builds: integrating AddressSanitizer, UndefinedBehaviorSanitizer, ThreadSanitizer in debug, plus production hardening flags (-fstack-protector-strong, -D_FORTIFY_SOURCE=3, -fPIE -pie, relro/now, stack clash protection) while keeping analyzer compatibility.**

9. **What is the role of fuzz testing (libFuzzer, AFL++, honggfuzz) in complementing static analysis and sanitizers? Provide a minimal fuzz target, build flags, and CI integration pattern.**

10. **How do I implement secure third-party dependency management: SBOM generation (CycloneDX or Syft), hash pinning, license/security scanning, and integrate SBOM + static analysis artifacts into CI/CD reporting?**
