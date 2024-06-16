package net.syphlex.skyblock.manager.gui.impl.island.extra;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.entity.Player;

public class IslandInfoGui extends SimpleGui {

    public IslandInfoGui(final Island island) {
        super(ConfigMenu.ISLAND_INFORMATION_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.ISLAND_INFORMATION_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.ISLAND_INFORMATION_MENU);

        setItems(ConfigMenu.ISLAND_INFORMATION_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

    }
}
