package net.syphlex.skyblock.manager.thread;

import java.util.ArrayList;

public class ThreadHandler {
    private final ArrayList<SkyblockThread> threads = new ArrayList<>();
    private final int maxThreads;

    public ThreadHandler() {
        this.maxThreads = Runtime.getRuntime().availableProcessors() * 2;
    }

    public void fire(Runnable r) {
        get().fire(r);
    }

    public SkyblockThread get() {

        int leastBusyIndex = 0;
        boolean found = false;

        for (int i = 0; i < this.threads.size(); i++) {
            if (this.threads.get(i).getCount() <= this.threads.get(leastBusyIndex).getCount()) {
                leastBusyIndex = i;
                found = true;
            }
        }

        int random = (int) (Math.random() * this.maxThreads);

        return (found ? this.threads.get(leastBusyIndex) : this.threads.get(random));
    }

    public void onEnable() {
        for (int i = 0; i < this.maxThreads; i++) {
            threads.add(new SkyblockThread("Skyblock Thread #" + i));
        }
    }

    public void onDisable() {
        for (SkyblockThread thread : this.threads) {
            thread.thread().shutdownNow();
        }
    }
}