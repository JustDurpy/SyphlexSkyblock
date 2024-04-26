package net.syphlex.skyblock.util.config;

import lombok.Getter;
import net.syphlex.skyblock.handler.profile.IslandProfile;
import net.syphlex.skyblock.util.StringUtil;
import org.bukkit.entity.Player;

@Getter
public enum Messages {
    NO_PERMISSION("&cError: You do not have permission to issue this."),
    ISLAND_CREATE("&aYou have successfully created an island. (%time%ms)"),
    ISLAND_DELETE("&cYou have successfully deleted your island. (%time%ms)"),
    TELEPORTED_TO_ISLAND("&aYou have teleported to your island."),
    ISLAND_SETHOME("&aYou have set your island home."),
    ALREADY_HAS_ISLAND("&cError: You are already apart of an island."),
    DOES_NOT_HAVE_ISLAND("&cError: You do not have an island."),
    NOT_ISLAND_LEADER("&cError: You must be the leader of the island to issue this."),
    NO_ISLAND_PERMISSION("&cError: You must have a higher role in the island to issue this."),
    MUST_BE_AT_ISLAND("&cError: You must be at your island to issue this."),
    INTERACT_NOT_ON_OWN_ISLAND("&cError: You cannot do this here as this is not your island.");

    private String msg;

    Messages(String msg){
        this.msg = msg;
    }

    public String get(){
        return StringUtil.CC(this.msg);
    }

    public void set(String msg){
        this.msg = msg;
    }

    public void send(Player p){
        p.sendMessage(get());
    }

    public void send(IslandProfile profile){
        profile.getPlayer().sendMessage(get());
    }
}
