package net.syphlex.skyblock.manager.profile;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.ProfileFile;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.util.IslandUtil;
import net.syphlex.skyblock.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {
    private final Map<UUID, IslandProfile> profileMap = new ConcurrentHashMap<>();
    private ProfileFile profileFile;

    public void onEnable(){
        this.profileFile = new ProfileFile();
        for (Player p : Bukkit.getOnlinePlayers())
            join(p);
    }

    public void onDisable(){
        for (IslandProfile profile : this.profileMap.values()) {
            quit(profile.getPlayer());
        }
    }

    public void join(Player player) {
        Skyblock.get().getThreadHandler().fire(() -> {
            IslandProfile profile = new IslandProfile(player);
            this.profileMap.put(player.getUniqueId(), profile);
            this.profileFile.read(get(player));
        });

        if (!WorldUtil.isWorld(player.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {
            Island island = IslandUtil.getIslandAtLocation(player.getLocation());

            if (island == null)
                return;

            Skyblock.get().getIslandHandler().generateIslandBorder(island, player, Color.BLUE);
        }, 1L);
    }

    public void quit(Player player){
        Skyblock.get().getThreadHandler().fire(() -> {
            IslandProfile profile = get(player);//this.profileMap.remove(player.getUniqueId());
            Skyblock.get().getScoreboardHandler().delScoreboard(profile);
            this.profileFile.write(profile);
            this.profileMap.remove(player.getUniqueId());
        });
    }

    public IslandProfile get(Player player){
        return this.profileMap.get(player.getUniqueId());
    }

    public IslandProfile get(UUID uuid){
        return this.profileMap.get(uuid);
    }

    public Map<UUID, IslandProfile> getMap(){
        return this.profileMap;
    }
}
