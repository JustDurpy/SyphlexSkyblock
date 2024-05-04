package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.profile.IslandProfile;
import net.syphlex.skyblock.util.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e){

        if (!(e.getWhoClicked() instanceof Player))
            return;

        final Player p = (Player) e.getWhoClicked();
        final IslandProfile profile = Skyblock.get().getHandlers().getDataHandler().get(p);

        if (!Skyblock.get().getHandlers().getGuiHandler().isInOurGui(profile, e.getInventory()))
            return;

        Skyblock.get().getHandlers().getGuiHandler().get(profile).onClickEvent(
                new ClickEvent(profile, e.getRawSlot()));
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){

        if (!(e.getPlayer() instanceof Player))
            return;

        final Player p = (Player) e.getPlayer();
        final IslandProfile profile = Skyblock.get().getHandlers().getDataHandler().get(p);

        if (!Skyblock.get().getHandlers().getGuiHandler().isInOurGui(profile, e.getInventory()))
            return;

        Skyblock.get().getHandlers().getGuiHandler().get(profile).onCloseEvent();
        Skyblock.get().getHandlers().getGuiHandler().closeGui(profile);
    }
}
