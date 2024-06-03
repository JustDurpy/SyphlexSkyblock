package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.PlayerUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    // todo drop exp bottle of players exp on death instead of just vanilla xp

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraftEvent(CraftItemEvent e) {
        final ItemStack result = e.getRecipe().getResult();
        if (result.getType() == Material.HOPPER)
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        final Block block = e.getBlock();
        final Location location = block.getLocation();

        if (!profile.hasIsland()) {
            if (Skyblock.get().getUpgradeHandler().isSpecialBlock(block.getType())) {
                e.setCancelled(true);
            }
            return;
        }

        if (!profile.getIsland().isInside(location)) {
            if (Skyblock.get().getUpgradeHandler().isSpecialBlock(block.getType())) {
                e.setCancelled(true);
            }
            return;
        }

        if (!Skyblock.get().getUpgradeHandler().isSpecialBlock(block.getType()))
            return;

        boolean wasSpecialBlockNear = false;

        for (int y = -4; y <= 4; y++) {
            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {

                    Location check = location.clone().add(x, y, z);

                    if (!profile.getIsland().isStoredBlock(check) || check.getBlock().getType() != block.getType()) {
                        continue;
                    }

                    e.setCancelled(true);

                    final ItemStack item = e.getItemInHand().clone();
                    item.setAmount(1);
                    p.getInventory().removeItem(item);

                    final IslandBlockData blockData = profile.getIsland().getStoredBlock(check);
                    blockData.setAmount(blockData.getAmount() + 1);

                    wasSpecialBlockNear = true;
                    //p.sendMessage("found stored special block and stored new value : " + blockData.getAmount());

                    blockData.getHologram().updateLine(0,
                            StringUtil.CC(blockData.getBlockData().getDisplayName()
                                    .replace("%amount%", String.valueOf(blockData.getAmount()))));
                    break;
                }
            }
        }

        if (!wasSpecialBlockNear) {

            //p.sendMessage("placed new stored block!");

            final SpecialBlockData data =  Skyblock.get().getUpgradeHandler().getSpecialBlockDataFromMaterial(block.getType());
            final IslandBlockData islandBlockData = new IslandBlockData(new Position(location), data, 1);

            profile.getIsland().getStoredBlocks().add(islandBlockData);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e){
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        final Block block = e.getBlock();
        final Location location = block.getLocation();

        if (!profile.hasIsland()) {

            if (Skyblock.get().getUpgradeHandler().isSpecialBlock(block.getType())) {
                e.setCancelled(true);
            }

            return;
        }

        if (!profile.getIsland().isInside(location)) {
            if (Skyblock.get().getUpgradeHandler().isSpecialBlock(block.getType())) {
                e.setCancelled(true);
            }
            return;
        }

        final IslandBlockData blockData = profile.getIsland().getStoredBlock(location);

        if (blockData == null)
            return;

        if (blockData.getAmount() > 1) {

            e.setCancelled(true);
            e.setExpToDrop(0);
            //e.setDropItems(false);
            if (p.getGameMode() == GameMode.SURVIVAL) {
                e.getBlock().getDrops().clear();
                block.getWorld().dropItemNaturally(location, new ItemStack(blockData.getBlockData().getMaterial(), 1));
            }
            blockData.setAmount(blockData.getAmount() - 1);

            blockData.getHologram().updateLine(0, blockData.getBlockData().getDisplayName()
                    .replace("%amount%", String.valueOf(blockData.getAmount())));

            //p.sendMessage("removed 1 stored block from island (" + blockData.getAmount() + ")");
            return;
        }

        blockData.getHologram().delete();
        //p.sendMessage("completely removed block data!");
        profile.getIsland().getStoredBlocks().remove(blockData);
    }
}
