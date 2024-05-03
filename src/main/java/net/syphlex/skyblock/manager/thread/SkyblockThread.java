package net.syphlex.skyblock.manager.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkyblockThread {
    private final ExecutorService thread;
    private final String name;
    private int count;
    private double delay;

    public SkyblockThread(String name) {
        this.name = name;
        this.thread = Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder().setNameFormat(name).build());
    }

    public void fire(Runnable r) {
        this.count++;
        long timestamp = System.currentTimeMillis();
        this.thread.execute(() -> {
            r.run();
            this.count = Math.max(count - 1, 0);
            this.delay = System.currentTimeMillis() - timestamp;
        });
    }

    public int getCount() {
        return this.count;
    }

    public ExecutorService thread() {
        return this.thread;
    }

    public String name() {
        return this.name;
    }

    public double delay() {
        return this.delay;
    }
}