# Python Producer–Consumer Exercise

This workspace is an exercise in using GitHub Copilot to iteratively evolve a simple producer–consumer example. Follow the ladder: copy a prompt, paste into Copilot Chat, review, refine, accept, run tests, then proceed. Keep diffs small. Update the spec (docs/specs.md) as behavior changes.

Include the specs.md in your Copilot context to help it understand requirements.

## Mantra
Understand first, ask early, change small, keep it safe, test every step.

### What "Diff only" Means
Diff only: Ask the assistant to return just the minimal unified patch (the added/removed lines) for the change—no full file listings, no extra commentary. This keeps each step small, reviewable, and easy to apply or revert, reinforcing incremental development and clean commit history.


##  Prompt Ladder 

Assume `specs.md` is always in context. Each prompt asks for minimal change (diff only where code edits). Stop after integration passes.

1. Audit
"Summarize current files vs spec; list missing pieces. No code."

2. Plan
"List any new files or adjustments needed (producer, consumer, runner, logging, tests). Brief rationale. No code."

3. Scaffold
"Add/adjust skeletal classes & functions (docstrings, pass). Ensure `producer.py`, `consumer.py`, `run.py`, and `logging_utils.py` all exist. Return unified diff only."

4. Implement Producer
"Fill in Producer logic: items, validation (negative count or delay -> ValueError), sentinel enqueue once. Diff only."

5. Implement Consumer
"Fill in Consumer loop: stop on sentinel, count items, validate delay (negative -> ValueError), return count. Diff only."

6. Logging + Runner
"Add logging utility and implement run() (queue=10, start/join threads, optional env overrides). Diff only."

7. Tests (Unit)
"Add tests: producer normal & zero; consumer counting; validation errors (negative count, negative producer delay, negative consumer delay). Diff only."

8. Integration Test
"Add end-to-end test: produced == consumed; queue empty; sentinel consumed exactly once (no residual sentinel). Diff only."

Optional Extras (run only if time remains):
* Async / multiple consumers sketch

## Quick Usage Prompt
When you need setup/run steps, ask Copilot:  
"Give me commands to create a venv, install deps, run the threaded demo, and run tests for this project."

