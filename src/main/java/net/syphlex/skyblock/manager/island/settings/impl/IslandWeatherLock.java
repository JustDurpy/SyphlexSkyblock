package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.Getter;
import net.syphlex.skyblock.util.config.Permissions;

@Getter
public enum IslandWeatherLock {
    NONE(0, ""),
    SUNNY(1, Permissions.ISLAND_SETTINGS_WEATHER_SUNNY.get()),
    RAINY(2, Permissions.ISLAND_SETTINGS_WEATHER_RAINY.get());

    private final int id;
    private final String permission;

    IslandWeatherLock(int id, String permission){
        this.id = id;
        this.permission = permission;
    }

    public static IslandWeatherLock of(String name){
        if (name == null) return NONE;
        return valueOf(name);
    }

    public static IslandWeatherLock find(int id){
        for (IslandWeatherLock var : IslandWeatherLock.values())
            if (id == var.getId()) return var;
        return NONE;
    }
}
