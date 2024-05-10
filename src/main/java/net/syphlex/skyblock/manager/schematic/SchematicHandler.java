package net.syphlex.skyblock.manager.schematic;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.SchematicsFile;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.schematic.paster.FastAsyncWorldEdit;
import net.syphlex.skyblock.manager.schematic.paster.SchematicPaster;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class SchematicHandler {

    private final SchematicsFile schematicsFile = new SchematicsFile();
    public SchematicPaster paster;

    public void onEnable(){
        this.paster = new FastAsyncWorldEdit();
        this.schematicsFile.read();
    }

    public void onDisable(){
        this.schematicsFile.write();
        this.paster.clearCache();
    }

    public File getSchematic(String name){
        for (Map.Entry<String, File> entry : this.schematicsFile.getSchematicFiles().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }

    public CompletableFuture<Void> pasteSchematic(Island island, File file){
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        completableFutureList.add(this.pasteSchematicDo(island, file));
        return CompletableFuture.runAsync(() -> completableFutureList.forEach(CompletableFuture::join));
    }

    private CompletableFuture<Void> pasteSchematicDo(Island island, File schematicFile){
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Location location = island.getCenter().getAsBukkit();
        //location.add(0, 100, 0);
        Bukkit.getScheduler().runTask(Skyblock.get(), () -> {
            if (schematicFile == null) {
                location.getBlock().setType(Material.BEDROCK);
                //Skyblock.get().getLogger().warning("Could not find schematic " + schematic.schematicID);
            } else {
                //if (fawe) {
                    Bukkit.getScheduler().runTaskAsynchronously(Skyblock.get(),
                            () -> this.paster.paste(schematicFile, location, true, completableFuture));
                //} else {
               //     schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture);
                //}
            }
        });
        return completableFuture;
    }
}
