package com.example;

import com.example.Producer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class ProducerTest {

  @Test
  void produceAsync_ShouldSetResultInTaskCompletionSource() throws ExecutionException, InterruptedException {
    var taskCompletionSource = new CompletableFuture<String>();
    var producer = new Producer(taskCompletionSource);

    var producerTask = producer.produceAsync();

    // Wait for the producer to complete
    producerTask.join();

    // Verify that the result is set correctly
    assertEquals("Message from producer...", taskCompletionSource.get());
  }
}