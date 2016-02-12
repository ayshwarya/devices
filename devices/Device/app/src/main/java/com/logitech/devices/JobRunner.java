package com.logitech.devices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ayshwarya on 11/02/16.
 */

/**
 * Singleton to submit Runnables
 */
public enum JobRunner {

    instance;


    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }
}
