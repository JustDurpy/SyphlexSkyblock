package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.mine.data.Mine;
import net.syphlex.skyblock.manager.mine.data.MineBlockData;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.simple.SimpleCmd;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MineCmd extends SimpleCmd {
    public MineCmd() {
        super("mine");
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();

        options.add("create");
        options.add("delete");
        options.add("setcorner1");
        options.add("setcorner2");
        options.add("setminepos1");
        options.add("setminepos2");
        options.add("setspawn");
        options.add("addblock");
        options.add("delblock");
        options.add("reset");
        options.add("list");

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
                case "setminepos1":
                case "setminepos2":
                case "setcorner1":
                case "setcorner2":
                case "addblock":
                case "delblock":
                case "setspawn":
                case "reset":
                    for (Mine mine : Skyblock.get().getMineHandler().getMines()) {
                        if (mine.getMineName().toLowerCase().startsWith(args[1].toLowerCase()))
                            list.add(mine.getMineName());
                    }
                    return list;
            }
        }
        return list;
    }

    @Override
    public void handleCmd(Player player, String[] args) {

        if (!Permissions.MINE_ADMIN.has(player) && !Permissions.ADMIN.has(player)) {
            Messages.NO_PERMISSION.send(player);
            return;
        }

        if (args.length > 0){
            switch (args[0].toLowerCase()) {
                case "create":
                    handleCreate(player, args);
                    break;
                case "delete":
                    handleDelete(player, args);
                    break;
                case "setcorner1":
                    handleCorner1(player, args);
                    break;
                case "setcorner2":
                    handleCorner2(player, args);
                    break;
                case "setminepos1":
                    handleMinePos1(player, args);
                    break;
                case "setminepos2":
                    handleMinePos2(player, args);
                    break;
                case "setspawn":
                    handleSetSpawn(player, args);
                    break;
                case "addblock":
                    handleAddBlock(player, args);
                    break;
                case "delblock":
                    handleDelBlock(player, args);
                    break;
                case "reset":
                    handleReset(player, args);
                    break;
                case "list":
                    if (Skyblock.get().getMineHandler().getMines().size() < 1) {
                        player.sendMessage("There were no mines found.");
                        return;
                    }
                    player.sendMessage("Mines:");
                    for (Mine mine : Skyblock.get().getMineHandler().getMines()) {
                        player.sendMessage(mine.getMineName());
                    }
                    break;
                default:
                    handleHelp(player);
                    break;
            }
        }
    }

    public static void handleHelp(CommandSender sender) {
        for (String s : ConfigEnum.MINE_HELP.getAsList())
            sender.sendMessage(StringUtil.HexCC(s));
    }

    private void handleSetSpawn(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setspawn <id>");
            return;
        }

        String name = args[1];
        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        mine.setSpawn(new Position(
                player.getWorld(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()));
        player.sendMessage(Messages.MINE_SET_SPAWN.get().replace("%mine%", mine.getMineName()));
    }

    private void handleReset(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine reset <id>");
            return;
        }

        String name = args[1];
        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        player.sendMessage("Resetting mine...");
        Skyblock.get().getMineHandler().regenerateMine(mine);
        player.sendMessage("Successfully reset mine " + mine.getMineName());
    }

    private void handleDelBlock(Player player, String[] args){
        if (args.length != 4) {
            player.sendMessage("usage: /mine delblock <mine> <material>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        Material material = Material.getMaterial(args[2]);
        if (material == null) material = Material.STONE;

        MineBlockData blockData = mine.getBlockData(material);

        if (blockData == null) {
            Messages.MINE_DOES_NOT_HAVE_BLOCK.send(player);
            return;
        }

        mine.getBlocks().remove(blockData);
        player.sendMessage(Messages.MINE_DEL_BLOCK.get()
                .replace("%block%", material.name())
                .replace("%mine%", mine.getMineName()));
    }

    private void handleAddBlock(Player player, String[] args){
        if (args.length != 4) {
            player.sendMessage("usage: /mine addblock <mine> <material> <chance>");
            return;
        }

        if (!StringUtil.isNumber(args[3])) {
            player.sendMessage(StringUtil.CC("&cThe chance must be a number."));
            return;
        }

        String name = args[1];
        double chance = Double.parseDouble(args[3]);

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        Material material = Material.getMaterial(args[2]);
        if (material == null) material = Material.STONE;

        if (mine.hasBlock(material)) {
            Messages.MINE_HAS_BLOCK.send(player);
            return;
        }

        mine.getBlocks().add(new MineBlockData(material, chance));
        player.sendMessage(Messages.MINE_ADD_BLOCK.get()
                .replace("%block%", material.name())
                .replace("%chance%", String.valueOf(chance))
                .replace("%mine%", mine.getMineName()));
    }

    private void handleCorner2(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setcorner1 <mine>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        mine.setAreaCorner1(new Position(
                player.getWorld(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
        player.sendMessage(Messages.MINE_SET_CORNER_POS2.get().replace("%mine%", mine.getMineName()));
    }

    private void handleCorner1(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setcorner1 <mine>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            player.sendMessage("That mine does not exist.");
            return;
        }

        mine.setAreaCorner1(new Position(
                player.getWorld(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
        player.sendMessage(Messages.MINE_SET_CORNER_POS1.get().replace("%mine%", mine.getMineName()));
    }

    private void handleMinePos2(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setminepos2 <mine>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        mine.setMinePos2(new Position(
                player.getWorld(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
        player.sendMessage(Messages.MINE_SET_MINE_POS2.get().replace("%mine%", mine.getMineName()));
    }

    private void handleMinePos1(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setminepos1 <mine>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        mine.setMinePos1(new Position(
                player.getWorld(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ));
        player.sendMessage(Messages.MINE_SET_MINE_POS1.get().replace("%mine%", mine.getMineName()));
    }

    private void handleDelete(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine delete <name>");
            return;
        }

        String name = args[1];

        Mine mine = Skyblock.get().getMineHandler().getMine(name);

        if (mine == null) {
            Messages.MINE_DOES_NOT_EXIST.send(player);
            return;
        }

        Skyblock.get().getMineHandler().getMines().remove(mine);
        player.sendMessage(Messages.MINE_DELETE.get()
                .replace("%mine%", mine.getMineName()));
    }

    private void handleCreate(Player player, String[] args){
        if (args.length != 3) {
            player.sendMessage("usage: /mine create <name>");
            return;
        }

        String name = args[1];

        if (Skyblock.get().getMineHandler().getMine(name) != null) {
            Messages.MINE_ALREADY_EXISTS.send(player);
            return;
        }

        Mine mine = new Mine(name);
        Skyblock.get().getMineHandler().getMines().add(mine);
        player.sendMessage(Messages.MINE_CREATE.get()
                .replace("%mine%", mine.getMineName()));
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
