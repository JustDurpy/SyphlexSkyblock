package net.syphlex.skyblock.manager.gui.impl.island.settings.type;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.settings.impl.IslandTimeLock;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandTimeLockGui extends SimpleGui {

    public IslandTimeLockGui() {
        super(ConfigMenu.TIME_LOCK_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.TIME_LOCK_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.TIME_LOCK_MENU);

        for (GuiItem guiItem : ConfigMenu.TIME_LOCK_MENU.getMenuSetting().getItems())
            this.inventory.setItem(guiItem.slot(), guiItem.item());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        final Island island = profile.getIsland();

        for (GuiItem guiItem : ConfigMenu.TIME_LOCK_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.slot())
                continue;

            ItemStack item = guiItem.item();

            if (!PluginUtil.isValidItem(item))
                continue;

            if (guiItem.id() == 99) {
                profile.openIslandPanel();
                return;
            }

            island.getSettings().setTimeLock(IslandTimeLock.find(guiItem.id()));
            break;
        }
    }
}
