package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.manager.island.block.OreGeneratorBlockData;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class SkyblockSettingsFile extends SimpleConfig {

    private final ArrayList<SpecialBlockData> specialBlockData = new ArrayList<>();
    private final ArrayList<IslandOreGenerator> islandOreGenerators = new ArrayList<>();

    public SkyblockSettingsFile() {
        super("/settings.yml", false);
    }

    public void read() {

        config.options().copyDefaults(true);

        /*
        SPECIAL BLOCKS SECTION
         */
        specialBlocksConfig();

        /*
        ORE GENERATOR SECTION
         */
        generatorConfig();

        /*
        save defaults
         */
        save();

        readSpecialBlocksConfig();
        readGeneratorConfig();
    }

    public void write(){
        // todo write if needed
    }

    private void readSpecialBlocksConfig(){
        for (String specialBlocks : config.getStringList("special-blocks")) {
            SpecialBlockData blockData = Skyblock.get().getUpgradeHandler().getSpecialBlockDataFromString(specialBlocks);
            Skyblock.get().getUpgradeHandler().getSpecialBlocks().add(blockData);
        }
    }

    private void readGeneratorConfig(){
        for (String section : config.getConfigurationSection("generators").getKeys(false)) {

            int tier = config.getInt("generators." + section + ".tier");
            String name = config.getString("generators." + section + ".name");

            IslandOreGenerator generator = new IslandOreGenerator(tier, name);

            for (String blockSec : config.getConfigurationSection("generators." + section + ".blocks").getKeys(false)) {
                String materialName = config.getString("generators." + section + ".blocks." + blockSec + ".material");
                double chance = config.getDouble("generators." + section + ".blocks." + blockSec + ".chance");
                generator.getBlocks().add(new OreGeneratorBlockData(Material.getMaterial(materialName), chance));
            }

            Skyblock.get().getUpgradeHandler().getOreGenerators().add(generator);
        }
    }

    private void specialBlocksConfig(){
        config.addDefault("special-blocks", Arrays.asList(
                "DIAMOND_BLOCK:10000:&6&lX%amount% &e&lDIAMOND BLOCKS",
                "IRON_BLOCK:5000:&6&lX%amount% &e&lIRON BLOCKS"));
    }

    private void generatorConfig(){
        /*
        config.addDefault("generators.cannot-upgrade.gui-item.lore", Arrays.asList(
                "",
                "&cYou cannot upgrade your generator",
                "&cbecause your island generator is",
                "&calready at the maximum level.",
                ""
        ));
        config.addDefault("generators.example.gui-item.name", "");
        config.addDefault("generators.example.gui-item.material", "");
        config.addDefault("generators.example.gui-item.slot", 0);
        config.addDefault("generators.example.gui-item.lore", Arrays.asList(
                "",
                "&e&lOres",
                " &6➥ &fCobblestone &7(80%)",
                " &6➥ &fCoal Ore &7(20%)",
                "",
                "&7&o(( Left click to upgrade ))",
                ""
        ));

         */
        config.addDefault("generators.example.tier", 0);
        config.addDefault("generators.example.name", "&7Stone Generator");
        config.addDefault("generators.example.blocks.1.material", "COBBLESTONE");
        config.addDefault("generators.example.blocks.1.chance", 80);
        config.addDefault("generators.example.blocks.2.material", "COAL_ORE");
        config.addDefault("generators.example.blocks.2.chance", 20);
    }
}
