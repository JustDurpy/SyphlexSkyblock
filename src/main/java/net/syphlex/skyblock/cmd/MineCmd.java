package net.syphlex.skyblock.cmd;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.mine.data.Mine;
import net.syphlex.skyblock.manager.mine.data.MineBlockData;
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
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();

        options.add("create");
        options.add("delete");
        options.add("setpos1");
        options.add("setpos2");
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
                case "setpos1":
                case "setpos2":
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
                case "setpos1":
                    handlePos1(player, args);
                    break;
                case "setpos2":
                    handlePos2(player, args);
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
                        player.sendMessage(mine.getMineName() + " ID:" + mine.getId());
                    }
                    break;
            }
        }
    }

    private void handleSetSpawn(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setspawn <id>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        mine.setSpawn(new Position(
                player.getWorld(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()));
        player.sendMessage("You have set the spawn position for " + mine.getMineName() + " ID:" + mine.getId());
    }

    private void handleReset(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine reset <id>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);

        if (Skyblock.get().getMineHandler().getMine(id) == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        player.sendMessage("Resetting mine...");
        Mine mine = Skyblock.get().getMineHandler().getMine(id);
        Skyblock.get().getMineHandler().regenerateMine(mine);
        player.sendMessage("Successfully reset mine ID:" + mine.getId());
    }

    private void handleDelBlock(Player player, String[] args){
        if (args.length != 4) {
            player.sendMessage("usage: /mine delblock <mineId> <material> <chance>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        if (!StringUtil.isNumber(args[3])) {
            player.sendMessage("The chance must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);
        double chance = Double.parseDouble(args[3]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        Material material = Material.getMaterial(args[2]);
        if (material == null) material = Material.STONE;

        MineBlockData blockData = mine.getBlockData(material);

        if (blockData == null) {
            player.sendMessage("This mine does not have this block.");
            return;
        }

        mine.getBlocks().remove(blockData);
        player.sendMessage("You have removed " + material.name() + " from mine. ID:" + id);
    }

    private void handleAddBlock(Player player, String[] args){
        if (args.length != 4) {
            player.sendMessage("usage: /mine addblock <mineId> <material> <chance>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        if (!StringUtil.isNumber(args[3])) {
            player.sendMessage("The chance must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);
        double chance = Double.parseDouble(args[3]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        Material material = Material.getMaterial(args[2]);
        if (material == null) material = Material.STONE;

        if (mine.hasBlock(material)) {
            player.sendMessage("This mine already has this block.");
            return;
        }

        mine.getBlocks().add(new MineBlockData(material, chance));
        player.sendMessage("You have added " + material.name() + " with chance of " + chance + "% to mine. ID:" + id);
    }

    private void handlePos2(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setpos2 <id>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        mine.setCorner2(new Position(
                player.getWorld(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()));
        player.sendMessage("You have set the corner 2 position for " + mine.getMineName() + " ID:" + mine.getId());
    }

    private void handlePos1(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine setpos1 <id>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        mine.setCorner1(new Position(
                player.getWorld(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()));
        player.sendMessage("You have set the corner 1 position for " + mine.getMineName() + " ID:" + mine.getId());
    }

    private void handleDelete(Player player, String[] args){
        if (args.length != 2) {
            player.sendMessage("usage: /mine delete <id>");
            return;
        }

        if (!StringUtil.isNumber(args[1])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        int id = (int)Double.parseDouble(args[1]);

        Mine mine = Skyblock.get().getMineHandler().getMine(id);

        if (mine == null) {
            player.sendMessage("That mine does not exist.");
            return;
        }

        Skyblock.get().getMineHandler().getMines().remove(mine);
        player.sendMessage("You deleted the mine ID:" + id);
    }

    private void handleCreate(Player player, String[] args){
        if (args.length != 3) {
            player.sendMessage("usage: /mine create <name> <id>");
            return;
        }

        if (!StringUtil.isNumber(args[2])) {
            player.sendMessage("The id must be a number.");
            return;
        }

        String name = args[1];
        int id = (int)Double.parseDouble(args[2]);

        if (Skyblock.get().getMineHandler().getMine(id) != null
                || Skyblock.get().getMineHandler().getMine(name) != null) {
            player.sendMessage("That mine already exists.");
            return;
        }

        Mine mine = new Mine(id, name);
        Skyblock.get().getMineHandler().getMines().add(mine);
        player.sendMessage("You created the " + name + " mine ID:" + id);
    }

    @Override
    public void handleServerCmd(CommandSender sender, String[] args) {

    }
}
