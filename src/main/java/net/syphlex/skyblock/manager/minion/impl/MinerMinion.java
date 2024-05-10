package net.syphlex.skyblock.manager.minion.impl;

import net.syphlex.skyblock.manager.mine.data.MineBlockData;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MinerMinion extends Minion
{

    protected final List<MineBlockData> blocks = new ArrayList<>();

    public MinerMinion(Type type, int level) {
        super(type, level);
        updateData();
    }

    @Override
    public void updateData(){
        for (Pair<Integer, Object> pair : getData().getObjects()) {

            if (pair.getX() != getLevel())
                continue;

            String line = (String) pair.getY();
            String[] blockSplit = line.split(",");

            for (String s : blockSplit) {

                String[] split = s.split(":");
                String block = split[0];
                String stringChance = split[1].substring(0, split[1].length() - 1);
                double percent = Double.parseDouble(stringChance);

                MineBlockData mineBlock = new MineBlockData(Material.getMaterial(block), percent);
                this.blocks.add(mineBlock);
            }
        }
    }

    @Override
    public void update(){

        super.update();

        int radius = getData().getRadius();
        int count = 0;

        boolean bedrock = true;
        for (int x = -radius; x <= radius; x += 1) {
            for (int z = -radius; z <= radius; z += 1) {

                Location l = position.getAsBukkit().clone().add(x, -1, z);
                while (count < getLevel() && l.getBlock().getType() != Material.BEDROCK) {
                    count++;
                    if (hasChest()) {
                        for (ItemStack i : l.getBlock().getDrops()) {
                            Item item = l.getWorld().dropItem(l, i);
                            getDrops().add(item);
                            item.remove();
                        }
                        l.getBlock().breakNaturally(new ItemStack(Material.AIR));
                    } else {
                        l.getBlock().breakNaturally();
                    }
                    l.getBlock().setType(Material.BEDROCK);
                }

                sendItem();
                if (l.getBlock().getType() != Material.BEDROCK)
                    bedrock = false;
            }
        }

        if (bedrock)
            resetMine();
    }

    @Override
    public void createStand() {
        super.createStand();
        this.armorStand.setItemInHand(new ItemBuilder()
                .setMaterial(Material.DIAMOND_PICKAXE)
                .build());
        this.resetMine();
    }

    private void resetMine(){
        int radius = 1;

        for (int x = -radius; x <= radius; x += 1) {
            for (int z = -radius; z <= radius; z += 1) {
                Material random = PluginUtil.generateRandomMineBlock(this.blocks);
                while (random == null)
                    random = PluginUtil.generateRandomMineBlock(this.blocks);
                position.getAsBukkit().clone().add(x, -1, z).getBlock().setType(random);
            }
        }
    }

    @Override
    public void destroy() {
        for (int x = -1; x <= 1; x += 1) {
            for (int z = -1; z <= 1; z += 1) {
                this.position.getAsBukkit().clone().add(x, -1, z).getBlock().setType(Material.AIR);
            }
        }
        super.destroy();
    }
}
