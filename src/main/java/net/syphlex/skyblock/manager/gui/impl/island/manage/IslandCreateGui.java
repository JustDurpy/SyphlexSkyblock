package net.syphlex.skyblock.manager.gui.impl.island.manage;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandCreateGui extends SimpleGui {
    public IslandCreateGui() {
        super(ConfigMenu.CREATE_ISLAND_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.CREATE_ISLAND_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.CREATE_ISLAND_MENU);
        setItems(ConfigMenu.CREATE_ISLAND_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        for (GuiItem guiItem : ConfigMenu.CREATE_ISLAND_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            String schematicName = guiItem.getStringId();
            Permissions permission = Permissions.getPermEnum(guiItem.getPermission());

            if (!permission.has(profile)) {
                Messages.NO_PERMISSION.send(profile);
                break;
            }

            Skyblock.get().getIslandHandler().generateIsland(
                    profile, schematicName);
            closeInventory(profile.getPlayer());
        }
    }
}
