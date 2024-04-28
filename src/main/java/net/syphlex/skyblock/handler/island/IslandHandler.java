package net.syphlex.skyblock.handler.island;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.IslandFile;
import net.syphlex.skyblock.handler.island.block.SpecialBlockData;
import net.syphlex.skyblock.handler.island.data.Island;
import net.syphlex.skyblock.handler.island.block.IslandBlockData;
import net.syphlex.skyblock.handler.island.data.IslandGrid;
import net.syphlex.skyblock.handler.island.member.IslandRole;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.profile.IslandProfile;
import net.syphlex.skyblock.util.Position;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Getter
public class IslandHandler {

    public final static double MINIMUM_Y_LIMIT = -64.0;
    public final static double BUILD_HEIGHT = 256.0;

    private IslandFile islandFile;
    private IslandGrid grid;

    public void onEnable(){
        Skyblock.get().getThreadHandler().fire(() -> {
            this.islandFile = new IslandFile();
            ArrayList<Island> islandList = this.islandFile.read();

            this.grid = new IslandGrid(islandList.size() + 1);

            for (Island island : islandList)
                this.grid.insert(island);
        });
    }

    public void onDisable() {
        Skyblock.get().getThreadHandler().fire(() -> {
            for (File f : Objects.requireNonNull(this.islandFile.getFile().listFiles()))
                f.delete();

            for (int r = 0; r < this.grid.getGrid().length; r++) {
                for (int c = 0; c < this.grid.getGrid()[r].length; c++) {
                    this.islandFile.write(this.getGrid().getGrid()[r][c]);
                }
            }
        });
    }

    private CompletableFuture<Void> deleteIslandBlocks(Island island, World world){
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(Skyblock.get(), () -> {
                deleteIslandBlocks(island, world, world.getMaxHeight(), completableFuture, 0);
            });
        }
        return completableFuture;
    }

    private void deleteIslandBlocks(Island island, World world, int y, CompletableFuture<Void> completableFuture, int delay){

        for (int x = island.getMinX(); x <= island.getMaxX(); x++) {
            for (int z = island.getMinZ(); z <= island.getMaxZ(); z++) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getType() != Material.AIR) {
                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Material.AIR, false);
                }
            }
        }

        if (y <= world.getMinHeight()) {
            completableFuture.complete(null);
        } else {
            if (delay < 1) {
                deleteIslandBlocks(island, world, y - 1, completableFuture, delay);
            } else {
                Bukkit.getScheduler().runTaskLater(Skyblock.get(), () -> {
                    deleteIslandBlocks(island, world, y - 1, completableFuture, delay);
                }, delay);
            }
        }
    }

    private CompletableFuture<Void> getRidOfPlayers(Island island){
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(Skyblock.get(), () -> {

            for (Entity e : Skyblock.get().getIslandWorld().getNearbyEntities(
                    island.getCenter().getAsBukkit(Skyblock.get().getIslandWorld()),
                    island.getUpgrades().getSize(),
                    256,
                    island.getUpgrades().getSize())) {

                if (!(e instanceof Player))
                    continue;

                Player p = (Player)e;
                p.teleport(Skyblock.get().getMainSpawn());
            }
        });
        return completableFuture;
    }

    public CompletableFuture<Void> destroyIsland(Island island){
        return CompletableFuture.runAsync(() -> {
            getRidOfPlayers(island).join();
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    deleteIslandBlocks(island, Skyblock.get().getIslandWorld())
            );
            for (CompletableFuture<Void> future : completableFutures)
                future.join();
        });
    }

    public void degenerateIsland(IslandProfile profile){

        long started = System.currentTimeMillis();

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        degenerateIsland(profile.getIsland());
        profile.setIsland(null);

        profile.getPlayer().sendMessage(Messages.ISLAND_DELETE.get()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - started)));
    }

    public void degenerateIsland(Island island){

        destroyIsland(island);

        int[] id = getId(island.getIdentifier());
        this.grid.getGrid()[id[0]][id[1]] = null;
    }

    public void generateIsland(IslandProfile profile){

        Player player = profile.getPlayer();

        long started = System.currentTimeMillis();

        if (profile.getIsland() != null) {
            Messages.ALREADY_HAS_ISLAND.send(profile);
            return;
        }

        int[] nextSpot = this.grid.getNextSpot();

        Position center = new Position(Skyblock.get().getIslandWorld(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5,
                ConfigEnum.DEFAULT_Y_POSITION.getAsDouble(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5);
        Position corner1 = center.clone().add(
                -ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d,
                BUILD_HEIGHT - ConfigEnum.DEFAULT_Y_POSITION.getAsDouble(),
                -ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d);
        Position corner2 = center.clone().add(
                ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d,
                -ConfigEnum.DEFAULT_Y_POSITION.getAsDouble() + MINIMUM_Y_LIMIT,
                ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d);

        Island island = new Island(idToString(nextSpot),
                new MemberProfile(player.getUniqueId(), IslandRole.LEADER),
                corner1, corner2, center);

        island.setHome(island.getCenter().clone().add(0, 1, 0));

        //island.getCorner1().setBlock(Material.GLOWSTONE);
        //island.getCorner2().setBlock(Material.GLOWSTONE);
        island.getCenter().setBlock(Material.DIAMOND_BLOCK);

        this.grid.insert(island, nextSpot);

        profile.setIsland(island);

        island.teleport(player);

        profile.getPlayer().sendMessage(Messages.ISLAND_CREATE.get()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - started)));
    }

    public void generateIslandBorder(Island island, Player player, Color color) {

        WorldBorder worldBorder = Bukkit.getServer().createWorldBorder();
        worldBorder.setCenter(island.getCenter().getX(), island.getCenter().getZ());
        worldBorder.setSize(island.getUpgrades().getSize());

        worldBorder.setDamageAmount(0);
        worldBorder.setDamageBuffer(0);

        if (color == Color.RED) {
            worldBorder.setSize(island.getUpgrades().getSize() - 0.1D, 20000000L);
        } else if (color == Color.GREEN) {
            worldBorder.setSize( island.getUpgrades().getSize() + 0.1D, 20000000L);
        }

        player.setWorldBorder(worldBorder);
    }

    public void degenerateIslandBorder(Player player){

        if (player.getWorldBorder() == null)
            return;

        player.getWorldBorder().reset();
        player.setWorldBorder(null);
    }

    public Island getIslandAtLocation(Location location){
        for (int r = 0; r < this.grid.getGrid().length; r++) {
            for (int c = 0; c < this.grid.getGrid()[r].length; c++) {
                Island island = this.grid.getGrid()[r][c];
                if (island != null && island.isInside(location)) {
                    return island;
                }
            }
        }
        return null;
    }

    public Island getIslandAtXZ(double x, double z){
        for (int r = 0; r < this.grid.getGrid().length; r++) {
            for (int c = 0; c < this.grid.getGrid()[r].length; c++) {
                Island island = this.grid.get(r, c);
                if (island != null) {
                    if (island.isInside(x, z))
                        return island;
                }
            }
        }
        return null;
    }

    public String idToString(int[] id){
        return id[0] + ";" + id[1];
    }

    public int[] getId(String identifier) {

        if (identifier == null
                || identifier.length() <= 0
                || identifier.equalsIgnoreCase("null"))
            return new int[]{-1, -1};

        String[] split = identifier.split(";");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }

    public IslandBlockData getIslandBlockDataFromPos(Island island, Location location) {
        for (IslandBlockData blockData : island.getStoredBlocks()) {
            if (blockData.getPosition().getBlockX() == location.getBlockX()
                    && blockData.getPosition().getBlockY() == location.getBlockY()
                    && blockData.getPosition().getBlockZ() == location.getBlockZ())
                return blockData;
        }
        return null;
    }

    public String printGrid(){
        StringBuilder gridPrint = new StringBuilder();

        for (int r = 0; r < this.grid.getGrid().length; r++) {
            for (int c = 0; c < this.grid.getGrid()[r].length; c++) {
                gridPrint.append((this.grid.getGrid()[r][c] == null ? "o " : "x "));
            }
            gridPrint.append("\n");
        }

        return gridPrint.toString();
    }
}
