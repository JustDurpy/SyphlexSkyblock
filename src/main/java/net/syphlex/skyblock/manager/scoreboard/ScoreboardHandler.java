package net.syphlex.skyblock.manager.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {

    private BukkitTask task;

    public void onEnable(){

        if (!ConfigEnum.SCOREBOARD_ENABLED.getAsBoolean())
            return;

        this.task = new BukkitRunnable(){
            @Override
            public void run(){
                for (Profile profile : Skyblock.get().getDataHandler().getMap().values()) {
                    if (profile.getScoreboard() == null) continue;
                    profile.getScoreboard().updateTitle(StringUtil.CC(ConfigEnum.SCOREBOARD_TITLE.getAsString()));
                    profile.getScoreboard().updateLines(createLines(profile, profile.hasIsland()));
                }
            }
        }.runTaskTimerAsynchronously(Skyblock.get(), 0L, 100L);
    }

    public void onDisable(){
        this.task.cancel();
    }

    private List<String> createLines(Profile profile, boolean island){
        List<String> board = new ArrayList<>();

        for (String l : (island
                ? ConfigEnum.ISLAND_SCOREBOARD_LINES.getAsList()
                : ConfigEnum.NORMAL_SCOREBOARD.getAsList()))
            board.add(StringUtil.HexCC(PlaceholderAPI.setPlaceholders(profile.getPlayer(), l)));

        return board;
    }

    public void delScoreboard(Profile profile){
        if (profile.getScoreboard() != null) profile.getScoreboard().delete();
        profile.setScoreboard(null);
    }
}
