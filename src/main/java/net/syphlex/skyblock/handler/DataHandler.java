package net.syphlex.skyblock.handler;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.ProfileFile;
import net.syphlex.skyblock.profile.IslandProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {
    private final Map<UUID, IslandProfile> profileMap = new ConcurrentHashMap<>();
    private ProfileFile profileFile;

    public void onEnable(){

        this.profileFile = new ProfileFile();

        Bukkit.getOnlinePlayers().forEach(this::join);
    }

    public void onDisable(){
        this.profileMap.values().forEach(data -> quit(data.getPlayer()));
    }

    public void join(Player player){
        IslandProfile profile = new IslandProfile(player);
        this.profileFile.read(profile);

        if (profile.getIsland() != null) {

        }

        this.profileMap.put(player.getUniqueId(), profile);
    }

    public void quit(Player player){
        IslandProfile profile = this.profileMap.remove(player.getUniqueId());
        Skyblock.get().getScoreboardHandler().delScoreboard(profile);
        this.profileFile.write(profile);
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
