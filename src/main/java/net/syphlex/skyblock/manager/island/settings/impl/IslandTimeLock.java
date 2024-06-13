package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.Permissions;
import org.bukkit.Material;

@Getter
public enum IslandTimeLock {
    NONE(0, ""),
    SUNRISE(1, Permissions.ISLAND_SETTINGS_TIME_SUNRISE.get()),
    DAY(2, Permissions.ISLAND_SETTINGS_TIME_DAY.get()),
    SUNSET(3, Permissions.ISLAND_SETTINGS_TIME_SUNSET.get()),
    NIGHT(4, Permissions.ISLAND_SETTINGS_TIME_NIGHT.get());

    private final int id;
    private final String permission;

    IslandTimeLock(int id, String permission){
        this.id = id;
        this.permission = permission;
    }

    public static IslandTimeLock of(String name){
        if (name == null) return NONE;
        return valueOf(name);
    }

    public static IslandTimeLock find(int id) {
        for (IslandTimeLock var : IslandTimeLock.values())
            if (id == var.getId()) return var;
        return NONE;
    }
}
