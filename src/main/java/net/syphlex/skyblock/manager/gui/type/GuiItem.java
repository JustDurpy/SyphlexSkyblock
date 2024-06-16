package net.syphlex.skyblock.manager.gui.type;

import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiItem {
    private ItemBuilder item;
    private List<Pair<String, ItemBuilder>> multipleItems = null;
    private String command = "none", stringId, permission = "none";
    private int id = 0, slot = 0;

    public GuiItem(ItemBuilder item){
        this.item = item;
    }

    public GuiItem(List<Pair<String, ItemBuilder>> multipleItems){
        this.multipleItems = multipleItems;
    }

    public GuiItem setSlot(int slot){
        this.slot = slot;
        return this;
    }

    public GuiItem setId(int id){
        this.id = id;
        return this;
    }

    public GuiItem setStringId(String stringId){
        this.stringId = stringId;
        return this;
    }

    public GuiItem setCmd(String cmd){
        this.command = cmd;
        return this;
    }

    public GuiItem setPerm(String permission){
        this.permission = permission;
        return this;
    }

    /*
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
        this.stringId = stringId;
    }

    public GuiItem(ItemBuilder item, String stringId, String permission, int slot){
        this.item = item;
        this.stringId = stringId;
        this.permission = permission;
        this.slot = slot;
    }

    public GuiItem(ItemBuilder item, int slot){
        this.item = item;
        this.slot = slot;
    }

     */

    public ItemStack getItem(){
        if (this.item == null) return new ItemStack(Material.AIR);
        return this.item.build();
    }

    public ItemBuilder getBuilder(){
        return this.item;
    }

    public String getCommand(){
        if (this.command == null) return "none";
        return this.command;
    }

    public List<Pair<String, ItemBuilder>> getItems(){
        return this.multipleItems;
    }

    public int getId(){
        return this.id;
    }

    public String getStringId() { return this.stringId; }

    public String getPermission(){
        return this.permission;
    }

    public boolean hasMultipleItems(){
        return this.multipleItems != null;
    }

    public boolean hasCmd(){
        return this.command != null && this.command.length() > 0 && !this.command.equals("none");
    }

    public int getSlot(){
        return this.slot;
    }
}
