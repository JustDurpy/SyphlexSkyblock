package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.IslandCreateGui;
import net.syphlex.skyblock.manager.gui.impl.island.IslandDeleteGui;
import net.syphlex.skyblock.manager.gui.impl.island.IslandGui;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.request.InviteRequest;
import net.syphlex.skyblock.manager.profile.IslandProfile;
import net.syphlex.skyblock.util.IslandUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IslandCmd extends SimpleCmd {

    public IslandCmd() {
        super("island");
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        final IslandProfile profile = Skyblock.get().getDataHandler().get(player);

        if (profile.hasIsland()) {

            player.sendMessage("worth: $" + profile.getIsland().getWorth());
            player.sendMessage("placement: " + profile.getIsland().getIdentifier());

        }

        if (args.length > 0) {

            switch (args[0].toLowerCase()) {
                case "create":
                    handleCreate(profile);
                    break;
                case "delete":
                    handleDelete(profile);
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

    private void handleJoin(IslandProfile profile, String[] args){

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
            IslandProfile inviterProfile = Skyblock.get().getDataHandler().get(Bukkit.getPlayer(islandToJoin));
            request = profile.getIslandInvite(inviterProfile.getIsland());
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
        island.addMember(profile);

        profile.getPlayer().sendMessage("You have joined " + island.getLeader().getUsername() + "'s island.");
    }

    private void handleLeave(IslandProfile profile){

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        if (profile.isIslandLeader()) {
            Messages.LEADER_LEAVE_ISLAND.send(profile);
            return;
        }


    }

    private void handleInvite(IslandProfile profile, String[] args){

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
        IslandProfile targetProfile = Skyblock.get().getDataHandler().get(target);

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

    private void handleHome(IslandProfile profile){
        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        profile.getIsland().teleport(profile.getPlayer());
        Messages.TELEPORTED_TO_ISLAND.send(profile);
    }

    private void handleDelete(IslandProfile profile){
        if (!profile.isIslandLeader()) {
            Messages.NOT_ISLAND_LEADER.send(profile);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(profile, new IslandDeleteGui());
    }

    private void handleCreate(IslandProfile profile){
        Skyblock.get().getIslandHandler().generateIsland(profile);
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("create");
            list.add("delete");
            list.add("sethome");
            list.add("home");
            list.add("invite");
            list.add("join");
            list.add("leave");
        }

        return list;
    }
}
