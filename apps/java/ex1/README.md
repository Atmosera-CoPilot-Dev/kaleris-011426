This repository contains a simple Java implementation of the Producer-Consumer pattern using a blocking queue and multithreading.

Use the following refined Copilot Chat prompts (≤10) to enhance the existing codebase rather than recreate it from scratch:

# Java Producer-Consumer — Copilot Chat Prompts (Refined)

1. "Review existing classes (Producer, Consumer, Main); summarize current behavior and identify gaps (logging, shutdown, tests, resilience)."
2. "Propose an improved design: clarify class responsibilities, data flow, thread model, shutdown strategy, error handling, and extension points."
3. "Add structured logging (SLF4J): list what to log at DEBUG/INFO/WARN/ERROR for Producer, Consumer, and Main."
4. "Enhance Producer for graceful shutdown, backpressure awareness, and resource management; suggest code changes."
5. "Enhance Consumer with retry strategy, exception handling guidance, and metrics/logging improvements."
6. "Improve Main: CLI arg parsing (e.g., queue capacity, rates), wiring, and JVM shutdown hook; show diff."
7. "Generate targeted JUnit 5 test cases (unit + integration) for queue flow, retry logic, shutdown correctness, and error paths."
8. "Suggest performance and scalability optimizations (queue sizing, batching, rate limiting, tuning ExecutorService)."
9. "Draft concise README sections: build, run commands, sample output, and how to extend (additional consumers, message types)."
10. "Provide a final review checklist with commit message templates for each logical change."




