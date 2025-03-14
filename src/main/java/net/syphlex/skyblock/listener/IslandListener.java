package net.syphlex.skyblock.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.permissions.IslandPermission;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class IslandListener implements Listener {

    /*
        The logic for every event is su checking if the player
        is a part of an island FIRST, then we will check if their location is inside THEIR
        island location that way we don't have to loop through every island (cause lag)
         */

    @EventHandler
    public void onEntityDespawnEvent(EntityRemoveFromWorldEvent e) {

        if (!(e.getEntity() instanceof Item))
            return;

        final Item item = (Item) e.getEntity();

        if (item.getLocation().getY() > ConfigEnum.MINIMUM_Y_LIMIT.getAsDouble())
            return;

        final Island island = IslandUtil.getIslandAtXZ(
                item.getLocation().getX(),
                item.getLocation().getZ());

        if (island == null)
            return;

        /*
        If the server is using wild stacker we will use
        the wild stacker entity stack on the item
         */
        if (Skyblock.get().isWildStackerHook()) {

            // todo store to void chest

            return;
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtendEvent(BlockPistonExtendEvent e){

        if (!WorldUtil.isWorld(e.getBlock().getWorld(), Skyblock.get().getIslandWorld()))
                return;

        final Island island = IslandUtil.getIslandAtLocation(e.getBlock().getLocation());

        if (island == null)
            return;

        for (Block b : e.getBlocks()) {

            final Location l = b.getLocation();

            /*
            We are actually contracting it by '1' block
            the dimensions of the island with the border
            are a little fucky wucky, but it's fine, it works.
             */
            if (!island.isInside(l, -2, -2)) {
                e.setCancelled(true);
                break;
            }

            /*

            this is just a debug of like how tf this island border works

            if (island.isInside(l, -2, -2)) {
                Bukkit.broadcastMessage(StringUtil.CC("&ainside island! &r") + e.getBlock().getType().name() + " : " + b.getType().name());
            } else {
                Bukkit.broadcastMessage(StringUtil.CC("&coutside island! &r") + e.getBlock().getType().name() + " : " + b.getType().name());
            }
             */
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDropItemEvent(EntityDropItemEvent e){

        if (!(e.getEntity() instanceof Player))
            return;

        final Player p = (Player) e.getEntity();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        if (!profile.hasIsland() || !profile.getIsland().isInside(p.getLocation())) {

            final Island island = IslandUtil.getIslandAtLocation(p.getLocation());

            if (island != null && island.getVisitorRole().hasPermission(IslandPermission.PICKUP_AND_DROP_ITEMS))
                return;

            e.setCancelled(true);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.PICKUP_AND_DROP_ITEMS)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickUpItemEvent(EntityPickupItemEvent e){

        if (!(e.getEntity() instanceof Player)) {

            Entity entity = e.getEntity();

            if (!WorldUtil.isWorld(entity.getWorld(), Skyblock.get().getIslandWorld()))
                return;

            /*
            We don't want mobs picking up items
             */
            e.setCancelled(true);
            return;
        }

        final Player p = (Player) e.getEntity();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        if (!profile.hasIsland() || !profile.getIsland().isInside(p.getLocation())) {

            final Island island = IslandUtil.getIslandAtLocation(p.getLocation());

            if (island != null && island.getVisitorRole().hasPermission(IslandPermission.PICKUP_AND_DROP_ITEMS))
                return;

            e.setCancelled(true);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.PICKUP_AND_DROP_ITEMS)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteractEvent(EntityInteractEvent e){

        if (e.getEntity() instanceof Player)
            return;

        if (e.getBlock().getType() == Material.FARMLAND)
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){

        if ((e.getEntity() instanceof Player)) {

            final Player p = (Player) e.getEntity();

            if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
                return;

            Profile profile = Skyblock.get().getDataHandler().get(p);

            // admin mode will bypass all island restrictions
            if (profile.isAdminMode()) return;

            if (!profile.hasIsland() || !profile.getIsland().isInside(p.getLocation())) {

                Island island = IslandUtil.getIslandAtLocation(p.getLocation());

                if (island != null && island.getVisitorRole().hasPermission(IslandPermission.INTERACT))
                    return;

                e.setCancelled(true);
                return;
            }

            return;
        }

        if (!WorldUtil.isWorld(e.getEntity().getWorld(), Skyblock.get().getIslandWorld()))
            return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawnerSpawnEvent(SpawnerSpawnEvent e){

        final CreatureSpawner spawner = e.getSpawner();
        final Location l = e.getLocation();

        /*
        default spawner: min=200, max=800
         */

        final double defaultMax = 800.0;
        final double defaultMin = 200.0;

        if (!WorldUtil.isWorld(l.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        if (e.getSpawner().getMaxNearbyEntities() != 2500)
            e.getSpawner().setMaxNearbyEntities(2500);

        final Island island = IslandUtil.getIslandAtLocation(l);

        if (island == null) return;

        final double multiplier = island.getUpgrades().getSpawnRate().get();

        if (multiplier <= 1.0) return;

        /*
        2.5x multiplier = 320 max delay
        2.5x multiplier =
         */

        final int maxDelay = (int) (defaultMax / multiplier);
        final int minDelay = (int) (defaultMin / multiplier);

        if (spawner.getMaxSpawnDelay() != maxDelay)
            spawner.setMaxSpawnDelay(maxDelay);
        if (spawner.getMinSpawnDelay() != minDelay)
            spawner.setMinSpawnDelay(minDelay);

        /*
        we want to check if the spawner is stacked, if it is we will multiply the spawner amount
        by the stack amount
         */
        if (!Skyblock.get().getUpgradeHandler().isSpecialBlock(spawner.getType())
                || spawner.getSpawnedType() == null)
            return;

        IslandBlockData blockData = island.getStoredBlock(spawner.getLocation());

        for (int i = 0; i < blockData.getAmount() - 1; i++)
            spawner.getWorld().spawnEntity(e.getEntity().getLocation(), spawner.getSpawnedType());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplodeEvent(EntityExplodeEvent e){
        final Entity block = e.getEntity();

        if (!WorldUtil.isWorld(block.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        e.blockList().clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplodeEvent(BlockExplodeEvent e) {

        final Block block = e.getBlock();

        if (!WorldUtil.isWorld(block.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        e.blockList().clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockSpreadEvent(BlockSpreadEvent e) {

        final Block block = e.getSource();

        if (!WorldUtil.isWorld(block.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        if (block.getType().equals(Material.FIRE))
            e.setCancelled(true);
    }

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeathEvent(PlayerDeathEvent e){

        final Player p = e.getEntity();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        e.setKeepInventory(true);
        e.setKeepLevel(true);

        p.spigot().respawn();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {
            final Profile profile = Skyblock.get().getDataHandler().get(p);
            if (!profile.hasIsland()) {
                return;
            }
            profile.getIsland().teleport(p);
        }, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFormEvent(BlockFormEvent e) {

        final Block b = e.getBlock();

        if (!WorldUtil.isWorld(b.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Island island = IslandUtil.getIslandAtLocation(b.getLocation());

        /*
        a real island was not found at the location
         */
        if (island == null)
            return;

        if (e.getNewState().getType() == Material.STONE || e.getNewState().getType() == Material.COBBLESTONE) {

            Material material = Skyblock.get().getUpgradeHandler().getOreGenerator((int)island.getUpgrades().getGenerator().get()).generate().getMaterial();

            if (material == null)
                material = Material.STONE;

            e.getNewState().setType(material);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        if (!profile.hasIsland() || !profile.getIsland().isInside(p.getLocation())) {

            Island island = IslandUtil.getIslandAtLocation(p.getLocation());

            if (island != null && island.getVisitorRole().hasPermission(IslandPermission.INTERACT))
                return;

            e.setCancelled(true);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.INTERACT)) {
            e.setCancelled(true);
            return;
        }

        final Block b = e.getClickedBlock();

        if (b == null || !(b.getState() instanceof InventoryHolder))
            return;

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.ACCESS_CONTAINERS)) {
            e.setCancelled(true);
            Messages.NO_ISLAND_PERMISSION.send(profile);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlock();

        if (!WorldUtil.isWorld(block.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        /*
        if (!profile.hasIsland()) {
            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }
         */

        if (!profile.hasIsland() || !profile.getIsland().isInside(block.getLocation())) {

            Island island = IslandUtil.getIslandAtLocation(block.getLocation());
            if (island != null && island.getVisitorRole().hasPermission(IslandPermission.BLOCK_BREAK))
                return;

            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.BLOCK_BREAK)) {
            e.setCancelled(true);
            Messages.NO_ISLAND_PERMISSION.send(profile);
        }

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        final Player p = e.getPlayer();
        final Block block = e.getBlockPlaced();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        if (!profile.hasIsland() || !profile.getIsland().isInside(block.getLocation())) {

            Island island = IslandUtil.getIslandAtLocation(block.getLocation());
            if (island != null && island.getVisitorRole().hasPermission(IslandPermission.BLOCK_BREAK))
                return;

            e.setCancelled(true);
            Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.BLOCK_PLACE)) {
            e.setCancelled(true);
            Messages.NO_ISLAND_PERMISSION.send(profile);
        }
    }

    @EventHandler
    public void onTeleportEvent(PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        Profile profile = Skyblock.get().getDataHandler().get(p);

        if (profile.hasIsland() && profile.getIsland().isInside(e.getTo()))
            return;

        if (WorldUtil.isWorld(e.getTo().getWorld(), Skyblock.get().getIslandWorld())) {

            Island island = IslandUtil.getIslandAtLocation(e.getTo());

            if (island == null)
                return;

            //Skyblock.get().getIslandHandler().degenerateIslandBorder(p);
            island.generateIslandBorder(p);
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

            island.generateIslandBorder(p);
        }
    }

    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player) {

            final Player p = (Player)e.getDamager();

            if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
                return;

            final Profile profile = Skyblock.get().getDataHandler().get(p);

            // admin mode will bypass all island restrictions
            if (profile.isAdminMode()) return;

            if (!profile.hasIsland() && !profile.getIsland().isInside(p.getLocation())) {

                Island island = IslandUtil.getIslandAtLocation(p.getLocation());
                if (island != null && island.getVisitorRole().hasPermission(IslandPermission.DAMAGE_ENTITIES))
                    return;

                e.setCancelled(true);
                Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
                return;
            }

            if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.DAMAGE_ENTITIES)) {
                e.setCancelled(true);
                Messages.NO_ISLAND_PERMISSION.send(profile);
                return;
            }

            if (!(e.getEntity() instanceof Player))
                return;

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e){

        if (!(e.getEntity() instanceof Player))
            return;

        final Player p = (Player) e.getEntity();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        // admin mode will bypass all island restrictions
        if (profile.isAdminMode()) return;

        if (!profile.hasIsland()) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }

        if (!profile.getIsland().isInside(p.getLocation())) {
            e.setCancelled(true);
            //Messages.INTERACT_NOT_ON_OWN_ISLAND.send(profile);
            return;
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Location l = p.getLocation();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        if (l.getY() < ConfigEnum.MINIMUM_Y_LIMIT.getAsDouble()) {

            final Island island = IslandUtil.getIslandAtXZ(l.getBlockX(), l.getBlockZ());
            if (island == null)
                return;

            island.teleport(p);
            p.setFallDistance(0);
            p.setNoDamageTicks(100); // 5 SECONDS
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent e){
        final Block b = e.getBlock();
        final Location l = b.getLocation();

        if (!(b.getBlockData() instanceof Ageable) && !(b.getType().equals(Material.SUGAR_CANE)))
            return;

        if (!WorldUtil.isWorld(l.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {

                Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);

                if (island == null)
                    continue;

                // 2.5 is max
                final double multiplier = island.getUpgrades().getHarvest().get();

                //if (multiplier <= 1.0) continue;

                e.setCancelled(true);

                Ageable a = (Ageable) b.getBlockData();
                a.setAge(Math.min(
                        a.getAge() + (int)Math.ceil(multiplier * (multiplier == 1 ? 1 : 2)), // 2 : 3
                        a.getMaximumAge()));
                b.setBlockData(a);
                b.getState().update();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {

        if (!WorldUtil.isWorld(e.getEntity().getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final LivingEntity entity = e.getEntity();

        if (entity instanceof Phantom
                || entity instanceof Wither
                || entity instanceof EnderDragon) {
            e.setCancelled(true);
            return;
        }

        /*
        Handles island mob spawn amount upgrade
         */

        // todo update/fix this (outdated/hardcoded)

        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL
                || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {

            final Island island = IslandUtil.getIslandAtLocation(e.getLocation());

            if (island == null)
                return;

            final double multiplier = island.getUpgrades().getSpawnAmount().get();

            if (multiplier == 1.0) return;

            if (multiplier == 1.25) {

                int random = (int) (Math.random() * 101);
                int amount = random > 75 ? 2 : 1;

                for (int i = 0; i < amount; i++)
                    Skyblock.get().getIslandWorld().spawnEntity(entity.getLocation(), entity.getType());

            } else if (multiplier == 1.75) {

                int random = (int) (Math.random() * 101);
                int amount = random > 25 ? 2 : 1;

                for (int i = 0; i < amount; i++)
                    Skyblock.get().getIslandWorld().spawnEntity(entity.getLocation(), entity.getType());

            } else if (multiplier == 2.5) {

                int random = (int) (Math.random() * 101);
                int amount = random > 50 ? 3 : 2;

                for (int i = 0; i < amount; i++)
                    Skyblock.get().getIslandWorld().spawnEntity(entity.getLocation(), entity.getType());
            }
        }
    }

}
