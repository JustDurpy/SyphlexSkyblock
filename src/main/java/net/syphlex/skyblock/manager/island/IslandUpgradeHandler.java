package net.syphlex.skyblock.manager.island;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import org.bukkit.Material;

import java.util.ArrayList;

@Getter
public class IslandUpgradeHandler {


    private ArrayList<IslandOreGenerator> oreGenerators;
    private ArrayList<SpecialBlockData> specialBlocks;

    public void onEnable(){
        this.oreGenerators = Skyblock.get().getSettingsFile().oreGeneratorsSection;
        this.specialBlocks = Skyblock.get().getSettingsFile().specialBlocksSection;
    }

    public void onDisable(){
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

    public IslandOreGenerator getOreGenerator(int tier){
        for (IslandOreGenerator generator : this.oreGenerators) {
            if (generator.getTier() == tier)
                return generator;
        }
        return null;
    }
}
