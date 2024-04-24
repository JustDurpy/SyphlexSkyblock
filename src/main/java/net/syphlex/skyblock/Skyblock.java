package net.syphlex.skyblock;

import lombok.Getter;
import net.syphlex.skyblock.cmd.IslandCmd;
import net.syphlex.skyblock.database.flat.PluginFile;
import net.syphlex.skyblock.handler.DataHandler;
import net.syphlex.skyblock.handler.gui.GuiHandler;
import net.syphlex.skyblock.handler.island.IslandHandler;
import net.syphlex.skyblock.handler.island.upgrade.IslandUpgradeHandler;
import net.syphlex.skyblock.handler.scoreboard.ScoreboardHandler;
import net.syphlex.skyblock.listener.GuiListener;
import net.syphlex.skyblock.listener.IslandListener;
import net.syphlex.skyblock.listener.JoinQuitListener;
import net.syphlex.skyblock.listener.PlayerListener;
import net.syphlex.skyblock.util.VoidGenerator;
import net.syphlex.skyblock.util.WorldUtil;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    private VoidGenerator voidGenerator;
    private IslandUpgradeHandler upgradeHandler;
    private IslandHandler islandHandler;
    private ScoreboardHandler scoreboardHandler;
    private DataHandler dataHandler;
    private GuiHandler guiHandler;

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
    }

    private void registerCmds(){
        new IslandCmd();
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
    }

    private void init(){
        this.upgradeHandler = new IslandUpgradeHandler();
        this.islandHandler = new IslandHandler();
        this.scoreboardHandler = new ScoreboardHandler();
        this.dataHandler = new DataHandler();
        this.guiHandler = new GuiHandler();
    }

    private void start(){
        this.upgradeHandler.onEnable();
        this.islandHandler.onEnable();
        this.dataHandler.onEnable();
        this.scoreboardHandler.onEnable();
    }

    private void stop(){
        this.dataHandler.onDisable();
        this.scoreboardHandler.onDisable();
        this.islandHandler.onDisable();
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
