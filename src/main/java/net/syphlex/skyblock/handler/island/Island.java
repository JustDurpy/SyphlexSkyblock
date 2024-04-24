package net.syphlex.skyblock.handler.island;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.island.upgrade.IslandUpgrade;
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
    private final IslandUpgrade upgrades = new IslandUpgrade();

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

    public double getMinX(){
        return Math.min(corner1.getX(), corner2.getX());
    }

    public double getMinY(){
        return Math.min(corner1.getY(), corner2.getY());
    }

    public double getMinZ(){
        return Math.min(corner1.getZ(), corner2.getZ());
    }

    public double getMaxX(){
        return Math.max(corner1.getX(), corner2.getX());
    }

    public double getMaxY(){
        return Math.max(corner1.getY(), corner2.getY());
    }

    public double getMaxZ(){
        return Math.max(corner1.getZ(), corner2.getZ());
    }
}
