package net.syphlex.skyblock.manager.cooldown;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.cooldown.impl.IslandCreateCooldown;
import net.syphlex.skyblock.manager.cooldown.impl.IslandDeleteCooldown;
import net.syphlex.skyblock.manager.cooldown.type.Cooldown;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Getter
public class CooldownHandler {

    private final ArrayList<Cooldown> cooldowns = new ArrayList<>();

    private final IslandDeleteCooldown islandDeleteCooldown = new IslandDeleteCooldown();
    private final IslandCreateCooldown islandCreateCooldown = new IslandCreateCooldown();

    private BukkitTask task;

    public CooldownHandler(){
        this.cooldowns.add(this.islandDeleteCooldown);
        this.cooldowns.add(this.islandCreateCooldown);
    }

    /*
    10 - minute cooldown garbage collector!!
     */
    public void onEnable(){
        this.task = new BukkitRunnable(){
            @Override
            public void run(){
                Skyblock.get().getThreadHandler().fire(() -> {
                    // loop through all the cooldowns
                    for (Cooldown cooldown : cooldowns) {

                        // loop through every cooldown entry
                        for (Map.Entry<UUID, Long> entry : cooldown.getCache().entrySet()) {

                            final UUID uuid = entry.getKey();

                            // remove any expired entries to prevent memory leaks
                            // (when players log off the cooldown will still be there)
                            if (cooldown.isExpired(uuid))
                                cooldown.stop(uuid);

                        }
                    }
                });
            }
        }.runTaskTimerAsynchronously(Skyblock.get(), 0L, 12000L);
    }

    public void onDisable(){
        this.task.cancel();
    }
}
