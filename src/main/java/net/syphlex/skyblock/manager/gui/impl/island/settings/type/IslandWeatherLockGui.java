package net.syphlex.skyblock.manager.gui.impl.island.settings.type;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.settings.impl.IslandWeatherLock;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import net.syphlex.skyblock.util.simple.SimpleGui;

public class IslandWeatherLockGui extends SimpleGui {
    public IslandWeatherLockGui() {
        super(ConfigMenu.WEATHER_LOCK_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.WEATHER_LOCK_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.WEATHER_LOCK_MENU);

        setItems(ConfigMenu.WEATHER_LOCK_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        for (GuiItem guiItem : ConfigMenu.WEATHER_LOCK_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            if (!Permissions.getPermEnum(guiItem.getPermission()).has(profile)) {
                Messages.NO_PERMISSION.send(profile);
                return;
            }

            if (guiItem.getId() == 99) {
                profile.openIslandPanel();
                return;
            }

            island.getSettings().setWeatherLock(IslandWeatherLock.find(guiItem.getId()));
            break;
        }
    }
}
