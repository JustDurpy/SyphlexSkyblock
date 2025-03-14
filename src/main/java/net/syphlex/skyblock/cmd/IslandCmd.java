package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.extra.IslandInfoGui;
import net.syphlex.skyblock.manager.gui.impl.island.extra.IslandTopGui;
import net.syphlex.skyblock.manager.gui.impl.island.manage.IslandCreateGui;
import net.syphlex.skyblock.manager.gui.impl.island.manage.IslandDeleteGui;
import net.syphlex.skyblock.manager.gui.impl.island.permissions.IslandPermissionsRolesGui;
import net.syphlex.skyblock.manager.gui.impl.island.settings.IslandSettingsGui;
import net.syphlex.skyblock.manager.gui.impl.island.upgrades.IslandUpgradeGui;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.permissions.IslandPermission;
import net.syphlex.skyblock.manager.island.request.InviteRequest;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Permissions;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@SuppressWarnings("all")
public class IslandCmd extends SimpleCmd {

    public IslandCmd() {
        super("island");
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();

        options.add("help");
        options.add("create");
        options.add("delete");
        options.add("disband");
        options.add("info");
        options.add("settings");
        options.add("upgrade");
        options.add("permissions");
        options.add("settings");
        options.add("sethome");
        options.add("home");
        options.add("invite");
        options.add("join");
        options.add("leave");
        options.add("promote");
        options.add("demote");
        options.add("setleader");
        options.add("top");
        options.add("kick");
        options.add("kickvisitor");
        options.add("ban");
        options.add("unban");
        options.add("visit");
        options.add("warp");

        if (Permissions.ADMIN.has(player)) {
            options.add("admin");
        }

        if (args.length == 0) {
            return options;
        } else if (args.length == 1) {

            for (String option : options) {
                if (option.startsWith(args[0].toLowerCase()))
                    list.add(option);
            }

            return list;
        } else {
            switch (args[0].toLowerCase()) {
                case "invite":
                case "join":
                case "info":
                case "kick":
                case "kickvisitor":
                case "ban":
                case "unban":
                case "visit":
                case "warp":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            list.add(p.getName());
                    }
                    return list;
                case "promote":
                case "demote":
                case "setleader":

                    final Profile profile = Skyblock.get().getDataHandler().get(player);

                    if (!profile.hasIsland())
                        return list;

                    for (MemberProfile member : profile.getIsland().getMembers()) {
                        if (member.getUsername().toLowerCase().startsWith(args[1].toLowerCase()))
                            list.add(member.getUsername());
                    }
                    return list;
            }
        }
        return list;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        final Profile profile = Skyblock.get().getDataHandler().get(player);

        if (args.length > 0) {

            switch (args[0].toLowerCase()) {
                case "help":
                    handleHelp(profile.getPlayer());
                    break;
                case "admin":
                    handleAdmin(profile);
                    break;
                case "create":
                    handleCreate(profile);
                    break;
                case "delete":
                case "disband":
                    handleDelete(profile);
                    break;
                case "info":
                    handleInfo(profile, args);
                    break;
                case "upgrade":
                case "upgrades":
                    handleUpgrades(profile);
                    break;
                case "permissions":
                    handlePermissions(profile);
                    break;
                case "settings":
                case "setting":
                    handleSettings(profile);
                    break;
                case "home":
                    handleHome(profile);
                    break;
                case "visit":
                case "warp":
                    handleVisit(profile, args);
                    break;
                case "invite":
                    handleInvite(profile, args);
                    break;
                case "leave":
                    handleLeave(profile);
                    break;
                case "join":
                    handleJoin(profile, args);
                    break;
                case "promote":
                    handlePromote(profile, args);
                    break;
                case "demote":
                    handleDemote(profile, args);
                    break;
                case "setleader":
                    handleSetLeader(profile, args);
                    break;
                case "top":
                    if (ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().isEnabled()) {
                        Skyblock.get().getGuiHandler().openGui(profile, new IslandTopGui());
                        return;
                    }
                    Messages.FEATURE_DISABLED.send(profile);
                    break;
                case "kick":
                    handleKick(profile, args);
                    break;
                case "ban":
                    handleBan(profile, args);
                    break;
                case "unban":
                    handleUnban(profile, args);
                    break;
                case "kickvisitor":
                    handleKickVisitor(profile, args);
                    break;
                case "grid":
                    player.sendMessage(IslandUtil.printGrid());
                    break;
                default:
                    if (profile.hasIsland()) {
                        profile.openIslandPanel();
                    } else {
                        Skyblock.get().getGuiHandler().openGui(profile, new IslandCreateGui());
                    }
                    break;
            }
        } else {
            if (profile.hasIsland()) {
                profile.openIslandPanel();
            } else {
                Skyblock.get().getGuiHandler().openGui(profile, new IslandCreateGui());
            }
        }
    }

    public static void handleHelp(CommandSender sender){
        for (String s : ConfigEnum.ISLAND_HELP.getAsList())
            sender.sendMessage(StringUtil.HexCC(s));
    }

    private void handlePromote(Profile profile, String[] args){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        final Island island = profile.getIsland();

        if (!island.isApartOfIsland(target.getUniqueId())) {
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        MemberProfile targetMember = island.getMember(target.getUniqueId());

        // the boolean runs the role change
        if (targetMember.promote()) {

            profile.sendMessage(Messages.PROMOTED_ISLAND_MEMBER.get()
                    .replace("%player%", targetMember.getUsername())
                    .replace("%role%", targetMember.getRole().getIdentifier()));

            if (target.isOnline()) {
                target.getPlayer().sendMessage(Messages.ISLAND_MEMBER_GOT_PROMOTED.get()
                        .replace("%role%", targetMember.getRole().getIdentifier()));
            }

            island.broadcast(Messages.PROMOTED_ISLAND_MEMBER_BROADCAST.get()
                    .replace("%player%", targetMember.getUsername())
                    .replace("%leader%", profile.getPlayer().getName())
                    .replace("%role%", targetMember.getRole().getIdentifier()));

        } else {
            Messages.CANT_PROMOTE_OR_DEMOTE_ISLAND_MEMBER.send(profile);
        }
    }

    private void handleDemote(Profile profile, String[] args){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        final Island island = profile.getIsland();

        if (!island.isApartOfIsland(target.getUniqueId())) {
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        MemberProfile targetMember = island.getMember(target.getUniqueId());

        // the boolean runs the role change
        if (targetMember.demote()) {

            profile.sendMessage(Messages.DEMOTED_ISLAND_MEMBER.get()
                    .replace("%player%", targetMember.getUsername())
                    .replace("%role%", targetMember.getRole().getIdentifier()));

            if (target.isOnline())
                target.getPlayer().sendMessage(Messages.ISLAND_MEMBER_GOT_DEMOTED.get()
                        .replace("%role%", targetMember.getRole().getIdentifier()));

            island.broadcast(Messages.DEMOTED_ISLAND_MEMBER_BROADCAST.get()
                    .replace("%player%", targetMember.getUsername())
                    .replace("%leader%", profile.getPlayer().getName())
                    .replace("%role%", targetMember.getRole().getIdentifier()));

        } else {
            Messages.CANT_PROMOTE_OR_DEMOTE_ISLAND_MEMBER.send(profile);
        }
    }

    private void handleSetLeader(Profile profile, String[] args){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        final Island island = profile.getIsland();

        // this prevents users from setting themselves to the leader, todo add messages?
        if (target.getUniqueId().equals(profile.getPlayer().getUniqueId()))
            return;

        if (!island.isApartOfIsland(target.getUniqueId())) {
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        island.setLeader(island.getMember(target.getUniqueId()));
    }

    private void handleAdmin(Profile profile){

        if (!Permissions.ADMIN.has(profile) && !profile.getPlayer().isOp()) {
            Messages.NO_PERMISSION.send(profile);
            return;
        }

        profile.setAdminMode(!profile.isAdminMode());
        profile.sendMessage(StringUtil.HexCC(profile.isAdminMode()
                ? "&aYour admin mode is now enabled."
                : "&cYour admin mode is now disabled."));
    }

    private void handleVisit(Profile profile, String[] args){

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (target.isOnline()) {

            final Profile targetProfile = Skyblock.get().getDataHandler().get(target.getPlayer());
            final Island island = targetProfile.getIsland();

            if (island == null) {
                Messages.ISLAND_NOT_FOUND.send(profile);
                return;
            }

            if (island.isBanned(profile.getPlayer())) {
                Messages.BANNED_FROM_ISLAND.send(profile);
                return;
            }

            island.teleport(profile.getPlayer());
            return;
        }

        if (!Skyblock.get().getDataHandler().getProfileFile().isRecorded(target.getUniqueId())) {
            Messages.ISLAND_NOT_FOUND.send(profile);
            return;
        }

        Island toVisit = IslandUtil.findIsland(target.getUniqueId());

        if (toVisit == null) {
            Messages.ISLAND_NOT_FOUND.send(profile);
            return;
        }

        if (toVisit.isBanned(profile.getPlayer())) {
            Messages.BANNED_FROM_ISLAND.send(profile);
            return;
        }

        toVisit.teleport(profile.getPlayer());
    }

    private void handleSettings(Profile profile){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        if (!ConfigMenu.ISLAND_SETTINGS_MENU.getMenuSetting().isEnabled()) {
            Messages.FEATURE_DISABLED.send(profile);
            return;
        }
        Skyblock.get().getGuiHandler().openGui(profile, new IslandSettingsGui());
    }

    private void handleKick(Profile profile, String[] args) {

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        if (!island.getMember(profile.getPlayer().getUniqueId()).getRole().hasPermission(IslandPermission.KICK_MEMBER)) {
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        /*
        target is offline
         */
        if (Bukkit.getPlayer(args[1]) == null) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (!Skyblock.get().getDataHandler().getProfileFile().isRecorded(target.getUniqueId())) {
                Messages.NO_RECORD_OF_PLAYER.send(profile);
                return;
            }

            if (!island.isApartOfIsland(target.getUniqueId())) {
                Messages.NOT_APART_OF_ISLAND.send(profile);
                return;
            }

            if (island.getLeader().getUuid().equals(target.getUniqueId())) {
                Messages.NO_ISLAND_PERMISSION.send(profile);
                return;
            }

            Skyblock.get().getDataHandler().getProfileFile().set(target.getUniqueId(),
                    "island", "null");
            Skyblock.get().getDataHandler().getProfileFile().set(target.getUniqueId(),
                    "island-role", IslandRole.VISITOR.getIdentifier());

            profile.sendMessage(Messages.KICK_MEMBER.get()
                    .replace("%member%", target.getName()));

            island.broadcast(Messages.KICK_MEMBER_BROADCAST.get()
                    .replace("%member%", target.getName())
                    .replace("%player%", profile.getPlayer().getName()));
            return;
        }

        /*
        target is online
         */

        final Player target = Bukkit.getPlayer(args[1]);
        final Profile targetProfile = Skyblock.get().getDataHandler().get(target.getUniqueId());

        if (!island.isApartOfIsland(target.getUniqueId())) {
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        if (island.getLeader().getUuid().equals(target.getUniqueId())) {
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        targetProfile.leaveIsland();

        profile.sendMessage(Messages.KICK_MEMBER.get()
                .replace("%member%", target.getName()));

        island.broadcast(Messages.KICK_MEMBER_BROADCAST.get()
                .replace("%member%", target.getName())
                .replace("%player%", profile.getPlayer().getName()));
    }

    private void handleBan(Profile profile, String[] args){

        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!profile.hasIsland()) {
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.BAN_VISITOR)) {
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        if (island.isApartOfIsland(target.getUniqueId())) {
            Messages.CANT_BAN_MEMBER_OF_ISLAND.send(profile);
            return;
        }

        island.banVisitor(target);

        profile.sendMessage(Messages.BAN_VISITOR.get()
                .replace("%visitor%", target.getName()));
        island.broadcast(Messages.BAN_VISITOR_BROADCAST.get()
                .replace("%player%", profile.getPlayer().getName())
                .replace("%visitor%", target.getName()));

        if (target.isOnline()) {
            Player p = target.getPlayer();
            p.teleport(Skyblock.get().getMainSpawn());
            Messages.BANNED_FROM_ISLAND.send(p);
        }
    }

    private void handleUnban(Profile profile, String[] args) {

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.UNBAN_VISITOR)) {
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        final Island island = profile.getIsland();

        if (!island.isBanned(target)) {
            Messages.PLAYER_IS_NOT_BANNED.send(profile);
            return;
        }

        profile.sendMessage(Messages.UNBAN_VISITOR.get()
                .replace("%visitor%", target.getName()));
        island.broadcast(Messages.UNBAN_VISITOR_BROADCAST.get()
                .replace("%visitor%", target.getName())
                .replace("%player%", profile.getPlayer().getName()));
    }

    private void handleKickVisitor(Profile profile, String[] args){

        if (!profile.hasIsland()){
            Messages.NOT_APART_OF_ISLAND.send(profile);
            return;
        }

        final Player target = Bukkit.getPlayer(args[1]);

        if (target == null){
            Messages.PLAYER_NOT_FOUND.send(target);
            return;
        }

        final Island island = profile.getIsland();
        final Profile targetProfile = Skyblock.get().getDataHandler().get(target);

        if (!profile.getMemberProfile().getRole().hasPermission(IslandPermission.KICK_VISITOR)) {
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        if (island.isApartOfIsland(target.getUniqueId())) {
            Messages.CANT_VISITOR_KICK_MEMBER_FROM_ISLAND.send(profile);
            return;
        }

        profile.sendMessage(Messages.KICK_VISITOR.get()
                .replace("%visitor%", target.getName()));
        island.broadcast(Messages.KICK_VISITOR_BROADCAST.get()
                .replace("%visitor%", target.getName())
                .replace("%player%", profile.getPlayer().getName()));
        target.teleport(Skyblock.get().getMainSpawn());
    }

    private void handleInfo(Profile profile, String[] args){

        Island infoIsland = null;

        if (args.length == 1) {

            if (!profile.hasIsland()) {
                Messages.DOES_NOT_HAVE_ISLAND.send(profile);
                return;
            }

            infoIsland = profile.getIsland();

        } else {

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            Island targetIsland = IslandUtil.findIsland(target.getUniqueId());

            if (targetIsland == null) {
                Messages.ISLAND_NOT_FOUND.send(profile);
                return;
            }

            infoIsland = targetIsland;
        }

        if (!ConfigMenu.ISLAND_INFORMATION_MENU.getMenuSetting().isEnabled()) {
            Messages.FEATURE_DISABLED.send(profile);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(profile, new IslandInfoGui(infoIsland));
    }

    private void handleUpgrades(Profile profile){

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(profile, new IslandUpgradeGui(profile.getIsland()));
    }

    private void handlePermissions(Profile profile){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsRolesGui());
    }

    private void handleJoin(Profile profile, String[] args){

        if (args.length != 2) {
            Messages.USAGE.usage("island join <player>");
            return;
        }

        if (profile.hasIsland()) {
            Messages.ALREADY_HAS_ISLAND.send(profile);
            return;
        }

        String islandToJoin = args[1];
        InviteRequest request = null;

        if (Bukkit.getPlayer(islandToJoin) != null) {
            Profile inviterProfile = Skyblock.get().getDataHandler().get(Bukkit.getPlayer(islandToJoin));

            Island island = inviterProfile.getIsland();

            if (island == null) {

                return;
            }

            request = profile.getIslandInvite(island);
        }

        if (request == null) {

            Island island = IslandUtil.getIslandFromOwnerName(islandToJoin);

            if (island == null) {
                Messages.NO_INVITE_FROM_ISLAND.send(profile);
                return;
            }

            request = profile.getIslandInvite(island);
        }

        Island island = request.getIsland();

        profile.joinIsland(island);
    }

    private void handleLeave(Profile profile){

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (profile.isIslandLeader()) {
            Messages.LEADER_LEAVE_ISLAND.send(profile);
            return;
        }

        profile.leaveIsland();
    }

    private void handleInvite(Profile profile, String[] args){

        if (args.length != 2) {
            Messages.USAGE.usage("island invite <player>").send(profile);
            return;
        }

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        Island island = profile.getIsland();

        if (!island.getMember(profile.getPlayer().getUniqueId()).getRole().hasPermission(IslandPermission.INVITE_MEMBER)){
            Messages.NO_ISLAND_PERMISSION.send(profile);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(profile);
            return;
        }

        Profile targetProfile = Skyblock.get().getDataHandler().get(target);

        if (island.isApartOfIsland(target.getUniqueId())) {
            Messages.CANT_INVITE_MEMBERS.send(profile);
            return;
        }

        if (targetProfile.hasIslandInviteRequest(island)) {

            InviteRequest request = targetProfile.getIslandInvite(island);

            long inviteSent = request.getTimeOfRequest();

            if (System.currentTimeMillis() - inviteSent < request.INVITATION_TIME) {
                Messages.ISLAND_INVITE_COOLDOWN.send(profile);
                return;
            }

            targetProfile.getInviteRequests().remove(request);
        }

        InviteRequest request = new InviteRequest(island, targetProfile);
        targetProfile.getInviteRequests().add(request);

        profile.sendMessage(Messages.SENT_ISLAND_INVITE.get()
                .replace("%player%", target.getName()));

        targetProfile.sendMessage(Messages.RECEIVED_ISLAND_INVITE.get()
                .replace("%player%", profile.getPlayer().getName()));
    }

    private void handleHome(Profile profile){
        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        profile.getIsland().teleport(profile.getPlayer());
        Messages.TELEPORTED_TO_ISLAND.send(profile);
    }

    private void handleDelete(Profile profile){

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        if (!ConfigMenu.DELETE_ISLAND_MENU.getMenuSetting().isEnabled()) {
            Skyblock.get().getIslandHandler().degenerateIsland(profile);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(profile, new IslandDeleteGui());
    }

    private void handleCreate(Profile profile){

        if (ConfigMenu.CREATE_ISLAND_MENU.getMenuSetting().isEnabled()) {
            Skyblock.get().getGuiHandler().openGui(profile, new IslandCreateGui());
            return;
        }

        Skyblock.get().getIslandHandler().generateIsland(profile, ConfigEnum.DEFAULT_SCHEMATIC_NAME.getAsString());
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {
    }
}
