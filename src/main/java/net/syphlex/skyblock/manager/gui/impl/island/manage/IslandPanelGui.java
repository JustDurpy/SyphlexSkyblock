package net.syphlex.skyblock.manager.gui.impl.island.manage;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.extra.IslandInfoGui;
import net.syphlex.skyblock.manager.gui.impl.island.extra.IslandTopGui;
import net.syphlex.skyblock.manager.gui.impl.island.permissions.IslandPermissionsRolesGui;
import net.syphlex.skyblock.manager.gui.impl.island.settings.IslandSettingsGui;
import net.syphlex.skyblock.manager.gui.impl.island.upgrades.IslandUpgradeGui;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;

public class IslandPanelGui extends SimpleGui {

    public IslandPanelGui() {
        super(ConfigMenu.ISLAND_PANEL_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.ISLAND_PANEL_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.ISLAND_PANEL_MENU);

        setItems(ConfigMenu.ISLAND_PANEL_MENU.getMenuSetting().getItems());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        for (GuiItem guiItem : ConfigMenu.ISLAND_PANEL_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            handleClick(profile, guiItem.getId());
            break;
        }
    }

    private void handleClick(Profile profile, int id){
        switch (id) {
            case 0:
                closeInventory(profile.getPlayer());
                profile.getIsland().teleport(profile.getPlayer());
                Messages.TELEPORTED_TO_ISLAND.send(profile);
                break;
            case 1:

                if (!ConfigMenu.ISLAND_INFORMATION_MENU.getMenuSetting().isEnabled()) {
                    Messages.FEATURE_DISABLED.send(profile);
                    break;
                }

                Skyblock.get().getGuiHandler().openGui(profile, new IslandInfoGui(profile.getIsland()));
                break;
            case 2:
                if (!ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().isEnabled()) {
                    Messages.FEATURE_DISABLED.send(profile);
                    break;
                }
                Skyblock.get().getGuiHandler().openGui(profile, new IslandTopGui());
                break;
            case 3:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandUpgradeGui(profile.getIsland()));
                break;
            case 4:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsRolesGui());
                break;
            case 5:
                if (!ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().isEnabled()) {
                    Messages.FEATURE_DISABLED.send(profile);
                    break;
                }
                Skyblock.get().getGuiHandler().openGui(profile, new IslandSettingsGui());
                break;
        }
    }
}
