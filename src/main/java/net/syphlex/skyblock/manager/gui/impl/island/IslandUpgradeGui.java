package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.util.data.ClickEvent;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandUpgradeGui extends SimpleGui {

    private final Island island;

    public IslandUpgradeGui(Island island) {
        super("Island Upgrades", 27);

        this.island = island;

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));


    }

    @Override
    public void onClickEvent(ClickEvent e) {

    }
}
