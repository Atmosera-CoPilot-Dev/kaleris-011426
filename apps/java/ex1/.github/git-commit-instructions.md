# Git Commit Instructions

## Format
- Start with conventional commit type: feat, fix, docs, style, refactor, test, chore
- Optional scope in parentheses right after type: (service), (controller), (model), (repository), (config), (build)
- Subject line (summary) under 50 characters, no trailing period
- Use imperative mood: "add" not "added" or "adds"
- Blank line after subject, then body (wrap at ~72 chars)
- Footer for metadata (issues, breaking changes)

## Examples
- feat(service): add order processing workflow
- fix(repository): handle null entity in save()
- docs(javadoc): update Manager class docs
- refactor(model): simplify equals/hashCode
- chore(build): update Gradle to 8.10 and JDK 21
- test(controller): add JUnit 5 tests for login

## Body Guidelines
- Required if the change is non-trivial (multiple files, logic changes, or needs rationale)
- Explain what and why, not how (code shows how)
- Reference issues: "Closes #123" (in footer)
- Breaking changes: start a footer line with `BREAKING CHANGE:`
- Mention build tool impacts: pom.xml or build.gradle changes
- Note API changes: public method signatures, package moves, module exports

## Footer Examples
BREAKING CHANGE: require JDK 21 and update Gradle wrapper
BREAKING CHANGE: rename com.example.api.UserService to com.example.user.UserService
Closes #123

## Allowed Types
- feat: new feature
- fix: bug fix
- docs: documentation only (README, Javadoc)
- style: formatting (no code logic change)
- refactor: code change that neither fixes a bug nor adds a feature
- test: add or modify tests (JUnit 5)
- chore: tooling, build, deps, maintenance (Maven/Gradle, JDK)

## Checklist Before Commit
- Subject < 50 chars
- Imperative mood
- Scope appropriate (package/module/component)
- Body wrapped and present when needed
- No trailing whitespace
- All referenced issues valid
- Build files updated if deps/tooling changed (pom.xml/build.gradle)
- Tests updated/added (JUnit 5) for public API changes
- Javadoc updated for modified public methods/classes
