package net.syphlex.skyblock.listener;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

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
                .replace("%mobcoins%", profile.getMobCoins())
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

                    if (!profile.getIsland().isStoredBlock(check)) {
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

}
