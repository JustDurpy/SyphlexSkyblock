package net.syphlex.skyblock.listener;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.minion.ManageMinionGui;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.PlayerUtil;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MinionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {

        if (e.getEntity() == null
                || !(e.getEntity() instanceof ArmorStand))
            return;

        for (Minion m : Skyblock.get().getMinionHandler().getMinions()) {
            if (m.getArmorStand().getEntityId() == e.getEntity().getEntityId()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {

        Player p = e.getPlayer();
        Profile profile = Skyblock.get().getDataHandler().get(p);
        Entity entity = e.getRightClicked();

        if (profile.getAttachingChest() != null)
            profile.setAttachingChest(null);

        if (!(entity instanceof ArmorStand))
            return;

        for (Minion m : Skyblock.get().getMinionHandler().getMinions()) {
            if (m.getArmorStand().getEntityId() == entity.getEntityId()) {

                e.setCancelled(true);

                //if (!m.getOwner().equals(p.getUniqueId())) {
                //    p.sendMessage(StringUtil.CC("That minion does not belong to you"));
                //    return;
                //}

                Skyblock.get().getGuiHandler().openGui(profile, new ManageMinionGui(m));
                //new ManageMinionGui(m).open(e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent e){

        if (e.getEntity() == null
                || !(e.getEntity() instanceof ArmorStand))
            return;

        for (Minion m : Skyblock.get().getMinionHandler().getMinions()) {
            if (m.getArmorStand().getEntityId() == e.getEntity().getEntityId())
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent e) {

        if (e.getEntity() == null
                || !PluginUtil.isMob(e.getEntity()))
            return;

        LivingEntity entity = e.getEntity();
        Location l = entity.getLocation();

        for (Minion m : Skyblock.get().getMinionHandler().getMinions()) {

            //if (!(m instanceof SlayerMinion)
            //        || !(m.hasChest()))
            //    continue;

            if (!WorldUtil.isWorld(l.getWorld(), m.getPosition().getWorld()))
                continue;

            if (l.distance(m.getPosition().getAsBukkit()) >= m.getData().getRadius() * 3)
                continue;

            for (ItemStack i : e.getDrops()) {
                Item item = l.getWorld().dropItem(l, i);
                m.getDrops().add(item);
                item.remove();
            }
            e.getDrops().clear();
            m.sendItem();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e){

        Player p = e.getPlayer();
        Profile profile = Skyblock.get().getDataHandler().get(p);
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = p.getItemInHand();

        if (!Skyblock.get().getMinionHandler().isMinionEgg(item)) {
            if (profile.getAttachingChest() != null) {
                if (e.getClickedBlock().getState() instanceof Chest
                        || e.getClickedBlock().getState() instanceof DoubleChest) {
                    p.sendMessage(StringUtil.CC("&aSuccessfully attached a chest."));
                    profile.getAttachingChest().attachChest(e.getClickedBlock().getLocation());
                } else {
                    p.sendMessage(StringUtil.CC("&cThat block is not a chest."));
                }
                profile.setAttachingChest(null);
                e.setCancelled(true);
            }
            return;
        }

        Location placed = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);

        for (Minion m : Skyblock.get().getMinionHandler().getMinions()) {

            if (!WorldUtil.isWorld(m.getPosition().getWorld(), placed.getWorld()))
                continue;

            if (m.getPosition().getAsBukkit().distance(placed) < m.getData().getRadius() * 3) {
                p.sendMessage("This minion is too close to another minion!");
                e.setCancelled(true);
                return;
            }
        }

        placed.setYaw(p.getLocation().getYaw());
        placed.setPitch(p.getLocation().getPitch());
        Minion.Type type = Skyblock.get().getMinionHandler().getTypeFromEgg(item);

        if (type == Minion.Type.MINER) {
            for (int x = -1; x <= 1; x += 1) {
                for (int z = -1; z <= 1; z += 1) {
                    if (placed.clone().add(x, 0, z).getBlock().getType() != Material.AIR) {
                        p.sendMessage(StringUtil.CC("&cTo place a miner minion you must clear a 3x3 area."));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        Skyblock.get().getMinionHandler().create(p, item, placed, type);
        p.setItemInHand(PlayerUtil.removeItem(item, 1));
        //p.updateInventory();
        e.setCancelled(true);
    }
}
