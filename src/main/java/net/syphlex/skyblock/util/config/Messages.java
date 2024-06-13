package net.syphlex.skyblock.util.config;

import lombok.Getter;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.entity.Player;

@Getter
public enum Messages {
    USAGE("&cUsage: /%usage%"),
    NO_PERMISSION("&cError: You do not have permission to issue this."),
    RANK_REQUIRED("&cError: It seems that your rank does not have permission for this. You may purchase a rank for perks and more at &estore.syphlex.net&c."),
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
    BAN_VISITOR("&aYou have banned %player% from visiting your island."),
    BAN_VISITOR_BROADCAST("&c[!] %visitor% was banned from visiting the island by %player%."),
    BANNED_FROM_ISLAND("&cError: You are banned from visiting this island."),
    CANT_BAN_MEMBER_OF_ISLAND("&cError: You cannot ban a member from your island."),
    UNBAN_VISITOR("&aYou have unbanned %visitor% from visiting your island."),
    UNBAN_VISITOR_BROADCAST("&c[!] %visitor% was unbanned from visiting the island by %player%."),
    PLAYER_IS_NOT_BANNED("&cError: This player is not banned from the island."),
    KICK_VISITOR("&aYou have kicked %visitor% from your island."),
    KICK_VISITOR_BROADCAST("&c[!] %visitor% was kicked from the island by %player%."),
    VISITOR_KICKED_FROM_ISLAND("&a"),
    CANT_VISITOR_KICK_MEMBER_FROM_ISLAND("&cError: You cannot kick this player from the island as they are not a visitor."),
    JOIN_ISLAND("&aYou have joined %player%'s island."),
    JOIN_ISLAND_BROADCAST("&a[!] %player% has joined the island."),
    LEADER_LEAVE_ISLAND("&cError: Leaders must either transfer island leadership or disband their island."),
    NO_INVITE_FROM_ISLAND("&cError: There was no invite from this island or the invite has expired."),
    WAIT_INVITE("&cError: You must wait before inviting this player again."),
    ISLAND_UPGRADE_INSUFFICIENT_FUNDS("&cError: You do not have enough money to purchase this upgrade."),
    ISLAND_UPGRADE_SUCCESS("&aYou have successfully upgraded your island!"),
    ISLAND_SETTING_ENABLED("&aYou have enabled this setting for your island."),
    ISLAND_SETTING_DISABLED("&cYou have disabled this setting for your island."),
    ISLAND_SETTING_BORDER_CHANGE("&aYou have changed the color of your island border to %color%&a."),
    CALCULATE_ISLAND_WORTH("&7Recalculating all island data..."),
    CALCULATE_ISLAND_WORTH_COMPLETE("&a&lSUCCESS! &7Recalculated all island data!"),
    CANNOT_MINE_OUTSIDE_MINING_AREA("&cError: You cannot mine outside of the mining area."),
    CANNOT_PVP_IN_MINE("&cError: You cannot pvp in this mine."),
    MINE_DOES_NOT_EXIST("&cError: This mine does not exist."),
    MINE_ALREADY_EXISTS("&cError: A mine with that name already exists."),
    MINE_HAS_BLOCK("&cError: This mine already has this block"),
    MINE_DOES_NOT_HAVE_BLOCK("&cError: This mine does not have this block."),
    MINE_CREATE("&aYou successfully created the mine '%mine%'."),
    MINE_DELETE("&cYou successfully deleted the mine '%mine%'."),
    MINE_SET_MINE_POS1("&aYou have set the mine position 1 for '%mine%'."),
    MINE_SET_MINE_POS2("&aYou have set the mine position 2 for '%mine%'."),
    MINE_SET_CORNER_POS1("&aYou have set the corner position 1 for '%mine%'."),
    MINE_SET_CORNER_POS2("&aYou have set the corner position 2 for '%mine%'."),
    MINE_SET_SPAWN("&aYou have set the spawn position for %mine%."),
    MINE_ADD_BLOCK("&aYou have added %block% with a chance of %chance%% to %mine%."),
    MINE_DEL_BLOCK("&aYou have removed %block% from %mine%."),
    FEATURE_DISABLED("&cError: That feature is currently disabled.");

    private String msg;

    Messages(String msg){
        this.msg = msg;
    }

    public String get(){
        return StringUtil.HexCC(this.msg);
    }

    public void set(String msg){
        this.msg = msg;
    }

    public void send(Player p){
        p.sendMessage(get());
    }

    /*
    todo fix the 'replace' and 'usage' as they are one time use
    and for some reason replace the string completely
     */

    public Messages usage(String usage) {
        this.msg.replace("%usage%", usage);
        return this;
    }

    public void send(Profile profile){
        profile.getPlayer().sendMessage(get());
    }
}
