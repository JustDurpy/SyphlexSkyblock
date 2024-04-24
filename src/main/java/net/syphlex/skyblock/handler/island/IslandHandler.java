package net.syphlex.skyblock.handler.island;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.IslandFile;
import net.syphlex.skyblock.handler.island.data.Island;
import net.syphlex.skyblock.handler.island.data.IslandGrid;
import net.syphlex.skyblock.handler.island.member.IslandRole;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.profile.IslandProfile;
import net.syphlex.skyblock.util.Position;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
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

    public final static double MINIMUM_Y_LIMIT = -128.0;
    public final static double BUILD_HEIGHT = 256.0;

    private IslandFile islandFile;
    private IslandGrid grid;

    public void onEnable(){
        this.islandFile = new IslandFile();
        ArrayList<Island> islandList = this.islandFile.read();

        this.grid = new IslandGrid(islandList.size() / 2);
        for (Island island : islandList)
            this.grid.insert(island);
    }

    public void onDisable() {

        for (File f : Objects.requireNonNull(this.islandFile.getFile().listFiles())) {
            f.delete();
        }

        for (int r = 0; r < this.grid.getGrid().length; r++) {
            for (int c = 0; c < this.grid.getGrid()[r].length; c++) {
                this.islandFile.write(this.getGrid().getGrid()[r][c]);
            }
        }
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
            for (MemberProfile profiles : island.getMembers()) {

                Player p = Bukkit.getPlayer(profiles.getUuid());

                if (p == null) continue;

                p.setHealth(0); // todo change this to teleport back to spawn
            }

            Player p = Bukkit.getPlayer(island.getOwner().getUuid());
            if (p != null)
                p.setHealth(0.0); // todo
        });
        return completableFuture;
    }

    public CompletableFuture<Void> destroyIsland(Island island){
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    getRidOfPlayers(island),
                    deleteIslandBlocks(island, Skyblock.get().getIslandWorld())
            );
            for (CompletableFuture<Void> future : completableFutures)
                future.join();
        });
    }

    public void degenerateIsland(IslandProfile profile){

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        Messages.ISLAND_DELETE.send(profile);

        degenerateIsland(profile.getIsland());
        profile.setIsland(null);
    }

    public void degenerateIsland(Island island){

        destroyIsland(island);

        int[] id = getId(island.getIdentifier());
        this.grid.getGrid()[id[0]][id[1]] = null;
    }

    public void generateIsland(IslandProfile profile){

        Player player = profile.getPlayer();

        if (profile.getIsland() != null) {
            Messages.ALREADY_HAS_ISLAND.send(profile);
            return;
        }

        Messages.ISLAND_CREATE.send(profile);

        int[] nextSpot = this.grid.getNextSpot();

        Island island = new Island(idToString(nextSpot),
                new MemberProfile(player.getUniqueId(), IslandRole.LEADER));

        island.setCenter(new Position(Skyblock.get().getIslandWorld(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5,
                ConfigEnum.DEFAULT_Y_POSITION.getAsDouble(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5));
        island.setCorner1(island.getCenter().clone().add(
                -ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d,
                BUILD_HEIGHT - ConfigEnum.DEFAULT_Y_POSITION.getAsDouble(),
                -ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d));
        island.setCorner2(island.getCenter().clone().add(
                ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d,
                -(Math.abs(MINIMUM_Y_LIMIT - ConfigEnum.DEFAULT_Y_POSITION.getAsDouble()) - ConfigEnum.DEFAULT_Y_POSITION.getAsDouble()),
                ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() / 2.0d));

        island.setHome(island.getCenter().clone());

        island.getCorner1().setBlock(Material.GLOWSTONE);
        island.getCorner2().setBlock(Material.GLOWSTONE);
        island.getCenter().setBlock(Material.DIAMOND_BLOCK);

        this.grid.insert(island, nextSpot);

        profile.setIsland(island);

        island.teleport(player);
    }

    public void generateIslandBorder(Player player, Color color) {

        WorldBorder worldBorder = Bukkit.getServer().createWorldBorder();
        worldBorder.setCenter(player.getLocation().getX(), player.getLocation().getZ());
        worldBorder.setSize(ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble());

        worldBorder.setDamageAmount(0);
        worldBorder.setDamageBuffer(0);

        if (color == Color.RED) {
            worldBorder.setSize(ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() - 0.1D, 20000000L);
        } else if (color == Color.GREEN) {
            worldBorder.setSize( ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble() + 0.1D, 20000000L);
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
                Island island = this.grid.get(r, c);
                if (island != null) {
                    if (island.isInside(location))
                        return island;
                }
            }
        }
        return null;
    }

    public String idToString(int[] id){
        return id[0] + ";" + id[1];
    }

    public int[] getId(String identifier){

        if (identifier == null
                || identifier.length() <= 0
                || identifier.equalsIgnoreCase("null"))
            return new int[]{-1, -1};

        String[] split = identifier.split(";");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
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
