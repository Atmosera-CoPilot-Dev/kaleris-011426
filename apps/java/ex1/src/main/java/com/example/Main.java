// This file serves as the entry point of the application.
// It creates instances of Producer and Consumer, sets up the task completion source,
// and starts the producer and consumer tasks, waiting for both to complete.

package com.example;

import java.util.concurrent.CompletableFuture;

public class Main {
  public static void main(String[] args) {
    var taskCompletionSource = new CompletableFuture<String>();
    var producer = new Producer(taskCompletionSource);
    var consumer = new Consumer(taskCompletionSource);

    var producerTask = producer.produceAsync();
    var consumerTask = consumer.consumeAsync();

    CompletableFuture.allOf(producerTask, consumerTask).join();
  }
}