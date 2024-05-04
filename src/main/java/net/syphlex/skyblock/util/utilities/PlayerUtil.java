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
}
