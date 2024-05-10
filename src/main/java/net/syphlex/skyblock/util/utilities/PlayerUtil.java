package net.syphlex.skyblock.util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class PlayerUtil {


    public void giveItem(Player p, ItemStack i){
        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItemNaturally(p.getLocation(), i);
            return;
        }
        p.getInventory().addItem(i);
        //p.updateInventory();
    }

    public ItemStack removeItem(ItemStack item, int amount) {
        ItemStack itemStack = item.clone();

        if (item.getAmount() <= amount) {
            itemStack = new ItemStack(Material.AIR);
        } else {
            itemStack.setAmount(item.getAmount() - amount);
        }

        return itemStack;
    }

    public static void removeItem(Player player, ItemStack item, int amount) {
        try {
            boolean found = false;

            if (player.getInventory().contains(item)) {
                if (item.getAmount() <= amount) {
                    player.getInventory().removeItem(item);
                } else {
                    item.setAmount(item.getAmount() - amount);
                }
                found = true;
            }

            if (!found) {
                //ItemStack offHand = Objects.requireNonNull(player.getEquipment()).getItemInOffHand();

                //if (offHand.isSimilar(item)) {
                //    if ((amount - offHand.getAmount()) >= 0) {
                //        player.getEquipment().setItemInOffHand(new ItemStack(Material.AIR, 1));
                //    } else {
                //        item.setAmount(offHand.getAmount() - amount);
                //    }
                //}
            }

        } catch (Exception e) {
        }
        //player.updateInventory();
    }

    public boolean isArmor(ItemStack item) {

        if (item == null
                || item.getType() == Material.AIR
                || item.getAmount() <= 0)
            return false;

        String name = item.getType().name().toLowerCase();
        return name.contains("helmet") || name.contains("shell")
                || name.contains("cap") || name.contains("tunic")
                || name.contains("chestplate") || name.contains("leggings")
                || name.contains("boots");
    }
}
