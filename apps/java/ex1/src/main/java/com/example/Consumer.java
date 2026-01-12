package com.example;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Consumer class that consumes messages produced by the Producer.
 * <p>
 * Thread-safety: This record is immutable; it holds a reference to an external CompletableFuture which
 * is thread-safe for completion and callbacks. Callers should ensure proper completion of the future.
 */
public record Consumer(CompletableFuture<String> _task) {
    /**
     * Canonical constructor to validate inputs.
     *
     * @param _task the CompletableFuture to be consumed; must not be null
     */
    public Consumer {
        Objects.requireNonNull(_task, "_task must not be null");
    }

    /**
     * Asynchronously consumes the message from the producer and prints it to the console.
     *
     * @return a CompletableFuture that completes after the message is consumed
     */
    public CompletableFuture<Void> consumeAsync() {
        return _task.thenAccept(System.out::println);
    }
}