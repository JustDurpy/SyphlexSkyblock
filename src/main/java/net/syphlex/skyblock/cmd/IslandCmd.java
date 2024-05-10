package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.*;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.request.InviteRequest;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IslandCmd extends SimpleCmd {

    public IslandCmd() {
        super("island");
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();

        options.add("help");
        options.add("create");
        options.add("delete");
        options.add("info");
        options.add("upgrade");
        options.add("permissions");
        options.add("sethome");
        options.add("home");
        options.add("invite");
        options.add("join");
        options.add("leave");


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
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            list.add(p.getName());
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
                    player.sendMessage(StringUtil.CC(" "));
                    player.sendMessage(StringUtil.CC("&6&lIsland Help:"));
                    player.sendMessage(" ");
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fcreate"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fdelete"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &finfo <island>"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fupgrades"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fpermissions"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fhome"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &finvite <player>"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fjoin <island/player>"));
                    player.sendMessage(StringUtil.CC(" &7* &e/island &fleave"));
                    player.sendMessage("");
                    break;
                case "create":
                    handleCreate(profile);
                    break;
                case "delete":
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
                case "home":
                    handleHome(profile);
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
                case "grid":
                    player.sendMessage(IslandUtil.printGrid());
                    break;
                default:
                    if (profile.hasIsland()) {
                        Skyblock.get().getGuiHandler().openGui(profile, new IslandGui());
                    } else {
                        Skyblock.get().getGuiHandler().openGui(profile, new IslandCreateGui());
                    }
                    break;
            }
        } else {
            if (profile.hasIsland()) {
                Skyblock.get().getGuiHandler().openGui(profile, new IslandGui());
            } else {
                Skyblock.get().getGuiHandler().openGui(profile, new IslandCreateGui());
            }
        }
    }

    private void handleInfo(Profile profile, String[] args){
        if (args.length == 1) {

            if (!profile.hasIsland()) {
                Messages.DOES_NOT_HAVE_ISLAND.send(profile);
                return;
            }

            profile.getPlayer().sendMessage(profile.getPlayer().getName() + "'s island:");
            profile.getPlayer().sendMessage("Members: ");
            for (MemberProfile memberProfile : profile.getIsland().getMembers()) {
                profile.getPlayer().sendMessage("" + memberProfile.getUsername() + " : " + memberProfile.getRole().getIdentifier());
            }
            profile.getPlayer().sendMessage("Worth: $" + profile.getIsland().getWorth());
        } else {

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            Island targetIsland = null;

            for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
                for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().length(); c++) {
                    Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);
                    if (island == null) continue;
                    if (island.getLeader().getUuid().equals(target.getUniqueId())) {
                        targetIsland = island;
                        break;
                    }
                }
            }

            if (targetIsland == null) {
                Messages.ISLAND_NOT_FOUND.send(profile);
                return;
            }

            profile.getPlayer().sendMessage(target.getName() + "'s island:");
            profile.getPlayer().sendMessage("Members: ");
            for (MemberProfile memberProfile : targetIsland.getMembers()) {
                profile.getPlayer().sendMessage("" + memberProfile.getUsername() + " : " + memberProfile.getRole().getIdentifier());
            }
            profile.getPlayer().sendMessage("Worth: $" + targetIsland.getWorth());
        }
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
        profile.getPlayer().sendMessage("You have joined " + island.getLeader().getUsername() + "'s island.");
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

        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(profile);
            return;
        }

        Island island = profile.getIsland();
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

        Messages.SENT_ISLAND_INVITE.replace("%player%", target.getName()).send(profile);
        Messages.RECEIVED_ISLAND_INVITE.replace("%player%", profile.getPlayer().getName()).send(targetProfile);
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

        Skyblock.get().getGuiHandler().openGui(profile, new IslandDeleteGui());
    }

    private void handleCreate(Profile profile){
        Skyblock.get().getIslandHandler().generateIsland(profile);
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {
    }
}
