package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandBorderColorGui;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.Permissions;
import org.bukkit.Color;
import org.bukkit.Material;

@Getter
public enum IslandBorderColor {
    BLUE(0, Permissions.ISLAND_SETTINGS_BLUE_BORDER.get()),
    GREEN(1, Permissions.ISLAND_SETTINGS_RED_BORDER.get()),
    RED(2, Permissions.ISLAND_SETTINGS_GREEN_BORDER.get());

    private final int id;
    private final String permission;

    IslandBorderColor(int id, String permission){
        this.id = id;
        this.permission = permission;
    }

    public static IslandBorderColor of(String identifier){
        if (identifier == null) return BLUE;
        return valueOf(identifier);
    }

    public static IslandBorderColor find(int id){
        for (IslandBorderColor var : IslandBorderColor.values())
            if (var.getId() == id) return BLUE;
        return BLUE;
    }
}
