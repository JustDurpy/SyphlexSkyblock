package net.syphlex.skyblock.manager.gui;

import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler {

    private final Map<Profile, SimpleGui> guicache = new HashMap<>();

    public void openGui(Profile profile, SimpleGui gui){
        guicache.put(profile, gui);
        gui.openInventory(profile.getPlayer());
    }

    public boolean isInOurGui(Profile profile, Inventory inventory){
        if (!guicache.containsKey(profile))
            return false;
        return guicache.get(profile).getInventory().getHolder() == inventory.getHolder();
    }

    public SimpleGui get(Profile profile){
        return this.guicache.get(profile);
    }

    public void closeGui(Profile profile){
        this.guicache.remove(profile);
    }
}
