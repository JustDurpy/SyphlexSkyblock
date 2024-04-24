package net.syphlex.skyblock.handler.island.data;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.island.upgrade.IslandUpgradeData;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Island {

    private String identifier;
    private MemberProfile owner;
    private Position corner1, corner2, center, home;
    private List<MemberProfile> members = new ArrayList<>();
    private final IslandUpgradeData upgrades = new IslandUpgradeData();

    public Island(String identifier,
                  MemberProfile owner, Position corner1,
                  Position corner2, Position center,
                  List<MemberProfile> members){
        this.identifier = identifier;
        this.owner = owner;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
        this.members = members;
    }

    public Island(String identifier, MemberProfile owner){
        this.identifier = identifier;
        this.owner = owner;
    }

    public void teleport(Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                Location bukkit = home.getAsBukkit().add(0, 1, 0);
                player.teleport(bukkit);
                Skyblock.get().getIslandHandler().generateIslandBorder(player, Color.BLUE);
            }
        }.runTaskLater(Skyblock.get(), 2L);
    }

    public boolean isInside(Location location){

        final double minX = Math.min(corner1.getX(), corner2.getX());
        final double minY = Math.min(corner1.getY(), corner2.getY());
        final double minZ = Math.min(corner1.getZ(), corner2.getZ());

        final double maxX = Math.max(corner1.getX(), corner2.getX());
        final double maxY = Math.max(corner1.getY(), corner2.getY());
        final double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return location.getX() >= minX && location.getY() >= minY && location.getZ() >= minZ
                && location.getX() <= maxX && location.getY() <= maxY && location.getZ() <= maxZ;
    }

    public int getMinX(){
        return Math.min(corner1.getBlockX(), corner2.getBlockX());
    }

    public int getMinY(){
        return Math.min(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMinZ(){
        return Math.min(corner1.getBlockZ(), corner2.getBlockZ());
    }

    public int getMaxX(){
        return Math.max(corner1.getBlockX(), corner2.getBlockX());
    }

    public int getMaxY(){
        return Math.max(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMaxZ(){
        return Math.max(corner1.getBlockZ(), corner2.getBlockZ());
    }
}
