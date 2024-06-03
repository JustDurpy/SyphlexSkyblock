package net.syphlex.skyblock.manager.gui.impl.island.settings.type;

import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.settings.IslandSettings;
import net.syphlex.skyblock.manager.island.settings.impl.IslandBorderColor;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandBorderColorGui extends SimpleGui {
    public IslandBorderColorGui() {
        super("Island Border Color", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        for (IslandBorderColor color : IslandBorderColor.values())
            this.inventory.setItem(color.getGuiItem().slot(), color.getGuiItem().item());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        for (IslandBorderColor color : IslandBorderColor.values()) {

            if (e.getSlot() != color.getGuiItem().slot())
                continue;

            if (!profile.getPlayer().hasPermission(color.getPermission())) {
                Messages.RANK_REQUIRED.send(profile);
                continue;
            }

            island.getSettings().setIslandBorderColor(color);
            island.refreshBorder();
            closeInventory(profile.getPlayer());
        }
    }
}
