package net.syphlex.skyblock.manager.leaderboard;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.data.Island;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LeaderboardHandler {

    private final ArrayList<Leaderboard> leaderboards = new ArrayList<>();
    private BukkitTask scheduledTask;

    public void onEnable(){
        this.leaderboards.add(new Leaderboard("Top Islands"));

        this.scheduledTask = new BukkitRunnable(){
            @Override
            public void run(){
                calc();
            }
        }.runTaskTimerAsynchronously(Skyblock.get(), 0L, 36000L);
    }

    public void onDisable(){
        this.scheduledTask.cancel();
        this.leaderboards.clear();
    }

    public void calc() {

        clearCache();

        Skyblock.get().getThreadHandler().fire(() -> {

            for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
                for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().length(); c++) {
                    Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);
                    if (island == null) continue;
                    getTopIslands().getList().add(new LeaderData(
                            island.getIdentifier(),
                            (int)island.getWorth()
                    ));
                }
            }

            for (Leaderboard leaderboard : this.leaderboards)
                leaderboard.calc();
        });
    }

    public void clearCache(){
        for (Leaderboard leaderboard : this.leaderboards)
            leaderboard.getList().clear();
    }

    public Leaderboard getTopIslands(){
        return this.leaderboards.get(0);
    }
}
