package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.gui.impl.IslandCreateGui;
import net.syphlex.skyblock.handler.gui.impl.IslandDeleteGui;
import net.syphlex.skyblock.handler.gui.impl.IslandGui;
import net.syphlex.skyblock.handler.island.block.IslandBlockData;
import net.syphlex.skyblock.handler.profile.IslandProfile;
import net.syphlex.skyblock.util.Position;
import net.syphlex.skyblock.util.WorldUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IslandCmd extends SimpleCmd {

    public IslandCmd() {
        super("island");
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        final IslandProfile profile = Skyblock.get().getDataHandler().get(player);

        if (profile.hasIsland()) {

            player.sendMessage("worth: $" + profile.getIsland().getWorth());

        }

        if (args.length > 0) {

            switch (args[0].toLowerCase()) {
                case "create":
                    Skyblock.get().getIslandHandler().generateIsland(profile);
                    break;
                case "delete":

                    if (!profile.isIslandLeader()) {
                        Messages.NOT_ISLAND_LEADER.send(profile);
                        return;
                    }

                    Skyblock.get().getGuiHandler().openGui(profile, new IslandDeleteGui());
                    break;
                case "home":

                    if (!profile.hasIsland()) {
                        Messages.DOES_NOT_HAVE_ISLAND.send(profile);
                        return;
                    }

                    profile.getIsland().teleport(player);
                    Messages.TELEPORTED_TO_ISLAND.send(profile);
                    break;
                case "sethome":

                    if (!profile.isIslandLeader()) {
                        Messages.NOT_ISLAND_LEADER.send(profile);
                        return;
                    }

                    if (!WorldUtil.isWorld(player.getWorld(), Skyblock.get().getIslandWorld())
                            || !profile.getIsland().isInside(player.getLocation())) {
                        Messages.MUST_BE_AT_ISLAND.send(profile);
                        return;
                    }

                    profile.getIsland().setHome(player.getLocation());
                    Messages.ISLAND_SETHOME.send(profile);
                    break;
                case "grid":
                    player.sendMessage(Skyblock.get().getIslandHandler().printGrid());
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

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {
    }
}
