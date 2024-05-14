package net.syphlex.skyblock.util;

import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private Material material;
    private boolean unbreakable = false;
    private boolean glowing = false;
    private int amount = 1;
    private final ArrayList<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();
    private final ArrayList<ItemFlag> itemFlagsToAdd = new ArrayList<>();
    private final ArrayList<ItemFlag> itemFlagsToRemove = new ArrayList<>();

    public ItemBuilder setName(String name){
        this.displayName = name;
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public ItemBuilder setMaterial(Material material){
        this.material = material;
        return this;
    }

    public ItemBuilder setAmount(int i){
        this.amount = i;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable){
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing){
        this.glowing = glowing;
        return this;
    }

    public int getAmount(){
        return this.amount;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment){
        this.enchantments.removeIf(pair -> pair.getX() == enchantment);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level){
        this.enchantments.add(new Pair<>(enchantment, level));
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag){
        this.itemFlagsToAdd.add(flag);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag flag){
        this.itemFlagsToRemove.add(flag);
        return this;
    }

    public ItemStack build(){
        ItemStack item = new ItemStack(this.material, this.amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return null;
        if (!this.enchantments.isEmpty()) {
            for (Pair<Enchantment, Integer> pair : this.enchantments) {
                meta.addEnchant(pair.getX(), pair.getY(), true);
            }
        }
        meta.setDisplayName(StringUtil.CC(this.displayName == null ? this.material.name() : this.displayName));
        meta.setLore(StringUtil.CC(this.lore));
        meta.setUnbreakable(this.unbreakable);

        if (this.glowing) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (this.itemFlagsToAdd.size() > 0) {
            for (ItemFlag flag : this.itemFlagsToAdd)
                meta.addItemFlags(flag);
        }

        if (this.itemFlagsToRemove.size() > 0) {
            for (ItemFlag flag : this.itemFlagsToRemove) {
                if (!meta.hasItemFlag(flag)) continue;
                meta.removeItemFlags(flag);
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}
