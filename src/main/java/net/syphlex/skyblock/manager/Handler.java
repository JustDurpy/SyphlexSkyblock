package net.syphlex.skyblock.manager;

import lombok.Getter;
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

@Getter
public class Handler {
    private final SchematicHandler schematicHandler;
    private final IslandUpgradeHandler upgradeHandler;
    private final IslandHandler islandHandler;
    private final MineHandler mineHandler;
    private final MinionHandler minionHandler;
    private final ScoreboardHandler scoreboardHandler;
    private final DataHandler dataHandler;
    private final GuiHandler guiHandler;
    private final MobCoinHandler mobCoinHandler;

    public Handler(){
        this.schematicHandler = new SchematicHandler();
        this.upgradeHandler = new IslandUpgradeHandler();
        this.islandHandler = new IslandHandler();
        this.mineHandler = new MineHandler();
        this.minionHandler = new MinionHandler();
        this.mobCoinHandler = new MobCoinHandler();
        this.scoreboardHandler = new ScoreboardHandler();
        this.dataHandler = new DataHandler();
        this.guiHandler = new GuiHandler();
    }

    public void onEnable(){
        this.schematicHandler.onEnable();
        this.islandHandler.onEnable();
        this.mineHandler.onEnable();
        this.minionHandler.onEnable();
        this.mobCoinHandler.onEnable();
        this.dataHandler.onEnable();
        this.scoreboardHandler.onEnable();
    }

    public void onDisable(){
        this.dataHandler.onDisable();
        this.scoreboardHandler.onDisable();
        this.mobCoinHandler.onDisable();
        this.minionHandler.onDisable();
        this.mineHandler.onDisable();
        this.islandHandler.onDisable();
        this.schematicHandler.onDisable();
    }
}
