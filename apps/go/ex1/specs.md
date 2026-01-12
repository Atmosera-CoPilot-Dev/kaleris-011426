# Microservice Performance Issues – Requirements & Specification

This repository contains a Go-based microservice designed to demonstrate common performance issues and poor design practices. The microservice handles **user profile management**, including creating, updating, and retrieving user profiles. It intentionally includes inefficiencies to serve as a teaching tool for identifying and resolving performance bottlenecks.

---

## Problematic Endpoint: `/getUserProfile`

The `/getUserProfile` endpoint retrieves a user's profile by their ID. However, the implementation suffers from several inefficiencies:

1. **Multiple Queries**:
   - The service performs three separate queries to fetch the user's basic information, address, and preferences.
2. **Inefficient String Concatenation**:
   - The JSON response is built using string concatenation in a loop, leading to excessive memory allocation.
3. **Blocking Operations**:
   - Each database query introduces a delay (`time.Sleep`) to simulate slow database performance, blocking the main thread.

---

## Performance Issues

Under high load, the current design leads to the following problems:

- **High Contention**:
  - Multiple queries per request increase the load, reducing throughput.
- **Increased Memory Usage**:
  - Inefficient string manipulation results in excessive memory allocation and garbage collection overhead.
- **Slow Response Times**:
  - Blocking operations and lack of caching cause significant delays in processing requests.

---

## Code Overview

### Simulated Database
The application uses a simulated in-memory database to store user data. Each query introduces an artificial delay to mimic real-world database latency.

### Endpoint Implementation
The `/getUserProfile` endpoint:
- Fetches user data in multiple steps.
- Constructs the JSON response using inefficient string concatenation.
- Does not handle errors gracefully or implement caching.

---

## Suggested Improvements

1. Optimize Database Queries (single composite fetch)
2. Implement Caching (in-memory with TTL)
3. Use `strings.Builder` or `encoding/json`
4. Add concurrency (goroutines + sync mechanisms)
5. Propagate context with timeout
6. Structured logging & metrics
7. Proper error handling and validation
8. Graceful shutdown
9. Refactor into layered architecture
10. Add benchmarks, profiling, and load tests

---

## Running the Application

1. Build:
   ```bash
   go build -o user-service main.go
   ```
2. Run:
   ```bash
   ./user-service
   ```
3. Test endpoint:
   ```bash
   curl -X GET "http://localhost:8080/getUserProfile?userID=1"
   ```
4. Observe response time, data correctness, and console logs.
5. Load test:
   ```bash
   hey -n 1000 -c 10 "http://localhost:8080/getUserProfile?userID=1"
   ```
6. Monitor latency, throughput, error rate, memory, and CPU.

---

## Purpose

Use this specification as the baseline to iteratively improve performance, reliability, and observability while applying the prompt ladder in README.md.
