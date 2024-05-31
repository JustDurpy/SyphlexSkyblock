package net.syphlex.skyblock.util.config;

import net.syphlex.skyblock.manager.profile.Profile;
import org.bukkit.entity.Player;

public enum Permissions {
    ADMIN("skyblock.admin"),
    MINE_ADMIN("skyblock.mine.admin"),
    ISLAND_SETTINGS_ALWAYS_DAY("skyblock.island.settings.alwaysday"),
    ISLAND_SETTINGS_ALWAYS_NIGHT("skyblock.island.settings.alwaysnight"),
    ISLAND_SETTINGS_BLUE_BORDER("skyblock.island.settings.blueborder"),
    ISLAND_SETTINGS_RED_BORDER("skyblock.island.settings.redborder"),
    ISLAND_SETTINGS_GREEN_BORDER("skyblock.island.settings.greenborder");

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
        return p.hasPermission(this.permission);
    }

    public boolean has(Profile profile){
        return profile.getPlayer().hasPermission(this.permission);
    }
}
