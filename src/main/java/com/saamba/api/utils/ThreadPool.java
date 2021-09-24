package com.saamba.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ThreadPool {

    private final BlockingQueue<Runnable> tasks;
    private final List<Task> runnables = new ArrayList<>();
    private boolean isStopped;

    public ThreadPool(int taskMax, int threadMax) {
        tasks = new ArrayBlockingQueue<>(taskMax);
        for(int i = 0; i < threadMax; ++i)
            runnables.add(new Task(tasks));
        for(Task runnable : runnables)
            new Thread(runnable).start();
    }

    public synchronized void execute(Runnable task) throws Exception {
        if(this.isStopped) throw new IllegalStateException("Thread pool has terminated.");
        this.tasks.offer(task);
    }

    public synchronized void stop() {
        this.isStopped = true;
        for(Task t : runnables)
            t.doStop();
    }

    public synchronized boolean waitForCompletion() {
        while(this.tasks.size() > 0) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                log.info("Thread interrupted.", e);
                return false;
            }
        }
        return true;
    }
}

