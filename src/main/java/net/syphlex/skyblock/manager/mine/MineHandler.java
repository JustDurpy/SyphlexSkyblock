package net.syphlex.skyblock.manager.mine;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.MinesFile;
import net.syphlex.skyblock.manager.mine.data.Mine;
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

    @Getter
    private ArrayList<Mine> mines;

    private MinesFile minesFile;


    public void onEnable() {
        Skyblock.get().getThreadHandler().fire(() -> {
            this.minesFile = new MinesFile();
            this.mines = this.minesFile.read();
        });
    }

    public void onDisable() {
        Skyblock.get().getThreadHandler().fire(() -> {
            this.minesFile.config.set("mines", null);
            for (Mine mine : this.mines)
                this.minesFile.write(mine);
            this.minesFile.save();
        });
    }

    public Mine getMine(String name){
        for (Mine mine : this.mines)
            if (mine.getMineName().equalsIgnoreCase(name))
                return mine;
        return null;
    }

    public void regenerateMine(Mine mine) {
        handleRefillMine(mine);
    }

    private CompletableFuture<Void> handleRefillMine(Mine mine) {
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    // todo get rid of players inside the mine so they dont suffocate
                    refillMine(mine, mine.getSpawn().getWorld())
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
