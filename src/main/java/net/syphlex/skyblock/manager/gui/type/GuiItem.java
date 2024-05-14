package net.syphlex.skyblock.manager.gui.type;

import org.bukkit.inventory.ItemStack;

public class GuiItem {
    private final ItemStack item;
    private String command;
    private int cost;
    private final int slot;

    public GuiItem(ItemStack item, String command, int cost, int slot){
        this.item = item;
        this.command = command;
        this.cost = cost;
        this.slot = slot;
    }

    public GuiItem(ItemStack item, int slot){
        this.item = item;
        this.slot = slot;
    }

    public ItemStack item(){
        return this.item;
    }

    public String command(){
        return this.command;
    }

    public int cost(){
        return this.cost;
    }

    public boolean hasCmd(){
        return this.command.length() > 0;
    }

    public int slot(){
        return this.slot;
    }
}
