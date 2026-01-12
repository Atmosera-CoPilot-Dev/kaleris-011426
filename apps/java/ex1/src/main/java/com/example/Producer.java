package com.example;

import java.util.concurrent.CompletableFuture;

/**
 * Producer class that simulates delivering a message asynchronously.
 */
public class Producer {
  private final CompletableFuture<String> _taskCompletionSource;

  /**
   * Constructor to initialize the Producer with a CompletableFuture.
   *
   * @param taskCompletionSource the CompletableFuture to be used for delivering the message
   */
  public Producer(CompletableFuture<String> taskCompletionSource) {
    _taskCompletionSource = taskCompletionSource;
  }

  /**
   * Simulates the production of a message asynchronously.
   *
   * @return a CompletableFuture that completes when the production task finishes
   */
  public CompletableFuture<Void> produceAsync() {
    System.out.println("Delivering message...");
    // Run the production on a default async executor and return the future so callers can compose.
    return CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(5000); // Simulate some long-running task
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      _taskCompletionSource.complete("Message from producer...");
    });
  }
}