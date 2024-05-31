package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import org.bukkit.Material;

@Getter
public enum IslandTimeLock {
    NONE(new GuiItem(new ItemBuilder()
            .setMaterial(Material.BARRIER)
            .setName("&cNone")
            .build(), 11)),
    DAY(new GuiItem(new ItemBuilder()
            .setMaterial(Material.SUNFLOWER)
            .setName("&eDay")
            .build(), 13)),
    NIGHT(new GuiItem(new ItemBuilder()
            .setMaterial(Material.BLACK_DYE)
            .setName("&8Night")
            .build(), 15));

    private final GuiItem guiItem;

    IslandTimeLock(GuiItem guiItem){
        this.guiItem = guiItem;
    }

    public static IslandTimeLock of(String name){
        if (name == null) return NONE;
        return valueOf(name);
    }
}
