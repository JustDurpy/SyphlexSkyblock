package net.syphlex.skyblock.util.simple;

import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public abstract class SimpleGui implements InventoryHolder {
    public final Inventory inventory;
    private final ArrayList<GuiItem> guiItems = new ArrayList<>();

    public SimpleGui(String title, int size){
        this.inventory = Bukkit.createInventory(this, size, StringUtil.CC(title));
    }

    public abstract void onClickEvent(ClickEvent e);

    public void onCloseEvent(){}

    public void openInventory(Player player){
        player.openInventory(this.inventory);
    }

    public void closeInventory(Player player){
        player.closeInventory();
    }

    public void setItem(ItemStack i, int slot){
        this.inventory.setItem(slot, i);
    }

    public void fill(ItemStack item){
        for (int i = 0; i < this.inventory.getSize(); i++){
            setItem(item, i);
        }
    }

    @Getter private int islandPanelButtonSlot = -1;

    public void setIslandPanelButton(int slot){

        this.islandPanelButtonSlot = slot;

        this.inventory.setItem(slot, new ItemBuilder()
                .setMaterial(Material.END_PORTAL_FRAME)
                .setName(StringUtil.parseHex("Island Panel", "#BC77EC"))
                .setLore(Arrays.asList(
                        "",
                        "&fClick to open your island panel.",
                        ""))
                .build());
    }

}
