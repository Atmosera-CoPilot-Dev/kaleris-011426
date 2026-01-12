# JavaScript/TypeScript

What are the recommended static analysis tools for JavaScript/TypeScript projects?

How do I implement ESLint for a JavaScript/TypeScript project built with VS Code?

What are the steps to configure and generate a SARIF report using ESLint for a JavaScript/TypeScript project?

How can I use the SARIF format to integrate static analysis tools with my CI/CD pipeline?  Include an example of GitHub Actions workflow that integrates ESLint and Pylint to generate SARIF reports.

How do I integrate the SARIF report into GitHub's Security tab?

What is an advanced ESLint + TypeScript hardening setup (tsconfig strict options, eslint-plugin-security, eslint-plugin-sonarjs, import restrictions, deprecation checks, custom rule authoring) and how do I balance security vs false positives?

How do I integrate JavaScript/TypeScript supply-chain security scanning (npm/pnpm/yarn audit, osv-scanner, Snyk, dependency-review-action) to produce/merge SARIF with ESLint results and enforce severity-based merge gating?

How can I design an incremental CI pipeline that: runs ESLint only on changed files, maps coverage to prioritize hotspots, merges static analysis + secret scan + dependency findings, and posts summarized SARIF deltas as a PR comment?
