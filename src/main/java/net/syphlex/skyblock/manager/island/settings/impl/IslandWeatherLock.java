package net.syphlex.skyblock.manager.island.settings.impl;

public enum IslandWeatherLock {
    NONE, RAINY, SUNNY, STORM;

    public static IslandWeatherLock of(String name){
        if (name == null) return NONE;
        return valueOf(name);
    }
}
