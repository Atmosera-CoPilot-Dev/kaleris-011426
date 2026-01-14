# Git Commit Instructions

## Format
- Start with conventional commit type: feat, fix, docs, style, refactor, test, chore
- Optional scope in parentheses right after type: (auth), (api), (ui)
- Subject line (summary) under 50 characters, no trailing period
- Use imperative mood: "add" not "added" or "adds"
- Blank line after subject, then body (wrap at ~72 chars)
- Footer for metadata (issues, breaking changes)

## Examples
- feat(auth): add JWT token validation
- fix(api): resolve null pointer in user service
- docs(readme): update installation instructions
- refactor(user): simplify validation logic
- chore(ci): update workflow to Python 3.13

## Body Guidelines
- Required if the change is non-trivial (multiple files, logic changes, or needs rationale)
- Explain what and why, not how (code shows how)
- Reference issues: "Closes #123" (in footer)
- Breaking changes: start a footer line with `BREAKING CHANGE:`

## Footer Examples
BREAKING CHANGE: remove deprecated /v1 login endpoint
Closes #123

## Allowed Types
- feat: new feature
- fix: bug fix
- docs: documentation only
- style: formatting (no code logic change)
- refactor: code change that neither fixes a bug nor adds a feature
- test: add or modify tests
- chore: tooling, build, deps, maintenance

## Checklist Before Commit
- Subject < 50 chars
- Imperative mood
- Scope appropriate (omit if unclear)
- Body wrapped and present when needed
- No trailing whitespace
- All referenced issues valid
