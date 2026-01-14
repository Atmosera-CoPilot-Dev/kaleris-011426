---
applyTo:  "**/*.py"
description: "Coding instructions for Python files in this repository"
---

# Copilot Instructions
Coding instructions for this repository


### Runtime Version
- Minimum required Python version: 3.13.3
- Code must run on Python 3.13.3 or greater (CPython)
- Prefer using modern 3.13 features when they simplify code (e.g., improved f-string rules); note if 3.13+-specific

### General Guidelines
- Add a brief file header comment describing purpose
- Use f-strings; follow PEP 8
- Provide docstrings for all public functions/classes
- Add full type hints (mypy-friendly)
- Avoid deprecated APIs

### Error Handling
- Wrap fallible operations in try/except (no bare except)
- Log errors using standard logging format
- Fail fast with clear messages

### Testing Conventions
- Framework: unittest only
- File pattern: tests/test_<feature>.py (mirror source tree)
- Structure: classes subclassing unittest.TestCase; use setUp/tearDown as needed
- Use unittest.mock for external dependencies; keep tests deterministic
- Prefer self.assert* methods (no bare assert)
- Run: python -m unittest discover -s tests -p "test_*.py"
- Coverage (optional): coverage run -m unittest discover && coverage report -m
- Target: ≥80% coverage of executable code

### Version Control
- Clear, concise commit messages
- Use feature branches for new work


