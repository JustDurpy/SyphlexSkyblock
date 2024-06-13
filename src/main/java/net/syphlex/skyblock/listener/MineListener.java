package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.mine.data.Mine;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MineListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){

        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        final Location l = b.getLocation();

        if (Permissions.MINE_ADMIN.has(p))
            return;

        for (Mine mine : Skyblock.get().getMineHandler().getMines()) {

            if (!mine.isInside(l)) continue;

            if (!mine.isInsideMiningArea(l)) {
                Messages.CANNOT_MINE_OUTSIDE_MINING_AREA.send(p);
                e.setCancelled(true);
                break;
            }

            break;
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){

        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        final Location l = b.getLocation();

        if (Permissions.MINE_ADMIN.has(p))
            return;

        for (Mine mine : Skyblock.get().getMineHandler().getMines()) {

            if (!mine.isInside(l)) continue;

            e.setCancelled(true);
            break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent e){

        final Entity victim = e.getEntity();
        final Entity damager = e.getDamager();

        final Location victimLocation = victim.getLocation();
        final Location damagerLocation = damager.getLocation();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            if (Permissions.MINE_ADMIN.has(player))
                return;
        }

        for (Mine mine : Skyblock.get().getMineHandler().getMines()) {

            if (!mine.isInsideMiningArea(victimLocation)
                    && !mine.isInsideMiningArea(damagerLocation))
                continue;

            if (!mine.isPvp()) {
                if (damager instanceof Player) {
                    Player p = (Player) damager;
                    Messages.CANNOT_PVP_IN_MINE.send(p);
                }
                e.setCancelled(true);
                break;
            }

            e.setCancelled(false);
            break;
        }
    }
}
