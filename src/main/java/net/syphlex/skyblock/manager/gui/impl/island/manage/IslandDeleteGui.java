package net.syphlex.skyblock.manager.gui.impl.island.manage;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandDeleteGui extends SimpleGui {
    public IslandDeleteGui() {
        super(ConfigMenu.DELETE_ISLAND_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.DELETE_ISLAND_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.DELETE_ISLAND_MENU);
        setItems(ConfigMenu.DELETE_ISLAND_MENU.getMenuSetting().getItems());

    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        for (GuiItem guiItem : ConfigMenu.DELETE_ISLAND_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            switch (guiItem.getId()) {
                case 0:
                    Skyblock.get().getIslandHandler().degenerateIsland(profile);
                    closeInventory(profile.getPlayer());
                    break;
                case 99:
                    profile.openIslandPanel();
                    break;
            }

            break;
        }
    }
}
