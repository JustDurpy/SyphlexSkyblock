package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.manager.island.block.OreGeneratorBlockData;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.manager.minion.MinionData;
import net.syphlex.skyblock.util.Pair;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyblockSettingsFile extends SimpleConfig {

    public ArrayList<IslandOreGenerator> oreGeneratorsSection = new ArrayList<>();
    public ArrayList<SpecialBlockData> specialBlocksSection = new ArrayList<>();
    public ArrayList<MinionData> minionDataSection = new ArrayList<>();

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


        /*
        MINION SECTION
         */

        config.addDefault("minion.miner.radius", 1);
        config.addDefault("minion.miner.max-level", 3);
        config.addDefault("minion.miner.levels.1", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.miner.levels.2", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.miner.levels.3", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.slayer.radius", 1);
        config.addDefault("minion.slayer.max-level", 3);
        config.addDefault("minion.slayer.levels.1", 1);
        config.addDefault("minion.slayer.levels.2", 3);
        config.addDefault("minion.slayer.levels.3", 5);
        config.addDefault("minion.lumberjack.radius", 1);
        config.addDefault("minion.lumberjack.max-level", 3);
        config.addDefault("minion.lumberjack.levels.1", "radius:2,amount:1");
        config.addDefault("minion.lumberjack.levels.2", "radius:3,amount:3");
        config.addDefault("minion.lumberjack.levels.3", "radius:5,amount:5");

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

            for (String minionType : config.getConfigurationSection("minion").getKeys(false)) {

                Minion.Type type = Skyblock.get().getMinionHandler().getTypeFromName(minionType);
                int maxLvl = config.getInt("minion." + minionType + ".max-level");
                int radius = config.getInt("minion." + minionType + ".radius");

                MinionData data = new MinionData();
                data.setType(type);
                data.setMaxLvl(maxLvl);
                data.setRadius(radius);

                for (String s : config.getConfigurationSection("minion." + minionType + ".levels").getKeys(false)) {

                    int level = Integer.parseInt(s);

                    Pair<Integer, Object> o = new Pair<>(level, config.get("minion." + minionType + ".levels." + level));
                    data.getObjects().add(o);
                }

                this.minionDataSection.add(data);
            }
        }
    }

    public void write(){
    }
}
