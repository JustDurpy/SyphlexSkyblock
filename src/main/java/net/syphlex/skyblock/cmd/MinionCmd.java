package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.PlayerUtil;
import net.syphlex.skyblock.util.PluginUtil;
import net.syphlex.skyblock.util.StringUtil;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinionCmd extends SimpleCmd {
    public MinionCmd() {
        super("minion");
    }

    @Override
    public void handleCmd(Player player, String[] args) {
        if (args.length > 0) {
            Player target = Bukkit.getPlayer(args[1]);
            Minion.Type type = Skyblock.get().getMinionHandler().getTypeFromName(args[2]);

            if (!StringUtil.isDigit(args[3]))
                return;

            int amount = Integer.parseInt(args[3]);

            if (type == null) {
                player.sendMessage(StringUtil.CC("&cThat minion does not exist."));
                return;
            }

            for (int i = 0; i < amount; i++) {
                ItemStack egg = Skyblock.get().getMinionHandler().createMinionEgg(type);
                PlayerUtil.giveItem(target, egg);
            }
            player.sendMessage("You have given " + amount + " " + type.name() + " Minion(s) to " + target.getName() + ".");
        }
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
