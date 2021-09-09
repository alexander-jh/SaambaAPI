package com.saamba.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

    private final BlockingQueue<Runnable> tasks;
    private final List<Task> runnables = new ArrayList<>();
    private boolean isStopped;

    public ThreadPool(int taskMax, int threadMax) {
        tasks = new ArrayBlockingQueue<>(taskMax);
        for(int i = 0; i < threadMax; ++i) {
            Task task = new Task(tasks);
            runnables.add(new Task(tasks));
        }
        for(Task runnable : runnables)
            new Thread(runnable).start();
    }

    public synchronized void execute(Runnable task) throws Exception {
        if(this.isStopped) throw new IllegalStateException("Pool is closed.\n");
        this.tasks.offer(task);
    }

    public synchronized void stop() {
        this.isStopped = true;
        for(Task t : runnables)
            t.doStop();
    }

    public synchronized void waitForCompletion() {
        while(this.tasks.size() > 0) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}

