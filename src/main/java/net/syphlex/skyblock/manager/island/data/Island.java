package net.syphlex.skyblock.manager.island.data;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.upgrade.IslandUpgradeData;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.MathHelper;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import org.bukkit.Bukkit;
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
    private Position home, warp;
    private List<MemberProfile> members = new ArrayList<>();
    private List<MemberProfile> bannedPlayers = new ArrayList<>();
    private ArrayList<IslandBlockData> storedBlocks = new ArrayList<>();
    private final IslandUpgradeData upgrades = new IslandUpgradeData(
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getIslandSizeUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getSpawnRateUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getSpawnAmountUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getHarvestUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getTeamSizeUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getGeneratorUpgrade().clone(),
            Skyblock.get().getUpgradeHandler().getUpgradesFile().getVoidChestUpgrade().clone()
    );

    /*
    TODO make island border color in here under "settings"
     */

    private final ImmutableList<IslandRole> roles = ImmutableList.of(
            IslandRole.VISITOR,
            IslandRole.MEMBER,
            IslandRole.MODERATOR,
            IslandRole.LEADER);

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

    /**
     * Initializes island object
     * @param id - integer island identifier id[0] = grid row, id[1] = grid column
     * @param identifier - string identifier
     * @param leader - leader of the island
     * @param corner1 - corner 1 of the island
     * @param corner2 - corner 2 of the island
     * @param center - center of the island
     */
    public Island(int[] id, String identifier, MemberProfile leader,
                  Position corner1, Position corner2, Position center){
        this.id = id;
        this.identifier = identifier;
        this.leader = leader;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.center = center;
    }

    /**
     *
     * @param o - object to broadcast to all island members
     */
    public void broadcast(Object o){
        for (MemberProfile members : this.members) {
            final Player p = Bukkit.getPlayer(members.getUuid());
            if (p == null) continue;
            p.sendMessage(StringUtil.CC(o.toString()));
        }
    }

    /**
     *
     * @param p - refreshes the island border for 'player'
     * @param color - color of the border
     */
    public void refreshBorder(Player p, Color color){
        Skyblock.get().getIslandHandler().degenerateIslandBorder(p);
        Skyblock.get().getIslandHandler().generateIslandBorder(this, p, color);
    }

    /**
     * Refreshes the border for all players on the island.
     */
    public void refreshBorder(){
        for (Player p : Bukkit.getOnlinePlayers()) {

            if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld())
                    || !isInside(p.getLocation()))
                continue;

            refreshBorder(p, Color.BLUE);
        }
    }

    public void addMember(Profile profile){
        this.members.add(new MemberProfile(profile.getPlayer().getUniqueId()));
        profile.getMemberProfile().setRole(IslandRole.MEMBER);
    }

    public void removeMember(UUID uuid){
        MemberProfile member = getMember(uuid);
        this.members.remove(member);
    }

    public void unloadHolograms(){
        if (this.storedBlocks.size() == 0) return;
        for (IslandBlockData blockData : this.storedBlocks)
            blockData.getHologram().delete();
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

    /**
     * Teleports player to the island
     * @param player - player to teleport
     */
    public void teleport(Player player) {
        Location bukkit = home.getAsBukkit(Skyblock.get().getIslandWorld());

        while (bukkit.getBlock().getType() != Material.AIR)
            bukkit.add(0, 0.5, 0);

        player.teleport(bukkit);
        Skyblock.get().getIslandHandler().generateIslandBorder(this, player, Color.BLUE);
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

    public boolean isStoredBlock(Location location) {
        return getStoredBlock(location) != null;
    }

    public boolean isMember(UUID uuid){
        return getMember(uuid) != null;
    }

    public boolean isApartOfIsland(UUID uuid){
        return isMember(uuid) || this.leader.getUuid().equals(uuid);
    }

    /**
     *
     * @param location -
     * @return
     */
    public IslandBlockData getStoredBlock(Location location) {
        for (IslandBlockData blockData : this.storedBlocks) {
            if (blockData.getPosition().getBlockX() == location.getBlockX()
                    && blockData.getPosition().getBlockY() == location.getBlockY()
                    && blockData.getPosition().getBlockZ() == location.getBlockZ())
                return blockData;
        }
        return null;
    }

    /**
     *
     * @param location - base location of object
     * @param x - contract/expand the island border by 'x'
     * @param z - contract/expand the island border by 'z'
     * @return true if location is inside the modified island boundary
     */
    public boolean isInside(Location location, double x, double z){

        final double maxX = getMaxX() + x;
        final double maxZ = getMaxZ() + z;
        final double minX = getMinX() - x;
        final double minZ = getMinZ() - z;

        return location.getX() >= minX && location.getZ() >= minZ && location.getX() <= maxX && location.getZ() <= maxZ;
    }

    /**
     *
     * @param location - checks if the given location is inside the island border
     * @return return true if the location is found inside the island border
     */
    public boolean isInside(Location location){
        return location.getX() >= getMinX() && location.getY() >= getMinY() && location.getZ() >= getMinZ()
                && location.getX() <= getMaxX() && location.getY() <= getMaxY() && location.getZ() <= getMaxZ();
    }

    /**
     *
     * @param x - check if x CoOrdinate is inside the islands x CoOrdinate
     * @param z - check if z CoOrdinate is inside the islands z CoOrdinate
     * @return true if the x & z CoOrdinates are found inside the island border
     */
    public boolean isInside(double x, double z){
        return x >= getMinX() && x <= getMaxX() && z >= getMinZ() && z <= getMaxZ();
    }

    public IslandRole getLeaderRole(){
        return this.roles.get(3);
    }

    public IslandRole getModeratorRole(){
        return this.roles.get(2);
    }

    public IslandRole getMemberRole(){
        return this.roles.get(1);
    }

    public IslandRole getVisitorRole(){
        return this.roles.get(0);
    }

    public int getMinX(){
        return MathHelper.floor_double(Math.min(corner1.getBlockX(), corner2.getBlockX()) - (this.upgrades.getIslandSize().get()) - ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble());
    }

    public int getMinY(){
        return Math.min(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMinZ(){
        return MathHelper.floor_double(Math.min(corner1.getBlockZ(), corner2.getBlockZ()) - (this.upgrades.getIslandSize().get() - ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble()));
    }

    public int getMaxX(){
        return MathHelper.floor_double(Math.max(corner1.getBlockX(), corner2.getBlockX()) + (this.upgrades.getIslandSize().get() - ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble()));
    }

    public int getMaxY(){
        return Math.max(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMaxZ(){
        return MathHelper.floor_double(Math.max(corner1.getBlockZ(), corner2.getBlockZ()) + (this.upgrades.getIslandSize().get() - ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble()));
    }
}
