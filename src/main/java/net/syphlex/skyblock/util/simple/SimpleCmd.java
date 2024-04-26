package net.syphlex.skyblock.util.simple;

import net.syphlex.skyblock.Skyblock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleCmd implements CommandExecutor {

    public SimpleCmd(String cmd){
        Skyblock.get().getCommand(cmd).setExecutor(this);
    }

    public abstract void handleCmd(Player player, String[] args);

    public abstract void handleServerCmd(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            handleServerCmd(sender, args);
            return true;
        } else {
            final Player p = (Player)sender;
            handleCmd(p, args);
            return true;
        }
    }
}
