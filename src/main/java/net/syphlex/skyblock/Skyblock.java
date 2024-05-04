package net.syphlex.skyblock;

import lombok.Getter;
import net.syphlex.skyblock.cmd.IslandCmd;
import net.syphlex.skyblock.cmd.MineCmd;
import net.syphlex.skyblock.cmd.MinionCmd;
import net.syphlex.skyblock.database.flat.PluginFile;
import net.syphlex.skyblock.database.flat.SkyblockSettingsFile;
import net.syphlex.skyblock.manager.Handler;
import net.syphlex.skyblock.manager.thread.ThreadHandler;
import net.syphlex.skyblock.listener.*;
import net.syphlex.skyblock.util.VoidGenerator;
import net.syphlex.skyblock.util.WorldUtil;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    private ThreadHandler threadHandler;
    private VoidGenerator voidGenerator;
    private SkyblockSettingsFile settingsFile;
    private Handler handlers;

    @Override
    public void onLoad(){
        this.voidGenerator = new VoidGenerator();
    }

    @Override
    public void onEnable(){
        instance = this;

        load();
        init();
        start();

        WorldUtil.createWorld(
                World.Environment.NORMAL,
                ConfigEnum.ISLAND_WORLD.getAsString());

        registerListeners();
        registerCmds();
    }

    @Override
    public void onDisable(){
        stop();
        this.settingsFile.write();
    }

    private void registerCmds(){
        new IslandCmd();
        new MineCmd();
        new MinionCmd();
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new IslandListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
    }

    private void load(){
        PluginFile pluginFile = new PluginFile();
        pluginFile.read();

        this.settingsFile = new SkyblockSettingsFile();
    }

    private void init(){
        this.threadHandler = new ThreadHandler();
        this.handlers = new Handler();
    }

    private void start(){
        this.threadHandler.onEnable();
        this.handlers.onEnable();
        this.settingsFile.read();
    }

    private void stop(){
        this.handlers.onDisable();
        this.threadHandler.onDisable();
    }

    public Location getMainSpawn(){
        return Bukkit.getWorld(ConfigEnum.MAIN_WORLD.getAsString()).getSpawnLocation();
    }

    public World getIslandWorld(){
        return Bukkit.getWorld(ConfigEnum.ISLAND_WORLD.getAsString());
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
        return this.voidGenerator;
    }

    public static Skyblock get(){
        return instance;
    }
}
