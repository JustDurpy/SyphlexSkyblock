package net.syphlex.skyblock.manager.island.data;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.upgrade.IslandUpgradeData;
import net.syphlex.skyblock.manager.profile.IslandProfile;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Island {

    private String identifier;
    private int[] id;
    private MemberProfile leader;
    private final Position corner1, corner2, center;
    private Position home;
    private List<MemberProfile> members = new ArrayList<>();
    private ArrayList<IslandBlockData> storedBlocks = new ArrayList<>();
    private final IslandUpgradeData upgrades = new IslandUpgradeData();

    public Island(int[] id, String identifier,
                  MemberProfile leader, Position corner1,
                  Position corner2, Position center,
                  ArrayList<MemberProfile> members,
                  ArrayList<IslandBlockData> storedBlocks){
        this.id = id;
        this.identifier = identifier;
        this.leader = leader;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
        this.members = members;
        this.storedBlocks = storedBlocks;
    }

    public Island(int[] id, String identifier, MemberProfile leader,
                  Position corner1, Position corner2, Position center){
        this.id = id;
        this.identifier = identifier;
        this.leader = leader;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
    }

    public void addMember(IslandProfile profile){
        this.members.add(new MemberProfile(profile.getPlayer().getUniqueId()));
        profile.getMemberProfile().setRole(IslandRole.DEFAULT);
    }

    public void removeMember(UUID uuid){
        MemberProfile member = getMember(uuid);
        this.members.remove(member);
    }

    public boolean isMember(UUID uuid){
        return getMember(uuid) != null;
    }

    public boolean isApartOfIsland(UUID uuid){
        return isMember(uuid) || this.leader.getUuid().equals(uuid);
    }

    public MemberProfile getMember(UUID uuid){
        for (MemberProfile profile : this.members) {
            if (profile.getUuid().equals(uuid))
                return profile;
        }
        return null;
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
        Location bukkit = home.getAsBukkit(Skyblock.get().getIslandWorld());

        while (bukkit.getBlock().getType() != Material.AIR)
            bukkit.add(0, 0.5, 0);

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
