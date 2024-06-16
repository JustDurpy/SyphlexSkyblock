package net.syphlex.skyblock.manager.gui.impl.island.settings.type;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.settings.IslandSettings;
import net.syphlex.skyblock.manager.island.settings.impl.IslandBorderColor;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandBorderColorGui extends SimpleGui {
    public IslandBorderColorGui() {
        super(ConfigMenu.ISLAND_BORDER_COLOR_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.ISLAND_BORDER_COLOR_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.ISLAND_BORDER_COLOR_MENU);

        setItems(ConfigMenu.ISLAND_BORDER_COLOR_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        for (GuiItem guiItem : ConfigMenu.ISLAND_BORDER_COLOR_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            ItemStack item = guiItem.getItem();

            if (!PluginUtil.isValidItem(item))
                continue;

            if (guiItem.getId() == 99) {
                profile.openIslandPanel();
                break;
            }

            IslandBorderColor color = IslandBorderColor.find(guiItem.getId());

            if (!profile.getPlayer().hasPermission(color.getPermission())) {
                Messages.RANK_REQUIRED.send(profile);
                break;
            }

            island.getSettings().setIslandBorderColor(color);
            island.refreshBorder();
            break;
        }
    }
}
