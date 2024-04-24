package net.syphlex.skyblock.handler.gui;

import net.syphlex.skyblock.profile.IslandProfile;
import net.syphlex.skyblock.util.SimpleGui;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler {

    private final Map<IslandProfile, SimpleGui> guicache = new HashMap<>();

    public void openGui(IslandProfile profile, SimpleGui gui){
        guicache.put(profile, gui);
        gui.openInventory(profile.getPlayer());
    }

    public boolean isInOurGui(IslandProfile profile, Inventory inventory){
        if (!guicache.containsKey(profile))
            return false;
        return guicache.get(profile).getInventory().getHolder() == inventory.getHolder();
    }

    public SimpleGui get(IslandProfile profile){
        return this.guicache.get(profile);
    }

    public void closeGui(IslandProfile profile){
        this.guicache.remove(profile);
    }
}
