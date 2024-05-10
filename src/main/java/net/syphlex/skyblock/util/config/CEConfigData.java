package net.syphlex.skyblock.util.config;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class CEConfigData {

    public ItemStack runeItem;
    public ItemStack shardItem;
    public ItemStack angelDust;
    public ItemStack whiteScrollItem;

    public int angelDustMax;
    public int angelDustMin;

    public String enchanterGuiTitle;

    public int runeCost;

}
