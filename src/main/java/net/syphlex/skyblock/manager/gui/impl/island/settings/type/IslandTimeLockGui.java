package net.syphlex.skyblock.manager.gui.impl.island.settings.type;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandTimeLockGui extends SimpleGui {
    public IslandTimeLockGui() {
        super("Island Settings - Time Lock", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

    }

    @Override
    public void onClickEvent(ClickEvent e) {

    }
}
