package net.syphlex.skyblock.manager.island;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.IslandFile;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.data.IslandGrid;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.data.Position;
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
import java.util.concurrent.CompletableFuture;

@Getter
public class IslandHandler {

    //public final static double MINIMUM_Y_LIMIT = -64.0;
    public final static double BUILD_HEIGHT = 256.0;

    private IslandFile islandFile;
    private IslandGrid grid;

    public void onEnable(){

        final long started = System.currentTimeMillis();

        Skyblock.info("Reading all island data from flat file database...");

        this.islandFile = new IslandFile();
        ArrayList<Island> islandList = this.islandFile.read();

        this.grid = new IslandGrid(islandList.size() + 1);

        for (Island island : islandList)
            this.grid.insert(island, island.getId());

        Skyblock.info("Successfully inserted and loaded all islands into virtual grid in "
                + (System.currentTimeMillis() - started)
                + "ms . (size=" + this.grid.length() + "x" + this.grid.length() + ")"
                + " (islands=" + islandList.size() + ")");
    }

    public void onDisable() {

            for (File f : this.islandFile.getFile().listFiles())
                f.delete();

            for (int r = 0; r < this.grid.length(); r++) {
                for (int c = 0; c < this.grid.width(r); c++) {
                    if (this.grid.get(r, c) != null)
                        this.grid.get(r, c).unloadHolograms();
                    this.islandFile.write(this.grid.get(r, c));
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

            for (Entity e : Skyblock.get().getIslandWorld().getNearbyEntities(
                    island.getCenter().getAsBukkit(Skyblock.get().getIslandWorld()),
                    island.getUpgrades().getIslandSize().get(),
                    256,
                    island.getUpgrades().getIslandSize().get())) {

                if (!(e instanceof Player)) {
                    e.remove();
                    continue;
                }

                Player p = (Player)e;
                p.teleport(Skyblock.get().getMainSpawn());
            }
        });
        return completableFuture;
    }

    public CompletableFuture<Void> destroyIsland(Island island){
        island.unloadHolograms();
        return CompletableFuture.runAsync(() -> {
            //getRidOfPlayers(island).join();
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    getRidOfPlayers(island),
                    deleteIslandBlocks(island, Skyblock.get().getIslandWorld())
            );
            for (CompletableFuture<Void> future : completableFutures)
                future.join();
        });
    }

    public void degenerateIsland(Profile profile){

        long started = System.currentTimeMillis();

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        if (Skyblock.get().getCooldownHandler().getIslandDeleteCooldown().isUnderCooldown(profile.getPlayer().getUniqueId())) {
            Skyblock.get().getCooldownHandler().getIslandDeleteCooldown().sendUnderCooldown(profile);
            return;
        }

        degenerateIsland(profile.getIsland());
        profile.setIsland(null);

        profile.getPlayer().sendMessage(Messages.ISLAND_DELETE.get()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - started)));

        // start island deletion cooldown
        Skyblock.get().getCooldownHandler().getIslandDeleteCooldown().start(profile.getPlayer().getUniqueId());
    }

    public void degenerateIsland(Island island){

        destroyIsland(island);

        int[] id = island.getId();
        this.grid.set(id[0], id[1], null);
        //this.grid.getGrid()[id[0]][id[1]] = null;
    }

    public void generateIsland(Profile profile, String schematic) {

        Player player = profile.getPlayer();

        long started = System.currentTimeMillis();

        if (profile.getIsland() != null) {
            Messages.ALREADY_HAS_ISLAND.send(profile);
            return;
        }

        // if player is still under a cooldown, dont continue.
        if (Skyblock.get().getCooldownHandler().getIslandCreateCooldown().isUnderCooldown(player.getUniqueId())) {
            Skyblock.get().getCooldownHandler().getIslandCreateCooldown().sendUnderCooldown(profile);
            return;
        }

        int[] nextSpot = this.grid.getNextSpot();

        Position center = new Position(Skyblock.get().getIslandWorld(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5,
                ConfigEnum.DEFAULT_STARTING_Y_POSITION.getAsDouble(),
                ConfigEnum.ISLAND_DISTANCE_APART.getAsDouble() * nextSpot[0] + 0.5);
        Position corner1 = center.clone().add(
                -ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble() / 2.0d,
                BUILD_HEIGHT - ConfigEnum.DEFAULT_STARTING_Y_POSITION.getAsDouble(),
                -ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble() / 2.0d);
        Position corner2 = center.clone().add(
                ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble() / 2.0d,
                -ConfigEnum.DEFAULT_STARTING_Y_POSITION.getAsDouble() + ConfigEnum.MINIMUM_Y_LIMIT.getAsDouble(),
                ConfigEnum.STARTING_ISLAND_SIZE.getAsDouble() / 2.0d);

        Island island = new Island(nextSpot, IslandUtil.idToString(nextSpot),
                new MemberProfile(player.getUniqueId(), IslandRole.LEADER),
                corner1, corner2, center);

        island.setHome(island.getCenter().clone());

        Skyblock.get().getSchematicHandler().pasteSchematic(
                island, Skyblock.get().getSchematicHandler().getSchematic(schematic));

        this.grid.insert(island, nextSpot);

        profile.getMemberProfile().setRole(IslandRole.LEADER);
        profile.setIsland(island);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {
            island.teleport(player);
        }, 20L);

        profile.getPlayer().sendMessage(Messages.ISLAND_CREATE.get()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - started)));

        // start island creation cooldown timer
        Skyblock.get().getCooldownHandler().getIslandCreateCooldown().start(player.getUniqueId());
    }

    public void degenerateIslandBorder(Player player){

        if (player.getWorldBorder() == null)
            return;

        player.getWorldBorder().reset();
        player.setWorldBorder(null);
    }
}
