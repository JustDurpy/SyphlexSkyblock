package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.profile.IslandProfile;
import net.syphlex.skyblock.util.IslandUtil;
import net.syphlex.skyblock.util.WorldUtil;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IslandListener implements Listener {

    /*
        The logic for every event is su checking if the player
        is a part of an island FIRST, then we will check if their location is inside THEIR
        island location that way we don't have to loop through every island (cause lag)
         */

    @EventHandler(ignoreCancelled = true)
    public void onBlockFormEvent(BlockFormEvent e) {

        final Block b = e.getBlock();

        if (!WorldUtil.isWorld(b.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Island island = IslandUtil.getIslandAtLocation(b.getLocation());

        /*
        a real island was not found at the location
         */
        if (island == null) {
            return;
        }

        if (e.getNewState().getType() == Material.STONE || e.getNewState().getType() == Material.COBBLESTONE) {

            Material material = island.getUpgrades().getGenerator().generate().getMaterial();

            if (material == null)
                material = Material.STONE;

            e.getNewState().setType(material);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteractEvent(PlayerInteractEvent e) {
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland()) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getIsland().isInside(p.getLocation())) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlock();

        if (!WorldUtil.isWorld(block.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland()) {
            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getIsland().isInside(block.getLocation())) {
            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlockPlaced();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland()) {
            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getIsland().isInside(block.getLocation())) {
            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
        }
    }

    @EventHandler
    public void onTeleportEvent(PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (profile.hasIsland() && profile.getIsland().isInside(e.getTo()))
            return;

        if (WorldUtil.isWorld(e.getTo().getWorld(), Skyblock.get().getIslandWorld())) {

            Island island = IslandUtil.getIslandAtLocation(e.getTo());

            if (island == null)
                return;

            //Skyblock.get().getIslandHandler().degenerateIslandBorder(p);
            Skyblock.get().getIslandHandler().generateIslandBorder(island, p, Color.BLUE);
            return;
        }

        Skyblock.get().getIslandHandler().degenerateIslandBorder(p);
    }

    @EventHandler
    public void onWorldChangeEvent(PlayerChangedWorldEvent e){
        final Player p = e.getPlayer();

        if (WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld())) {
            Island island = IslandUtil.getIslandAtLocation(p.getLocation());

            if (island == null)
                return;

            Skyblock.get().getIslandHandler().generateIslandBorder(island, p, Color.BLUE);
        }
    }

    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player) {

            final Player p = (Player)e.getDamager();

            if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
                return;

            final IslandProfile profile = Skyblock.get().getDataHandler().get(p);

            if (!profile.hasIsland()) {
                e.setCancelled(true);
                Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
                return;
            }

            if (!profile.getIsland().isInside(p.getLocation())) {
                e.setCancelled(true);
                Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            }
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e){

        if (!(e.getEntity() instanceof Player))
            return;

        final Player p = (Player) e.getEntity();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland()) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getIsland().isInside(p.getLocation())) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Location l = p.getLocation();
        if (l.getY() < -64.0 && WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld())) {
            final Island island = IslandUtil.getIslandAtXZ(l.getBlockX(), l.getBlockZ());
            if (island == null) return;
            island.teleport(p);
            p.setFallDistance(0);
            p.setNoDamageTicks(20);
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent e){
        final Block b = e.getBlock();
        final Location l = b.getLocation();

        if (!(b.getBlockData() instanceof Ageable))
            return;

        if (!WorldUtil.isWorld(l.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {

                Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);

                if (island == null)
                    continue;

                final double multiplier = island.getUpgrades().getHarvestMult();

                //if (multiplier <= 1.0) continue;

                e.setCancelled(true);

                Ageable a = (Ageable) b.getBlockData();
                a.setAge(Math.min(
                        a.getAge() + (int)Math.ceil(multiplier * (multiplier == 1 ? 2 : 3)),
                        a.getMaximumAge()));
                b.setBlockData(a);
                b.getState().update();
            }
        }
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {

        if (!WorldUtil.isWorld(e.getEntity().getWorld(), Skyblock.get().getIslandWorld()))
            return;

        if (e.getEntity() instanceof Phantom) {
            e.setCancelled(true);
            return;
        }


        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER)
            return;

        /*
        todo make spawn mob multiplier (NOT RATE but AMOUNT)
         */
    }

}
