package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.block.SpecialBlockData;
import net.syphlex.skyblock.handler.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.handler.island.block.OreGeneratorBlockData;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyblockSettingsFile extends SimpleConfig {

    public ArrayList<IslandOreGenerator> oreGeneratorsSection = new ArrayList<>();
    public ArrayList<SpecialBlockData> specialBlocksSection = new ArrayList<>();

    public SkyblockSettingsFile() {
        super("/settings.yml", false);
    }

    public void read(){

        config.options().copyDefaults(true);

        /*
        SPECIAL BLOCKS SECTION
         */

        config.addDefault("special-blocks", Arrays.asList(
                "DIAMOND_BLOCK:10000:Diamond Blocks",
                "IRON_BLOCK:5000:Iron Blocks"));

        /*
        ORE GENERATOR SECTION
         */

        config.addDefault("generators.example.tier", 1);
        config.addDefault("generators.example.name", "&6Example Generator");
        config.addDefault("generators.example.blocks.1.material", "COBBLESTONE");
        config.addDefault("generators.example.blocks.1.chance", 80);
        config.addDefault("generators.example.blocks.2.material", "COAL_ORE");
        config.addDefault("generators.example.blocks.2.chance", 20);

        save();

        for (String specialBlocks : config.getStringList("special-blocks")) {
            SpecialBlockData blockData = Skyblock.get().getUpgradeHandler().getSpecialBlockDataFromString(specialBlocks);
            this.specialBlocksSection.add(blockData);
        }

        for (String section : config.getConfigurationSection("generators").getKeys(false)) {

            int tier = config.getInt("generators." + section + ".tier");
            String name = config.getString("generators." + section + ".name");

            IslandOreGenerator generator = new IslandOreGenerator(tier, name);

            for (String blockSec : config.getConfigurationSection("generators." + section + ".blocks").getKeys(false)) {
                String materialName = config.getString("generators." + section + ".blocks." + blockSec + ".material");
                double chance = config.getDouble("generators." + section + ".blocks." + blockSec + ".chance");
                generator.getBlocks().add(new OreGeneratorBlockData(Material.getMaterial(materialName), chance));
            }

            this.oreGeneratorsSection.add(generator);
        }
    }
}
