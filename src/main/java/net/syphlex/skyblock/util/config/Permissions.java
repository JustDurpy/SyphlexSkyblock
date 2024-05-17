package net.syphlex.skyblock.util.config;

import net.syphlex.skyblock.manager.profile.Profile;
import org.bukkit.entity.Player;

public enum Permissions {
    ADMIN("skyblock.admin"),
    MINE_ADMIN("skyblock.mine.admin");

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
