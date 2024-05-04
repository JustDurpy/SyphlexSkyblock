package net.syphlex.skyblock;

import lombok.Getter;
import net.syphlex.skyblock.cmd.IslandCmd;
import net.syphlex.skyblock.cmd.MineCmd;
import net.syphlex.skyblock.cmd.MinionCmd;
import net.syphlex.skyblock.cmd.PluginCmd;
import net.syphlex.skyblock.database.flat.PluginFile;
import net.syphlex.skyblock.database.flat.SkyblockSettingsFile;
import net.syphlex.skyblock.manager.customenchant.EnchantHandler;
import net.syphlex.skyblock.manager.gui.GuiHandler;
import net.syphlex.skyblock.manager.island.IslandHandler;
import net.syphlex.skyblock.manager.island.IslandUpgradeHandler;
import net.syphlex.skyblock.manager.mine.MineHandler;
import net.syphlex.skyblock.manager.minion.MinionHandler;
import net.syphlex.skyblock.manager.mobcoin.MobCoinHandler;
import net.syphlex.skyblock.manager.profile.DataHandler;
import net.syphlex.skyblock.manager.schematic.SchematicHandler;
import net.syphlex.skyblock.manager.scoreboard.ScoreboardHandler;
import net.syphlex.skyblock.manager.thread.ThreadHandler;
import net.syphlex.skyblock.listener.*;
import net.syphlex.skyblock.util.VoidGenerator;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

@Getter
public class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    private final ArrayList<SimpleConfig> configs = new ArrayList<>();

    private ThreadHandler threadHandler;
    private SkyblockSettingsFile settingsFile;
    private SchematicHandler schematicHandler;
    private IslandUpgradeHandler upgradeHandler;
    private IslandHandler islandHandler;
    private MineHandler mineHandler;
    private DataHandler dataHandler;
    private MinionHandler minionHandler;
    private ScoreboardHandler scoreboardHandler;
    private GuiHandler guiHandler;
    private MobCoinHandler mobCoinHandler;
    private EnchantHandler enchantHandler;


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
        new PluginCmd();
        new IslandCmd();
        new MineCmd();
        new MinionCmd();
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinQuitListener(), this);
        pm.registerEvents(new IslandListener(), this);
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new GuiListener(), this);
        pm.registerEvents(new MinionListener(), this);
    }

    private void load(){
        PluginFile pluginFile = new PluginFile();
        pluginFile.read();

        this.settingsFile = new SkyblockSettingsFile();
    }

    private void init(){
        this.threadHandler = new ThreadHandler();
        this.upgradeHandler = new IslandUpgradeHandler();
        this.islandHandler = new IslandHandler();
        this.mineHandler = new MineHandler();
        this.dataHandler = new DataHandler();
        this.minionHandler = new MinionHandler();
        this.scoreboardHandler = new ScoreboardHandler();
        this.schematicHandler = new SchematicHandler();
        this.guiHandler = new GuiHandler();
        this.mobCoinHandler = new MobCoinHandler();
        this.enchantHandler = new EnchantHandler();
    }

    private void start(){
        this.threadHandler.onEnable();
        this.islandHandler.onEnable();
        this.mineHandler.onEnable();
        this.dataHandler.onEnable();
        this.minionHandler.onEnable();
        this.scoreboardHandler.onEnable();
        this.schematicHandler.onEnable();
        this.enchantHandler.onEnable();
        this.settingsFile.read();
    }

    private void stop(){
        this.schematicHandler.onDisable();
        this.scoreboardHandler.onDisable();
        this.minionHandler.onDisable();
        this.dataHandler.onDisable();
        this.mineHandler.onDisable();
        this.islandHandler.onDisable();
        this.threadHandler.onDisable();
    }

    public Location getMainSpawn(){
        return Bukkit.getWorld(ConfigEnum.MAIN_WORLD.getAsString()).getSpawnLocation();
    }

    public World getIslandWorld(){
        return Bukkit.getWorld(ConfigEnum.ISLAND_WORLD.getAsString());
    }

    private VoidGenerator voidGenerator;

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
        return this.voidGenerator;
    }

    public static Skyblock get(){
        return instance;
    }

    public static void log(Object log){
        get().getLogger().log(Level.SEVERE, log.toString());
    }

    public static void info(Object log){
        get().getLogger().info(log.toString());
    }

}
