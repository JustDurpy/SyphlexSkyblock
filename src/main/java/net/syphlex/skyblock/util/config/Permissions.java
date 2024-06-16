package net.syphlex.skyblock.util.config;

import net.syphlex.skyblock.manager.profile.Profile;
import org.bukkit.entity.Player;

public enum Permissions {
    ADMIN("skyblock.admin"),
    MINE_ADMIN("skyblock.mine.admin"),
    NONE("none"),
    ISLAND_SETTINGS_BLUE_BORDER("skyblock.island.settings.border.blue"),
    ISLAND_SETTINGS_RED_BORDER("skyblock.island.settings.border.red"),
    ISLAND_SETTINGS_GREEN_BORDER("skyblock.island.settings.border.green"),
    ISLAND_SETTINGS_WEATHER_SUNNY("skyblock.island.settings.weather.sunny"),
    ISLAND_SETTINGS_WEATHER_RAINY("skyblock.island.settings.weather.rainy"),
    ISLAND_SETTINGS_TIME_SUNRISE("skyblock.island.settings.time.sunrise"),
    ISLAND_SETTINGS_TIME_DAY("skyblock.island.settings.time.day"),
    ISLAND_SETTINGS_TIME_SUNSET("skyblock.island.settings.time.sunset"),
    ISLAND_SETTINGS_TIME_NIGHT("skyblock.island.settings.time.night");

    private String permission;

    Permissions(String permission){
        this.permission = permission;
    }

    public String get(){
        return this.permission;
    }

    public void set(String permission){
        this.permission = permission;
    }

    public boolean has(Player p){
        if (this == NONE) return true;
        return p.hasPermission(this.permission);
    }

    public boolean has(Profile profile){
        return has(profile.getPlayer());
    }

    public static Permissions getPermEnum(String permission){
        for (Permissions perm : values()) {
            if (perm.get().equalsIgnoreCase(permission))
                return perm;
        }
        return NONE;
    }
}
