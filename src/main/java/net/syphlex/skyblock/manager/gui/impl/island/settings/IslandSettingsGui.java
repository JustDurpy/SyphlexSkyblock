package net.syphlex.skyblock.manager.gui.impl.island.settings;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandBorderColorGui;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandTimeLockGui;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandWeatherLockGui;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;

public class IslandSettingsGui extends SimpleGui {

    public IslandSettingsGui() {
        super(ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.ISLAND_SETTINGS_MENU);

        setItems(ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile p = e.getProfile();

        for (GuiItem guiItem : ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            switch (guiItem.getId()) {

                case 0:

                    if (!ConfigMenu.TIME_LOCK_MENU.getMenuSetting().isEnabled()) {
                        Messages.FEATURE_DISABLED.send(p);
                        break;
                    }

                    Skyblock.get().getGuiHandler().openGui(p, new IslandTimeLockGui());
                    break;
                case 1:

                    if (!ConfigMenu.WEATHER_LOCK_MENU.getMenuSetting().isEnabled()) {
                        Messages.FEATURE_DISABLED.send(p);
                        break;
                    }

                    Skyblock.get().getGuiHandler().openGui(p, new IslandWeatherLockGui());
                    break;
                case 2:

                    if (!ConfigMenu.ISLAND_BORDER_COLOR_MENU.getMenuSetting().isEnabled()) {
                        Messages.FEATURE_DISABLED.send(p);
                        break;
                    }

                    Skyblock.get().getGuiHandler().openGui(p, new IslandBorderColorGui());
                    break;
                case 3:
                    p.sendMessage("in development...");
                    break;
                case 99:
                    p.openIslandPanel();
                    break;
            }
            break;
        }
    }

}
