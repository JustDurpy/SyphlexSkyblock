package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.Island;
import net.syphlex.skyblock.profile.IslandProfile;
import net.syphlex.skyblock.util.WorldUtil;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IslandListener implements Listener {

    /*
        The logic for every event is su checking if the player
        is a part of an island FIRST, then we will check if their location is inside THEIR
        island location that way we don't have to loop through every island (cause lag)
         */

    @EventHandler(ignoreCancelled = true)
    public void onBlockFormEvent(BlockFormEvent e){

        final Block b = e.getBlock();

        if (!WorldUtil.isWorld(b.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Island island = Skyblock.get().getIslandHandler().getIslandAtLocation(b.getLocation());

        /*
        a real island was not found at the location
         */
        if (island == null) {
            return;
        }

        //if (e.getNewState().getType() == Material.STONE || e.getNewState().getType() == Material.COBBLESTONE) {
            //e.setCancelled(true);

            Material material = island.getUpgrades().getGenerator().generate().getMaterial();

            if (material == null)
                material = Material.STONE;

            e.getNewState().setType(material);
        //}
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractEvent(PlayerInteractEvent e){
        final Player p = e.getPlayer();

        if (!p.getWorld().getName().equalsIgnoreCase(ConfigEnum.ISLAND_WORLD.getAsString()))
            return;

        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (profile.getIsland() == null)
            return;

        if (!profile.getIsland().isInside(p.getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlock();

        if (!p.getWorld().getName().equalsIgnoreCase(ConfigEnum.ISLAND_WORLD.getAsString()))
            return;

        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (profile.getIsland() == null)
            return;

        if (!profile.getIsland().isInside(block.getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlockPlaced();

        if (!p.getWorld().getName().equalsIgnoreCase(ConfigEnum.ISLAND_WORLD.getAsString()))
            return;

        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (profile.getIsland() == null)
            return;

        if (!profile.getIsland().isInside(block.getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleportEvent(PlayerTeleportEvent e){
        final Player p = e.getPlayer();
        IslandProfile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland() || e.getTo() == null)
            return;

        if (profile.getIsland().isInside(e.getTo()))
            return;

        Skyblock.get().getIslandHandler().degenerateIslandBorder(p);
    }

}
