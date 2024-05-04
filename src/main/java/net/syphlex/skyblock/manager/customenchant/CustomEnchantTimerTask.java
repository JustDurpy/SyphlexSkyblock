package net.syphlex.skyblock.manager.customenchant;

import net.syphlex.skyblock.Skyblock;
import org.bukkit.Bukkit;

public class CustomEnchantTimerTask
{

    private int task;
    private int seconds;

    public CustomEnchantTimerTask(Runnable runnable, Runnable second, Runnable finish, double limit) {
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Skyblock.get(),
                () -> {
                    runnable.run();
                    seconds++;
                    if (seconds % 20 == 0) second.run();
                    if ((seconds) / 20.0 >= limit) {
                        finish.run();
                        Bukkit.getScheduler().cancelTask(this.task);
                    }
                }, 1, 1L);
    }

}
