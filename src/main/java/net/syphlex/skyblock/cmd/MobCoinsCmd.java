package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigEnum;
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
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return null;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        if (!ConfigEnum.FEATURES_MOBCOINS.getAsBoolean()) {
            Messages.FEATURE_DISABLED.send(player);
            return;
        }

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

                profile.sendMessage(Messages.DISPLAY_MOB_COINS.get()
                        .replace("%player%", target.getName())
                        .replace("%mobcoins%", String.format("%,d", targetProfile.getMobCoins())));
            }

        } else {
            profile.sendMessage(Messages.DISPLAY_MOB_COINS.get()
                    .replace("%player%", player.getName())
                    .replace("%mobcoins%", String.format("%,d", profile.getMobCoins())));
        }
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
