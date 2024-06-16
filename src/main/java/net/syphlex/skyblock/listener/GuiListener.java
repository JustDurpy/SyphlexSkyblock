package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.manage.IslandPanelGui;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){

        if (!(e.getWhoClicked() instanceof Player))
            return;

        final Player p = (Player) e.getWhoClicked();
        final Profile profile = Skyblock.get().getDataHandler().get(p);

        if (!Skyblock.get().getGuiHandler().isInOurGui(profile, e.getInventory()))
            return;

        final int slot = e.getRawSlot();

        e.setCancelled(true);

        if (slot == Skyblock.get().getGuiHandler().get(profile).getIslandPanelButtonSlot()) {
            Skyblock.get().getGuiHandler().openGui(profile, new IslandPanelGui());
            return;
        }

        Skyblock.get().getGuiHandler().get(profile).onClickEvent(
                new ClickEvent(profile, slot));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent e){

        if (!(e.getPlayer() instanceof Player))
            return;

        final Player p = (Player) e.getPlayer();
        final Profile profile = Skyblock.get().getDataHandler().get(p);

        if (!Skyblock.get().getGuiHandler().isInOurGui(profile, e.getInventory()))
            return;

        Skyblock.get().getGuiHandler().get(profile).onCloseEvent();
        Skyblock.get().getGuiHandler().closeGui(profile);
    }
}
