package net.syphlex.skyblock.manager.gui.type;

import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiItem {
    private ItemBuilder item;
    private List<Pair<String, ItemBuilder>> multipleItems = null;
    private String command, stringId;
    private int id;
    private final int slot;

    public GuiItem(ItemBuilder item, String command, int id, int slot){
        this.item = item;
        this.command = command;
        this.id = id;
        this.slot = slot;
    }

    public GuiItem(List<Pair<String, ItemBuilder>> multipleItems, String stringId, String command, int id, int slot){
        this.multipleItems = multipleItems;
        this.stringId = stringId;
        this.command = command;
        this.id = id;
        this.slot = slot;
    }

    public GuiItem(List<Pair<String, ItemBuilder>> multipleItems, String stringId, int slot){
        this.multipleItems = multipleItems;
        this.stringId = stringId;
        this.slot = slot;
    }

    public GuiItem(ItemBuilder item, int id, int slot){
        this.item = item;
        this.id = id;
        this.slot = slot;
    }

    public GuiItem(ItemBuilder item, String stringId, int slot){
        this.item = item;
        this.slot = slot;
    }

    public GuiItem(ItemBuilder item, int slot){
        this.item = item;
        this.slot = slot;
    }

    public ItemStack item(){
        if (this.item == null) return new ItemStack(Material.AIR);
        return this.item.build();
    }

    public ItemBuilder builder(){
        return this.item;
    }

    public String command(){
        if (this.command == null) return "none";
        return this.command;
    }

    //public int cost(){
     //   return this.cost;
   // }

    public List<Pair<String, ItemBuilder>> items(){
        return this.multipleItems;
    }

    public int id(){
        return this.id;
    }

    public String stringId() { return this.stringId; }

    public boolean hasMultipleItems(){
        return this.multipleItems != null;
    }

    public boolean hasCmd(){
        return this.command != null && this.command.length() > 0;
    }

    public int slot(){
        return this.slot;
    }
}
