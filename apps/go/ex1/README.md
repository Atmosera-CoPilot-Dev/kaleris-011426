# Microservice Performance Issues

This repository contains a Go-based microservice demonstrating intentional performance issues in a user profile service (create, update, retrieve) to practice identifying and fixing bottlenecks.


### Level 1: Understand current state
1. Explain how the /getUserProfile handler works and list all functions it calls.
2. Highlight every place time.Sleep is used and explain its effect on throughput.

### Level 2: Spot inefficiencies
3. Show all string concatenations used to build the JSON response; suggest a more efficient alternative.
4. Identify repeated database (simulated) lookups for the same user; propose a single-fetch approach.

### Level 3: Establish a baseline
5. Generate a benchmark test for the current getUserProfile logic.
6. Add net/http/pprof to the server and give commands to capture a 30s CPU profile.

### Level 4: Optimize data access
7. Refactor the user data fetches into one function returning a composite struct (user, address, preferences).
8. Propose an interface abstraction for the data layer enabling mocking/optimization.

### Level 5: Improve JSON construction
9. Replace manual concatenation with encoding/json (show diff only).
10. If manual is required, rewrite using strings.Builder and explain the allocation benefit.

### Level 6: Introduce caching
11. Design an in-memory cache (struct + map + RWMutex + TTL fields) for user profiles.
12. Modify the handler to check cache first, populate on miss, and log hit/miss counts.

### Level 7: Concurrency & latency hiding
13. Run the three simulated queries concurrently with goroutines + sync.WaitGroup; aggregate results safely.
14. Add a context with timeout to the handler and propagate it to simulated DB calls.

### Level 8: Observability & robustness
15. Add structured logging (log/slog) capturing request ID, latency, and cache status.
16. Outline Prometheus-style metrics (counters, histograms) and where to increment them.

### Level 9: Advanced validation
17. Provide a go test -race command and interpret any reported races after refactor.
18. Create a benchmark comparing sequential vs concurrent fetch implementations.
19. Suggest hey load test commands for -c 1,10,100 and list key metrics to record.

### Level 10: Hardening
20. List all places missing error checks; propose improved early-return handling.
21. Add validation for userID query param with appropriate HTTP status codes on failure.
22. Implement graceful shutdown with context and server.Shutdown.

### Level 11: Refactoring & structure
23. Propose a new package layout (/api /service /store /cache /model) with responsibilities.
24. Extract the handler into its own file; show the new file content.

### Level 12: Review & roadmap
25. Summarize expected qualitative performance gains per change.
26. List next potential improvements (LRU cache, worker pools, protobuf, circuit breaker, connection pooling).

### Meta (reusable anytime)
27. Given the current diff, are there hidden allocations to reduce further?
28. Point out any goroutine leak risks and fixes.
29. Explain how to confirm no performance regression using benchmarks and tests.
30. Provide a production readiness checklist for this service.
