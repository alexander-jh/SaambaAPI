package com.saamba.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class Task implements Runnable {
    private Thread thread = null;
    private final BlockingQueue<Runnable> tasks;
    private boolean isStopped = false;

    public Task(BlockingQueue<Runnable> q) {
        tasks = q;
    }

    public void run() {
        this.thread = Thread.currentThread();
        while(!isStopped()) {
            try {
                Runnable runnable = tasks.take();
                runnable.run();
            } catch(Exception e) {
                log.info("Thread " + this.thread.getId() + " failed.", e);
            }
        }
    }

    public synchronized void doStop() {
        isStopped = true;
        this.thread.interrupt();
    }

    public synchronized boolean isStopped() { return isStopped; }
}