package net.syphlex.skyblock.listener;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.event.ArmorEquipEvent;
import net.syphlex.skyblock.event.ArmorUnEquipEvent;
import net.syphlex.skyblock.event.StartItemHoldEvent;
import net.syphlex.skyblock.event.StopItemHoldEvent;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeathEvent(EntityDeathEvent e){

        if (e.getEntity() instanceof Player)
            return;

        Skyblock.get().getMobCoinHandler().handleEntityDeath(e.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickUp(EntityPickupItemEvent e) {

        final ItemStack i = e.getItem().getItemStack();

        if (!Skyblock.get().getMobCoinHandler().isMobCoin(i))
            return;

        if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
            return;
        }

        final Player p = (Player) e.getEntity();
        final Profile profile = Skyblock.get().getDataHandler().get(p);

        e.getItem().remove();
        profile.setMobCoins(profile.getMobCoins() + 1);

        Messages.MOB_COIN_COLLECTED
                .replace("%mobcoins%", String.format("%,d", profile.getMobCoins()))
                .send(profile);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland())
            return;

        final Block block = e.getBlock();
        final Location location = block.getLocation();

        if (!profile.getIsland().isInside(location))
            return;

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

                    Hologram hologram = DHAPI.getHologram(
                            profile.getIsland().getIdentifier().replace(";", "-")
                            + blockData.getBlockData().getMaterial().name());

                    if (hologram == null)
                        break;

                    DHAPI.setHologramLine(hologram, 2, StringUtil.CC(blockData.getBlockData().getDisplayName()
                            + ": &6&lx" + blockData.getAmount()));
                    break;
                }
            }
        }

        if (!wasSpecialBlockNear) {

            //p.sendMessage("placed new stored block!");

            SpecialBlockData data =  Skyblock.get().getUpgradeHandler().getSpecialBlockDataFromMaterial(block.getType());

            profile.getIsland().getStoredBlocks().add(
                    new IslandBlockData(new Position(location), data, 1));

            DHAPI.createHologram(profile.getIsland().getIdentifier().replace(";", "-")
                            + block.getType().name(), location.clone().add(0.5, 3, 0.5), true);

            Hologram hologram = DHAPI.getHologram(
                    profile.getIsland().getIdentifier().replace(";", "-")
                    + block.getType().name());

            if (hologram == null)
                return;

            DHAPI.addHologramLine(hologram, new ItemStack(block.getType()));
            DHAPI.addHologramLine(hologram, "");
            DHAPI.addHologramLine(hologram, StringUtil.CC(data.getDisplayName() + ": &6&lx1"));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e){
        final Player p = e.getPlayer();

        if (!WorldUtil.isWorld(p.getWorld(), Skyblock.get().getIslandWorld()))
            return;

        final Profile profile = Skyblock.get().getDataHandler().get(p);

        if (!profile.hasIsland())
            return;

        final Block block = e.getBlock();
        final Location location = block.getLocation();

        if (!profile.getIsland().isInside(location))
            return;

        final IslandBlockData blockData = profile.getIsland().getStoredBlock(location);

        if (blockData == null)
            return;

        if (blockData.getAmount() > 1) {

            e.setCancelled(true);
            e.setExpToDrop(0);
            e.setDropItems(false);
            if (p.getGameMode() == GameMode.SURVIVAL) {
                p.getWorld().dropItemNaturally(location,
                        new ItemStack(blockData.getBlockData().getMaterial(), 1));
            }
            blockData.setAmount(blockData.getAmount() - 1);

            Hologram hologram = DHAPI.getHologram(profile.getIsland().getIdentifier()
                    .replace(";", "-")
                    + blockData.getBlockData().getMaterial().name());

            if (hologram == null)
                return;

            DHAPI.setHologramLine(hologram, 2, StringUtil.CC(blockData.getBlockData().getDisplayName()
                    + ": &6&lx" + blockData.getAmount()));

            //p.sendMessage("removed 1 stored block from island (" + blockData.getAmount() + ")");
            return;
        }

        DHAPI.removeHologram(profile.getIsland().getIdentifier()
                .replace(";", "-") + blockData.getBlockData().getMaterial().name());
        //p.sendMessage("completely removed block data!");
        profile.getIsland().getStoredBlocks().remove(blockData);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractEvent(PlayerInteractEvent e) {

        if (e.useItemInHand().equals(Event.Result.DENY))
            return;

        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack itemStack = e.getItem();

        if (itemStack == null
                || itemStack.getType() == Material.AIR
                || itemStack.getAmount() <= 0
                || !PlayerUtil.isArmor(itemStack))
            return;


        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, itemStack));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {

                ItemStack armorPiece = p.getItemInHand();

                if (armorPiece.getType() == Material.AIR
                        || armorPiece.getAmount() <= 0)
                    return;

                Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, p.getItemInHand()));
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {

        final int newSlot = e.getNewSlot();
        final int previousSlot = e.getPreviousSlot();

        final Player p = e.getPlayer();
        final ItemStack item = p.getInventory().getItem(newSlot);
        final ItemStack lastItem = p.getInventory().getItem(previousSlot);

        if (item != null
                && item.getAmount() > 0
                && item.getType() != Material.AIR
                && newSlot != previousSlot)
            Bukkit.getPluginManager().callEvent(new StartItemHoldEvent(p, item));

        if (lastItem != null
                && lastItem.getAmount() > 0
                && lastItem.getType() != Material.AIR
                && newSlot != previousSlot)
            Bukkit.getPluginManager().callEvent(new StopItemHoldEvent(p, item));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropItemEvent(PlayerDropItemEvent e){

        final Player p = e.getPlayer();
        final ItemStack item = e.getItemDrop().getItemStack();

        if (item.getAmount() > 0 && item.getType() != Material.AIR)
            Bukkit.getPluginManager().callEvent(new StopItemHoldEvent(p, item));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player))
            return;

        final Player p = (Player) e.getWhoClicked();
        final int slot = e.getSlot();

        /*
        When unequipping armor the e.getCurrentItem() is the item being unequipped
        When equipping armor the e.getCursor() is equipped
         */

        final ItemStack currentItem = e.getCurrentItem();
        final ItemStack cursorItem = e.getCursor();

        // equipping armor
        if (e.isShiftClick() && (slot < 36 || slot > 39)) {

            if (!PlayerUtil.isArmor(currentItem))
                return;

            Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, currentItem));
            return;
        }

        if (slot < 36 || slot > 39)
            return;

        // unequipping armor
        if (e.isShiftClick()) {

            if (!PlayerUtil.isArmor(currentItem))
                return;

            Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, currentItem));
            return;
        }

        if (currentItem != null && cursorItem != null) {

            // swapped armor in inventory

            if (PlayerUtil.isArmor(currentItem) && PlayerUtil.isArmor(cursorItem)) {
                Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, currentItem));
                Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, cursorItem));
            } else if (!PlayerUtil.isArmor(currentItem) && PlayerUtil.isArmor(cursorItem)) {
                Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, cursorItem));
            } else if (PlayerUtil.isArmor(currentItem) && !PlayerUtil.isArmor(cursorItem)) {
                Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, currentItem));
            }

            /*

            OLD:

            if (currentItem.getType() != Material.AIR && cursorItem.getType() != Material.AIR) {
                Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, currentItem));
                Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, cursorItem));
            } else if (currentItem.getType() == Material.AIR && cursorItem.getType() != Material.AIR) {
                Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, cursorItem));
            } else if (currentItem.getType() != Material.AIR && cursorItem.getType() == Material.AIR) {
                Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(p, currentItem));
            }

             */
        }
    }
}
