## Minimal Lab Spec (Simplified)

Goal: In under ~20 minutes, build and test a tiny Producer–Consumer example using incremental Copilot prompts.

Core Files:
1. `producer.py` – `Producer(count=10, delay=0.05).produce()` enqueues work items then a single `SENTINEL`.
2. `consumer.py` – `Consumer(delay=0.02).consume()` dequeues until `SENTINEL`, returns number of real items processed.
3. `run.py` – Creates `Queue(maxsize=10)`, starts one producer + one consumer thread, joins them.
4. `logging_utils.py` – `get_logger(name)` returns a logger (INFO summary; DEBUG optional per-item).
5. `__init__.py` – defines and exports a unique `SENTINEL` object (and optional `__version__`).

Work Item Shape:
`{"id": int (0..N-1), "created_at": ISO8601 UTC string}`

Sentinel Rule:
Enqueue exactly one unique `SENTINEL` after all items (if `count=0`, only the sentinel).

Functional Requirements:
1. Produce N contiguous ids starting at 0 (default 10).
2. Enqueue `SENTINEL` exactly once.
3. Consumer stops on `SENTINEL` and returns processed item count.
4. Negative `count` or any negative delay raises `ValueError` (fail fast).
5. After `run()`, both threads are joined, queue is empty, sentinel consumed once.
6. Logging: start, per-item only at DEBUG, sentinel enqueue, consumer stop summary.

Optional (Keep simple; may skip if time tight): environment variable overrides for `PRODUCER_COUNT`, `PRODUCER_DELAY`, `CONSUMER_DELAY` via tiny helper functions (`get_env_int`, `get_env_float`). Omit if it risks the 20‑minute goal.

Non-Functional:
* Python >= 3.13 (CPython). Stdlib only (no third‑party except the test framework already present).
* Deterministic tests: use `delay=0` in unit tests.
* Keep modules tiny; minimal branching.

Interfaces:
`class Producer(queue, count=10, delay=0.05): produce() -> int` (returns produced item count)
`class Consumer(queue, delay=0.02): consume() -> int` (returns consumed real item count)
`run.run() -> None`
`get_logger(name) -> logging.Logger`
`get_env_int(name, default) -> int` (ValueError on bad/negative)
`get_env_float(name, default) -> float` (ValueError on bad/negative)

Testing Checklist (Minimum):
* Producer normal case (e.g., count=5) -> 5 items + sentinel.
* Producer count=0 -> only sentinel.
* Consumer counts correctly given prepared queue.
* Validation: negative count or delay raises `ValueError`.
* Integration: end-to-end run -> produced == consumed; queue empty.
* (Optional) Env parsing helpers: valid values and error on bad/negative input.

Stretch Ideas (Only after core is done): multiple consumers, asyncio version, metrics.

Status: simplified-v1. Expand only if a later prompt explicitly requests it.