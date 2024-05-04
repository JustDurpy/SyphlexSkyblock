package net.syphlex.skyblock.util.config;

import lombok.Getter;
import net.syphlex.skyblock.manager.profile.IslandProfile;
import net.syphlex.skyblock.util.StringUtil;
import org.bukkit.entity.Player;

@Getter
public enum Messages {
    USAGE("&Error: Usage: /%usage%"),
    NO_PERMISSION("&cError: You do not have permission to issue this."),
    PLAYER_NOT_FOUND("&cError: That player is not online."),
    ISLAND_CREATE("&aYou have successfully created an island. (%time%ms)"),
    ISLAND_DELETE("&cYou have successfully deleted your island. (%time%ms)"),
    TELEPORTED_TO_ISLAND("&aYou have teleported to your island."),
    ISLAND_SETHOME("&aYou have set your island home."),
    ALREADY_HAS_ISLAND("&cError: You are already apart of an island."),
    DOES_NOT_HAVE_ISLAND("&cError: You do not have an island."),
    NOT_ISLAND_LEADER("&cError: You must be the leader of the island to issue this."),
    NO_ISLAND_PERMISSION("&cError: You must have a higher role in the island to issue this."),
    MUST_BE_AT_ISLAND("&cError: You must be at your island to issue this."),
    INTERACT_NOT_ON_OWN_ISLAND("&cError: You cannot do this here as this is not your island."),
    SENT_ISLAND_INVITE("&aYou have invited %player% to your island."),
    RECEIVED_ISLAND_INVITE("&aYou have received an invitation to join %player%'s island."),
    CANT_INVITE_MEMBERS("&cError: This player is already apart of your island."),
    ISLAND_INVITE_COOLDOWN("&cError: You must wait before sending another island invitation."),
    LEFT_ISLAND("&cYou have left your island."),
    LEADER_LEAVE_ISLAND("&cError: Leaders must either transfer island leadership or disband their island."),
    NO_INVITE_FROM_ISLAND("&cError: There was no invite from this island or the invite has expired."),
    WAIT_INVITE("&cError: You must wait before inviting this player again."),
    MOB_COIN_COLLECTED("&bYou &fhave collected a &bMob Coin &fthat was on the floor. &7(%mobcoins%)");

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

    public Messages replace(String s1, Object s2) {
        this.msg = this.msg.replace(s1, String.valueOf(s2));
        return this;
    }

    public Messages usage(String usage){
        this.msg = this.msg.replace("%usage%", usage);
        return this;
    }

    public void send(IslandProfile profile){
        profile.getPlayer().sendMessage(get());
    }
}
