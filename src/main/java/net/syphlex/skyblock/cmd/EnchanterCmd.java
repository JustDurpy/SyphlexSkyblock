package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EnchanterCmd extends SimpleCmd {

    public EnchanterCmd() {
        super("enchanter");
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        if (!ConfigEnum.FEATURES_CUSTOM_ENCHANTS.getAsBoolean()) {
            Messages.FEATURE_DISABLED.send(player);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(
                Skyblock.get().getDataHandler().get(player),
                Skyblock.get().getGuiHandler().getEnchanterGui());
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
