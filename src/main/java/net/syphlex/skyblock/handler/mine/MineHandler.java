package net.syphlex.skyblock.handler.mine;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.MinesFile;
import net.syphlex.skyblock.handler.mine.data.Mine;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MineHandler {

    private ArrayList<Mine> mines;

    private MinesFile minesFile;


    public void onEnable() {
        this.minesFile = new MinesFile();
        this.mines = this.minesFile.read();
    }

    public void onDisable() {
    }

    public void regenerateMine(Mine mine) {
        handleRefillMine(mine);
    }

    private CompletableFuture<Void> handleRefillMine(Mine mine) {
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    // todo get rid of players inside the mine so they dont suffocate
                    refillMine(mine, mine.getCorner1().getWorld())
            );
            for (CompletableFuture<Void> future : completableFutures)
                future.join();
        });
    }

    private CompletableFuture<Void> refillMine(Mine mine, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(Skyblock.get(), () -> {
                refillMine(mine, world, mine.getMaxY(), completableFuture, 0);
            });
        }
        return completableFuture;
    }

    private void refillMine(Mine mine, World world, int y, CompletableFuture<Void> completableFuture, int delay) {

        for (int x = mine.getMinX(); x <= mine.getMaxX(); x++) {
            for (int z = mine.getMinZ(); z <= mine.getMaxZ(); z++) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getState() instanceof InventoryHolder)
                    ((InventoryHolder) block.getState()).getInventory().clear();

                Material material = mine.generateBlock().getMaterial();
                if (material == null) material = Material.STONE;
                block.setType(material);
            }
        }

        if (y < mine.getMinY()) {
            completableFuture.complete(null);
        } else {
            if (delay < 1) {
                refillMine(mine, world, y - 1, completableFuture, delay);
            } else {
                Bukkit.getScheduler().runTaskLater(Skyblock.get(), () -> {
                    refillMine(mine, world, y - 1, completableFuture, delay);
                }, delay);
            }
        }
    }
}
