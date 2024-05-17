package net.syphlex.skyblock.manager.leaderboard;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LeaderboardHandler {

    private final ArrayList<Leaderboard> leaderboards = new ArrayList<>();
    private BukkitTask scheduledTask;

    public void onEnable(){
        this.leaderboards.add(new Leaderboard("Top Islands"));
        this.leaderboards.add(new Leaderboard("Blocks Broken"));
        this.leaderboards.add(new Leaderboard("Mobs Killed"));
        this.leaderboards.add(new Leaderboard("Top Balances"));

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

            PluginUtil.broadcast(Messages.CALCULATE_ISLAND_WORTH.get());

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

            PluginUtil.broadcast(Messages.CALCULATE_ISLAND_WORTH_COMPLETE.get());
        });
    }

    public void clearCache(){
        for (Leaderboard leaderboard : this.leaderboards)
            leaderboard.getList().clear();
    }

    public Leaderboard getTopIslands(){
        return this.leaderboards.get(0);
    }

    public Leaderboard getTopBlocksBroken(){
        return this.leaderboards.get(0);
    }

    public Leaderboard getTopMobsKilled(){
        return this.leaderboards.get(0);
    }

    public Leaderboard getTopBalances(){
        return this.leaderboards.get(0);
    }
}
