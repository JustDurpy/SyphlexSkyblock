package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PluginCmd extends SimpleCmd {
    public PluginCmd() {
        super("syphlexskyblock");
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            player.sendMessage(StringUtil.CC("&aReloading all configs..."));
            Skyblock.get().onDisable();
            Skyblock.get().onEnable();
            //Skyblock.get().start();
            player.sendMessage(StringUtil.CC("&aSuccessfully reloaded the plugin!"));
        }
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(StringUtil.CC("&aReloading all configs..."));
            Skyblock.get().onDisable();
            Skyblock.get().onEnable();
            sender.sendMessage(StringUtil.CC("&aSuccessfully reloaded the plugin!"));
        }
    }
}
