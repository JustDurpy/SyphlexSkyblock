package net.syphlex.skyblock.manager.island;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.UpgradesFile;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.upgrade.UpgradeObject;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import org.bukkit.Material;

import java.util.ArrayList;

@Getter
public class IslandUpgradeHandler {

    private final ArrayList<IslandOreGenerator> oreGenerators = new ArrayList<>();
    private final ArrayList<SpecialBlockData> specialBlocks = new ArrayList<>();
    private UpgradesFile upgradesFile;

    public void onEnable(){
        this.upgradesFile = new UpgradesFile();
        this.upgradesFile.read();
    }

    public boolean isSpecialBlock(Material material){
        for (SpecialBlockData blockData : this.specialBlocks) {
            if (blockData.getMaterial() == material)
                return true;
        }
        return false;
    }

    public SpecialBlockData getSpecialBlockDataFromString(String s){
        String[] split = s.split(":");

        /*
        split[0] is the material of the special block
        split[1] is the worth of the special block
        split[2] is the display name of the special block
         */

        return new SpecialBlockData(
                Material.getMaterial(split[0]),
                Float.parseFloat(split[1]),
                split[2]);
    }

    public SpecialBlockData getSpecialBlockDataFromMaterial(Material material){
        for (SpecialBlockData data : this.specialBlocks) {
            if (data.getMaterial() == material) {
                return data;
            }
        }
        return null;
    }

    public IslandOreGenerator getOreGenerator(int id){
        for (IslandOreGenerator generator : this.oreGenerators) {
            if (generator.getId() == id)
                return generator;
        }
        return this.oreGenerators.get(0);
    }

    public IslandOreGenerator getOreGenerator(String name){
        for (IslandOreGenerator generator : this.oreGenerators) {
            if (generator.getName().equalsIgnoreCase(name))
                return generator;
        }
        return this.oreGenerators.get(0);
    }
}
