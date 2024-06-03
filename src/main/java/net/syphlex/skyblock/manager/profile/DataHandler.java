package net.syphlex.skyblock.manager.profile;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.ProfileFile;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {
    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();
    @Getter private final ProfileFile profileFile = new ProfileFile();

    public void onEnable(){
        for (Player p : Bukkit.getOnlinePlayers())
            join(p);
    }

    public void onDisable(){
        for (Profile profile : this.profileMap.values())
            quit(profile.getPlayer());
    }

    public void join(Player player) {
        //Skyblock.get().getThreadHandler().fire(() -> {
            Profile profile = new Profile(player);
            this.profileMap.put(player.getUniqueId(), profile);
            this.profileFile.read(profile);

            if (profile.getIsland() != null) {
                if (profile.isIslandLeader()) {
                    profile.getMemberProfile().setRole(IslandRole.LEADER);
                } else {
                    profile.getMemberProfile().setRole(profile.getIsland().getMember(player.getUniqueId()).getRole());
                }
            }
        //});

        if (!WorldUtil.isWorld(player.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {
            Island island = IslandUtil.getIslandAtLocation(player.getLocation());

            if (island == null)
                return;

            island.generateIslandBorder(player);
        }, 1L);
    }

    public void quit(Player player){
        Skyblock.get().getThreadHandler().fire(() -> {
            Profile profile = get(player);//this.profileMap.remove(player.getUniqueId());
            Skyblock.get().getScoreboardHandler().delScoreboard(profile);
            this.profileFile.write(profile);
            this.profileMap.remove(player.getUniqueId());
        });
    }

    public Profile get(Player player){
        return this.profileMap.get(player.getUniqueId());
    }

    public Profile get(UUID uuid){
        return this.profileMap.get(uuid);
    }

    public Map<UUID, Profile> getMap(){
        return this.profileMap;
    }
}
