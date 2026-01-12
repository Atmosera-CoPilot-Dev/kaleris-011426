# Go

Provide an expert comparative overview of Go static/security tooling (go vet, staticcheck, govulncheck, Gosec, Semgrep, CodeQL Go queries, Sonar, commercial SAST) including detection domains, false‑positive pressure points, and optimal layering order for velocity vs depth.

How do I design a multi-stage SARIF pipeline for Go that: runs staticcheck + Gosec + govulncheck + Semgrep, normalizes severities, de‑duplicates by (rule + location hash), merges into a single SARIF, and publishes to GitHub Code Scanning with baseline awareness?

Detail a hardened Go module supply‑chain strategy: proxy + sumdb trust model, GONOSUMDB/GOPRIVATE handling, checksum database integrity, vendoring vs minimal version selection (MVS) risks, dependency diff gating, SBOM generation (CycloneDX + syft), and provenance/attestation via cosign + SLSA levels.

Show how to enforce reproducible and hermetic builds: pinned Go toolchain via go.env / asdf / Docker, -trimpath, -buildvcs=false, deterministic ldflags (version + commit), and module vendoring verification in CI.

Explain integrating fuzzing (native go test -fuzz) alongside sanitizers (memory/race via -race, memory sanitizer with Clang/cgo builds) and coverage-driven prioritization of fuzz targets; include resource caps and corpus seeding strategy.

Provide a secure concurrency audit prompt: detect goroutine leaks, unsafe channel patterns (unbounded sends, select default misuse), context cancellation propagation failures, time.After misuse, data races masked by maps guarded only informally.

Outline a hardened configuration for staticcheck: enabling all SA / ST / S / performance checks except selectively suppressed ones with documented justification in a central config; include approach for enforcing zero new issues via diff filtering.

Describe a govulncheck integration pattern that: runs in CI, fails only on newly introduced reachable vulns, annotates PR with advisory metadata + fix versions, and stores historical vuln trend metrics.

Show how to construct a Gosec profile that: elevates crypto/injection (G401+, G501+), suppresses noise in testdata/, enforces no hardcoded credentials (credential pattern extensions), and integrates custom rules for internal APIs (e.g., restricted logging or unsafe deserialization).

Provide a prompt for generating a policy script that: ingests merged SARIF + govulncheck JSON, maps severities to org policy tiers (Block/Review/Info), enforces waiver expirations, and outputs a markdown summary table with delta counts per category.

Explain strategies to minimize false positives in Go security scanning: consistent error wrapping (errors.Join / fmt.Errorf with %w), constrained use of reflection, avoiding shadowed identifiers, centralizing crypto primitives, and adopting context-aware helper APIs.

Detail an advanced secret scanning + config hygiene layer: scanning embedded files (go:embed), environment variable allowlist gating, detection of accidental inclusion of .pem/.key in modules, and enforcing build tag segregation (//go:build) for test-only instrumentation.

Provide a prompt to generate a GitHub Actions workflow that: caches module downloads, builds with -race for PRs, runs staticcheck + Gosec + govulncheck + fuzz smoke (short timeout), uploads SARIF, gates on critical findings, and posts inline annotations only for newly introduced issues.

Describe a disciplined approach to CGO risk reduction: audit unsafe.Pointer usage, forbid dynamic linking to unvetted native libs, enforce -buildmode=pie where applicable, and run CodeQL + Semgrep queries targeting boundary crossing patterns.

Show how to implement differential scanning limited to changed packages (git diff to determine package list), falling back to full scans on main branch or scheduled runs, with caching keyed by go.sum hash + tool versions.
