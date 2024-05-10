package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MobCoinsCmd extends SimpleCmd {
    public MobCoinsCmd() {
        super("mobcoins");
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        final Profile profile = Skyblock.get().getDataHandler().get(player);

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("shop")) {

                profile.getPlayer().sendMessage("Shop is in progress...");

            } else {

                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    Messages.PLAYER_NOT_FOUND.send(profile);
                    return;
                }

                final Profile targetProfile = Skyblock.get().getDataHandler().get(target);

                Messages.DISPLAY_MOB_COINS
                        .replace("%player%", target.getName())
                        .replace("%mobcoins%", String.format("%,d", targetProfile.getMobCoins()))
                        .send(profile);
            }

        } else {
            Messages.DISPLAY_MOB_COINS
                    .replace("%player%", player.getName())
                    .replace("%mobcoins%", String.format("%,d", profile.getMobCoins()))
                    .send(profile);
        }
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
