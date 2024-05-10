package net.syphlex.skyblock.listener;

import de.tr7zw.nbtapi.NBTItem;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.event.ArmorEquipEvent;
import net.syphlex.skyblock.event.ArmorUnEquipEvent;
import net.syphlex.skyblock.event.StartItemHoldEvent;
import net.syphlex.skyblock.event.StopItemHoldEvent;
import net.syphlex.skyblock.manager.customenchant.Appliance;
import net.syphlex.skyblock.manager.customenchant.CustomEnchant;
import net.syphlex.skyblock.manager.customenchant.EnchantmentMethod;
import net.syphlex.skyblock.manager.customenchant.Trigger;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CustomEnchantListener implements Listener {

    public void handle(Player player, Event event, CustomEnchant enchant) {
        int size = player.getInventory().getArmorContents().length + 1;
        ItemStack[] arr = new ItemStack[size];
        arr[size - 1] = player.getInventory().getItemInMainHand();

        for (int i = 0; i < player.getInventory().getArmorContents().length; i++)
            arr[i] = player.getInventory().getArmorContents()[i];

        for (ItemStack i : arr) {
            if (Skyblock.get().getEnchantHandler().itemHasEnchant(i, enchant))
                enchant.onEvent(player, i, enchant, enchant.getLevel(i), event);
        }
    }

    public void handle(Player player, ItemStack item, Event event, CustomEnchant enchant) {
        if (Skyblock.get().getEnchantHandler().itemHasEnchant(item, enchant))
            enchant.onEvent(player, item, enchant, enchant.getLevel(item), event);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {

        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {

            if (enchant.getTriggers().contains(Trigger.DEFENSE)) {

                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (e.getDamager() != p) {
                        handle(p, e, enchant);
                    }
                }
            }

            if (enchant.getTriggers().contains(Trigger.ATTACK)) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    if (e.getEntity() != p) {
                        handle(p, e, enchant);
                    }
                }
            }

            if (enchant.getTriggers().contains(Trigger.SHOOT)) {
                if (e.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) e.getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        Player p = (Player) projectile.getShooter();
                        if (e.getEntity() != p)
                            handle(p, e, enchant);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
            if (e.getEntity() != null && e.getEntity().getKiller() != null) {
                if (enchant.getTriggers().contains(Trigger.KILL_ENTITY)) {
                    final Player p = (Player) e.getEntity().getKiller();
                    final LivingEntity living = (LivingEntity) e.getEntity();

                    handle(p, e, enchant);

                    //if ((living.getHealth() - e.getFinalDamage()) <= 0)
                    //    handle(p, e, enchant);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        final Player p = e.getEntity();
        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
            if (enchant.getTriggers().contains(Trigger.DEATH))
                handle(p, e, enchant);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        final Player p = e.getPlayer();
        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
            if (enchant.getTriggers().contains(Trigger.BROKEN_BLOCK))
                handle(p, e, enchant);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e){
        final Player p = e.getPlayer();
        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
            if (enchant.getTriggers().contains(Trigger.MINING))
                handle(p, e, enchant);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){

        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();

            for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
                if (enchant.getTriggers().contains(Trigger.DEFENSE)) {
                    handle(p, e, enchant);
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e){

        if (e.getWhoClicked() instanceof Player) {

            Player p = (Player) e.getWhoClicked();

            ItemStack cursor = e.getCursor(); // armor piece
            ItemStack item = e.getCurrentItem(); // item being swapped

            if (e.getClick() != ClickType.LEFT)
                return;

            if (item == null
                    || item.getType() == Material.AIR
                    || cursor == null
                    || cursor.getType() == Material.AIR) {
                return;
            }

            if (Skyblock.get().getEnchantHandler().isShard(item)) {
                if (Skyblock.get().getEnchantHandler().isAngelDust(cursor)) {
                    if (Skyblock.get().getEnchantHandler().applyAngelDust(p, cursor, item)) {
                        p.setItemOnCursor(PlayerUtil.removeItem(cursor, 1));
                    } else {
                        p.setItemOnCursor(cursor);
                    }
                    e.setCancelled(true);
                    //p.updateInventory();
                }
                return;
            }

            if (Skyblock.get().getEnchantHandler().isShard(cursor)) {

                Pair<CustomEnchant, Integer> shard = Skyblock.get().getEnchantHandler().getShardInfo(cursor);
                if (Skyblock.get().getEnchantHandler().enchant(p, p.getItemOnCursor(),
                        e.getCurrentItem(), shard.getX())){
                    p.setItemOnCursor(PlayerUtil.removeItem(cursor, 1));
                } else {
                    p.setItemOnCursor(cursor);
                }
                //p.updateInventory();
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e){

        final Player p = e.getPlayer();

        if (e instanceof LivingEntity) {
            for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
                if (enchant.getTriggers().contains(Trigger.INTERACT_ENTITY))
                    handle(p, e, enchant);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        final Player p = e.getPlayer();
        final Action a = e.getAction();

        if (e.useInteractedBlock().equals(Event.Result.DENY))
            return;

        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {

            for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
                if (enchant.getTriggers().contains(Trigger.RIGHT_CLICK))
                    handle(p, e, enchant);
            }

            ItemStack item = e.getItem();

            if (item == null || item.getType() == Material.AIR)
                return;

            if (Skyblock.get().getEnchantHandler().isRune(item)) {
                NBTItem nbt = new NBTItem(item);
                nbt.clearNBT();
                nbt.clearCustomNBT();
                PlayerUtil.removeItem(p, e.getItem(), 1);
                Skyblock.get().getEnchantHandler().giveShard(p);
            }

        } else if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
            for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {
                if (enchant.getTriggers().contains(Trigger.LEFT_CLICK))
                    handle(p, e, enchant);
            }
        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e){

        final Player p = e.getPlayer();
        final ItemStack item = e.getArmorPiece();

        for (CustomEnchant enchant : Skyblock.get().getEnchantHandler().getEnchants()) {

            if (!Skyblock.get().getEnchantHandler().itemHasEnchant(item, enchant))
                continue;

            if (enchant.getTriggers().contains(Trigger.STATIC))
                handle(p, item, e, enchant);
        }
    }

    @EventHandler
    public void onArmorUnEquip(ArmorUnEquipEvent e){

        final Player p = e.getPlayer();
        final ItemStack item = e.getArmorPiece();

        if (!Skyblock.get().getEnchantHandler().hasEnchants(item))
            return;

        final List<Pair<CustomEnchant, Integer>> enchantments = Skyblock.get().getEnchantHandler().getEnchantmentsOnItem(item);

        for (Pair<CustomEnchant, Integer> pair : enchantments) {

            final CustomEnchant enchant = pair.getX();

            if (!Skyblock.get().getEnchantHandler().itemHasEnchant(item, enchant))
                continue;

            for (EnchantmentMethod method : enchant.getMethods()) {

                for (String effect : method.getEffects()) {

                    String[] args = effect.split(":");

                    if (args[0].toLowerCase().equalsIgnoreCase("potion")) {

                        PotionEffectType type = PotionEffectType.getByName(args[2]);

                        if (type == null) {
                            Skyblock.log("ERROR: INVALID POTION EFFECT NAME FOR " + enchant.getName());
                            continue;
                        }

                        p.removePotionEffect(type);
                    }
                }
            }
        }
    }
}