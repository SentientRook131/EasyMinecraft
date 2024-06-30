package com.sen.QuestionnaireCore;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Getter<T> {
    private CompletableFuture<T> future = new CompletableFuture<>();
    private final Object lock = new Object();

    public Getter() {

    }

    public T get() throws InterruptedException, ExecutionException {
        return future.get();
    }
    public CompletableFuture<T> asyncGet() {
        return future;
    }

    public void put(T content) {
        future.complete(content);
    }
}