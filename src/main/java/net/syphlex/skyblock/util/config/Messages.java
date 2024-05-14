package net.syphlex.skyblock.util.config;

import lombok.Getter;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.entity.Player;

@Getter
public enum Messages {
    USAGE("&cUsage: /%usage%"),
    NO_PERMISSION("&cError: You do not have permission to issue this."),
    PLAYER_NOT_FOUND("&cError: That player is not online."),
    NO_RECORD_OF_PLAYER("&cError: There was no record of that player found."),
    ISLAND_CREATE("&aYou have successfully created an island. (%time%ms)"),
    ISLAND_DELETE("&cYou have successfully deleted your island. (%time%ms)"),
    TELEPORTED_TO_ISLAND("&aYou have teleported to your island."),
    ISLAND_SETHOME("&aYou have set your island home."),
    ALREADY_HAS_ISLAND("&cError: You are already apart of an island."),
    DOES_NOT_HAVE_ISLAND("&cError: You do not have an island."),
    NOT_APART_OF_ISLAND("&cError: That player is not apart of your island."),
    ISLAND_NOT_FOUND("&cError: That island was not found."),
    NOT_ISLAND_LEADER("&cError: You must be the leader of the island to issue this."),
    NO_ISLAND_PERMISSION("&cError: You must have a higher role in the island to issue this."),
    MUST_BE_AT_ISLAND("&cError: You must be at your island to issue this."),
    INTERACT_NOT_ON_OWN_ISLAND("&cError: You cannot do this here as this is not your island."),
    SENT_ISLAND_INVITE("&aYou have invited %player% to your island."),
    RECEIVED_ISLAND_INVITE("&aYou have received an invitation to join %player%'s island."),
    CANT_INVITE_MEMBERS("&cError: This player is already apart of your island."),
    ISLAND_INVITE_COOLDOWN("&cError: You must wait before sending another island invitation."),
    LEFT_ISLAND("&cYou have left your island."),
    KICKED_FROM_ISLAND("&cYou were kicked from your island by %player%."),
    KICK_MEMBER("&cYou have kicked %member%. from your island."),
    KICK_MEMBER_BROADCAST("&c[!] %member% was kicked from the island by %player%."),
    JOIN_ISLAND("&aYou have joined %player%'s island."),
    JOIN_ISLAND_BROADCAST("&a[!] %player% has joined the island."),
    LEADER_LEAVE_ISLAND("&cError: Leaders must either transfer island leadership or disband their island."),
    NO_INVITE_FROM_ISLAND("&cError: There was no invite from this island or the invite has expired."),
    WAIT_INVITE("&cError: You must wait before inviting this player again."),
    ISLAND_UPGRADE_INSUFFICIENT_FUNDS("&cError: You do not have enough money to purchase this upgrade."),
    ISLAND_UPGRADE_SUCCESS("&aYou have successfully upgraded your island!"),
    MOB_COIN_COLLECTED("&6&l(!) &6You &ehave collected a &6Mob Coin &ethat was on the floor. &7(%mobcoins%⛁)"),
    DISPLAY_MOB_COINS("&6&l(!) &6%player% &ehas &6%mobcoins%⛁ &emob coins."),
    CALCULATE_ISLAND_WORTH("&7Recalculating all island data..."),
    CALCULATE_ISLAND_WORTH_COMPLETE("\n&a&lSUCCESS! &7Recalculated all island data!\n"),
    FEATURE_DISABLED("&cError: That feature is currently disabled.");

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
        //this.msg = this.msg.replace(s1, String.valueOf(s2));
        try {
            String msg = this.msg.replace(s1, String.valueOf(s2));
            Messages message = (Messages) this.clone();
            message.set(msg);
            return message;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Messages usage(String usage) {
        //this.msg = this.msg.replace("%usage%", usage);
        try {
            String msg = this.msg.replace("%usage%", usage);
            Messages message = (Messages) this.clone();
            message.set(msg);
            return message;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void send(Profile profile){
        profile.getPlayer().sendMessage(get());
    }
}
