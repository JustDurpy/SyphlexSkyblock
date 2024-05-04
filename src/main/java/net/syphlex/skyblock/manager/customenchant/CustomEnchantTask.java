package net.syphlex.skyblock.manager.customenchant;

public class CustomEnchantTask {

    private final Runnable runnable;

    public CustomEnchantTask(Runnable r) {
        this.runnable = r;
    }

    public Runnable runnable() {
        return this.runnable;
    }
}
