package net.syphlex.skyblock;

import lombok.Getter;
import net.syphlex.skyblock.cmd.IslandCmd;
import net.syphlex.skyblock.cmd.MineCmd;
import net.syphlex.skyblock.cmd.MinionCmd;
import net.syphlex.skyblock.database.flat.PluginFile;
import net.syphlex.skyblock.database.flat.SkyblockSettingsFile;
import net.syphlex.skyblock.manager.minion.MinionHandler;
import net.syphlex.skyblock.manager.profile.DataHandler;
import net.syphlex.skyblock.manager.gui.GuiHandler;
import net.syphlex.skyblock.manager.island.IslandHandler;
import net.syphlex.skyblock.manager.island.IslandUpgradeHandler;
import net.syphlex.skyblock.manager.mine.MineHandler;
import net.syphlex.skyblock.manager.schematic.SchematicHandler;
import net.syphlex.skyblock.manager.scoreboard.ScoreboardHandler;
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
    private SchematicHandler schematicHandler;
    private IslandUpgradeHandler upgradeHandler;
    private IslandHandler islandHandler;
    private MineHandler mineHandler;
    private MinionHandler minionHandler;
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
        this.schematicHandler = new SchematicHandler();
        this.upgradeHandler = new IslandUpgradeHandler();
        this.islandHandler = new IslandHandler();
        this.mineHandler = new MineHandler();
        this.minionHandler = new MinionHandler();
        this.scoreboardHandler = new ScoreboardHandler();
        this.dataHandler = new DataHandler();
        this.guiHandler = new GuiHandler();
    }

    private void start(){
        this.threadHandler.onEnable();
        this.settingsFile.read();
        this.schematicHandler.onEnable();
        this.upgradeHandler.onEnable();
        this.islandHandler.onEnable();
        this.mineHandler.onEnable();
        this.minionHandler.onEnable();
        this.dataHandler.onEnable();
        this.scoreboardHandler.onEnable();
    }

    private void stop(){
        this.dataHandler.onDisable();
        this.scoreboardHandler.onDisable();
        this.minionHandler.onDisable();
        this.mineHandler.onDisable();
        this.islandHandler.onDisable();
        this.schematicHandler.onDisable();
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
