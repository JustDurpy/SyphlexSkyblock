package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.handler.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.handler.island.upgrade.oregenerator.OreGeneratorBlockData;
import net.syphlex.skyblock.util.SimpleConfig;
import org.bukkit.Material;

import java.util.ArrayList;

@SuppressWarnings("all")
public class OreGeneratorFile extends SimpleConfig {

    public OreGeneratorFile() {
        super("/ore-generator.yml", false);
    }

    public void write(){
    }

    public ArrayList<IslandOreGenerator> read(){

        config.options().copyDefaults(true);
        config.addDefault("generators.example.tier", 1);
        config.addDefault("generators.example.name", "&6Example Generator");
        config.addDefault("generators.example.blocks.1.material", "COBBLESTONE");
        config.addDefault("generators.example.blocks.1.chance", 80);
        config.addDefault("generators.example.blocks.2.material", "COAL_ORE");
        config.addDefault("generators.example.blocks.2.chance", 20);

        save();

        ArrayList<IslandOreGenerator> oreGenerators = new ArrayList<>();

        for (String section : config.getConfigurationSection("generators").getKeys(false)) {

            int tier = config.getInt("generators." + section + ".tier");
            String name = config.getString("generators." + section + ".name");

            IslandOreGenerator generator = new IslandOreGenerator(tier, name);

            for (String blockSec : config.getConfigurationSection("generators." + section + ".blocks").getKeys(false)) {
                String materialName = config.getString("generators." + section + ".blocks." + blockSec + ".material");
                double chance = config.getDouble("generators." + section + ".blocks." + blockSec + ".chance");
                generator.getBlocks().add(new OreGeneratorBlockData(Material.getMaterial(materialName), chance));
            }

            oreGenerators.add(generator);
        }

        return oreGenerators;
    }
}
