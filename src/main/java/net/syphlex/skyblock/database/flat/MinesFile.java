package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.manager.mine.data.Mine;
import net.syphlex.skyblock.manager.mine.data.MineBlockData;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("all")
public class MinesFile extends SimpleConfig {
    public MinesFile() {
        super("/mines.yml", false);
    }

    public ArrayList<Mine> read(){

        config.options().setHeader(Arrays.asList("for 'permission' use 'none' if the mine has no permission"));

        if (config.getConfigurationSection("mines") == null) {
            config.options().copyDefaults(true);
            config.addDefault("mines.a.name", "A");
            config.addDefault("mines.a.permission", "mine.a");
            config.addDefault("mines.a.pvp-enabled", false);
            config.addDefault("mines.a.spawn", "world;0;0;0");
            config.addDefault("mines.a.area-corner1", "world;0;0;0");
            config.addDefault("mines.a.area-corner2", "world;0;0;0");
            config.addDefault("mines.a.mine-pos1", "world;0;0;0");
            config.addDefault("mines.a.mine-pos2", "world;0;0;0");
            config.addDefault("mines.a.blocks.1.material", "STONE");
            config.addDefault("mines.a.blocks.1.chance", 35);
            config.addDefault("mines.a.blocks.2.material", "COBBLESTONE");
            config.addDefault("mines.a.blocks.2.chance", 35);
            config.addDefault("mines.a.blocks.3.material", "IRON_ORE");
            config.addDefault("mines.a.blocks.3.chance", 20);
        }

        save();

        ArrayList<Mine> mines = new ArrayList<>();

        for (String section : config.getConfigurationSection("mines").getKeys(false)) {

            String name = config.getString("mines." + section + ".name");
            String permission = config.getString("mines." + section + ".permission");
            boolean pvp = config.getBoolean("mines." + section + ".pvp-enabled");

            Position spawn = new Position(config.getString("mines." + section + ".spawn"));
            Position corner1 = new Position(config.getString("mines." + section + ".area-corner1"));
            Position corner2 = new Position(config.getString("mines." + section + ".area-corner2"));
            Position minePos1 = new Position(config.getString("mines." + section + ".mine-pos1"));
            Position minePos2 = new Position(config.getString("mines." + section + ".mine-pos2"));

            ArrayList<MineBlockData> blocks = new ArrayList<>();

            for (String blockSec : config.getConfigurationSection("mines." + section + ".blocks").getKeys(false)) {
                Material material = Material.getMaterial(config.getString("mines." + section + ".blocks." + blockSec + ".material"));
                double chance = config.getDouble("mines." + section + ".blocks." + blockSec + ".chance");
                blocks.add(new MineBlockData(material, chance));
            }

            mines.add(new Mine(section, name, permission, blocks, corner1, corner2, minePos1, minePos2, spawn, pvp));
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

            config.set("mines." + mine.getMineName() + ".name", mine.getDisplayName());
            config.set("mines." + mine.getMineName() + ".permission", mine.getPermission());
            config.set("mines." + mine.getMineName() + ".pvp-enabled", mine.isPvp());
            config.set("mines." + mine.getMineName() + ".spawn", mine.getSpawn().getAsString());
            config.set("mines." + mine.getMineName() + ".area-corner1", mine.getAreaCorner1().getAsString());
            config.set("mines." + mine.getMineName() + ".area-corner2", mine.getAreaCorner2().getAsString());
            config.set("mines." + mine.getMineName() + ".mine-pos1", mine.getMinePos1().getAsString());
            config.set("mines." + mine.getMineName() + ".mine-pos2", mine.getMinePos2().getAsString());

            if (mine.getBlocks().size() < 1) return;

            int i = 1;
            for (MineBlockData data : mine.getBlocks()) {

                config.set("mines." + mine.getMineName() + ".blocks." + i + ".material", data.getMaterial().name());
                config.set("mines." + mine.getMineName() + ".blocks." + i + ".chance", data.getChance());

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
