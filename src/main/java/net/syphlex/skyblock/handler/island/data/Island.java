package net.syphlex.skyblock.handler.island.data;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.block.IslandBlockData;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.island.upgrade.IslandUpgradeData;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Island {

    private String identifier;
    private MemberProfile owner;
    private final Position corner1, corner2, center;
    private Position home;
    private List<MemberProfile> members = new ArrayList<>();
    private ArrayList<IslandBlockData> storedBlocks = new ArrayList<>();
    private final IslandUpgradeData upgrades = new IslandUpgradeData();

    public Island(String identifier,
                  MemberProfile owner, Position corner1,
                  Position corner2, Position center,
                  ArrayList<MemberProfile> members,
                  ArrayList<IslandBlockData> storedBlocks){
        this.identifier = identifier;
        this.owner = owner;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
        this.members = members;
        this.storedBlocks = storedBlocks;
    }

    public Island(String identifier, MemberProfile owner,
                  Position corner1, Position corner2, Position center){
        this.identifier = identifier;
        this.owner = owner;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
    }

    public double getWorth(){
        double sum = 0.0;
        for (IslandBlockData islandBlock : this.storedBlocks) {
            double blockWorth = islandBlock.getBlockData().getWorth();
            sum += islandBlock.getAmount() * blockWorth;
        }
        return sum;
    }

    public void setHome(Location location){
        this.home = new Position(
                Skyblock.get().getIslandWorld(),
                location.getX() + 0.5,
                location.getY(),
                location.getZ() + 0.5);
    }

    public void setHome(Position position){
        this.home = position;
    }

    public void teleport(Player player) {
        Location bukkit = home.getAsBukkit(Skyblock.get().getIslandWorld()).add(0, 1, 0);
        player.teleport(bukkit);
        Skyblock.get().getIslandHandler().generateIslandBorder(this, player, Color.BLUE);
    }

    public boolean isStoredBlock(Location location) {
        return getStoredBlock(location) != null;
    }

    public IslandBlockData getStoredBlock(Location location) {
        for (IslandBlockData blockData : this.storedBlocks) {
            if (blockData.getPosition().getBlockX() == location.getBlockX()
                    && blockData.getPosition().getBlockY() == location.getBlockY()
                    && blockData.getPosition().getBlockZ() == location.getBlockZ())
                return blockData;
        }
        return null;
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

    public boolean isInside(double x, double z){
        return x >= getMinX() && x <= getMaxX() && z >= getMinZ() && z <= getMaxZ();
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
