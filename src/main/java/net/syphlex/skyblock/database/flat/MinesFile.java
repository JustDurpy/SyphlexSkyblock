package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.handler.mine.data.Mine;
import net.syphlex.skyblock.handler.mine.data.MineBlockData;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MinesFile extends SimpleConfig {
    public MinesFile() {
        super("/mines.yml", false);
    }

    public ArrayList<Mine> read(){

        config.options().copyDefaults(true);
        config.addDefault("mines.A1.name", "A");
        config.addDefault("mines.A1.id", 1);
        config.addDefault("mines.A1.blocks.1.material", "STONE");
        config.addDefault("mines.A1.blocks.1.chance", 50);
        config.addDefault("mines.A1.blocks.2.material", "IRON_ORE");
        config.addDefault("mines.A1.blocks.2.chance", 50);

        save();

        ArrayList<Mine> mines = new ArrayList<>();

        for (String section : config.getConfigurationSection("mines").getKeys(false)) {

            String name = config.getString("mines." + section + ".name");
            int id = config.getInt("mines." + section + ".id");

            ArrayList<MineBlockData> blocks = new ArrayList<>();

            for (String blockSec : config.getConfigurationSection("mines." + section + ".blocks").getKeys(false)) {
                Material material = Material.getMaterial(config.getString("mines." + section + ".blocks." + blockSec + ".material"));
                double chance = config.getDouble("mines." + section + ".blocks." + blockSec + ".chance");
                blocks.add(new MineBlockData(material, chance));
            }

            mines.add(new Mine(id, name, blocks));
        }

        return mines;
    }

    public void write(Mine mine){

        if (mine == null)
            return;

        try {

            if (!getFile().exists()) {
                getFile().createNewFile();
            }

            config.set("mines." + mine.getConfigName() + ".name", mine.getMineName());
            config.set("mines." + mine.getConfigName() + ".id", mine.getId());

            if (mine.getBlocks().size() < 1) return;

            int i = 1;
            for (MineBlockData data : mine.getBlocks()) {

                config.set("mines." + mine.getConfigName() + ".blocks." + i + ".material", data.getMaterial().name());
                config.set("mines." + mine.getConfigName() + ".blocks." + i + ".chance", data.getChance());

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
