package net.syphlex.skyblock.manager.minion.impl;

import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class LumberJackMinion
extends Minion
{

    protected int amount;
    protected int radius;

    public LumberJackMinion(Type type, int level) {
        super(type, level);
        updateData();
    }

    @Override
    public void updateData() {
        for (Pair<Integer, Object> pair : getData().getObjects()) {

            if (pair.getX() != getLevel())
                continue;

            String line = pair.getY().toString();
            String[] lineSplit = line.split(",");

            String[] radSplit = lineSplit[0].split(":");
            String[] amoSplit = lineSplit[1].split(":");

            this.radius = Integer.parseInt(radSplit[1]);
            this.amount = Integer.parseInt(amoSplit[1]);
        }
    }

    @Override
    public void update(){
        super.update();

        int count = 0;

        for (int y = -this.radius; y <= this.radius + 1; y += 1) {
            for (int x = -this.radius; x <= this.radius; x += 1) {
                for (int z = -this.radius; z <= this.radius; z += 1) {

                    Location l = this.position.getAsBukkit().clone().add(x, y, z);

                    while (count < this.amount && l.getBlock().getType().name().contains("LOG")) {
                        if (hasChest()) {
                            for (ItemStack i : l.getBlock().getDrops()) {
                                Item item = l.getWorld().dropItem(l, i);
                                getDrops().add(item);
                                item.remove();
                            }
                            l.getBlock().setType(Material.AIR);
                        } else {
                            l.getBlock().breakNaturally();
                        }
                        sendItem();
                        count++;
                    }
                }
            }
        }
    }

    @Override
    public void createStand() {
        super.createStand();
        this.armorStand.setItemInHand(new ItemBuilder()
                .setMaterial(Material.DIAMOND_AXE)
                //.setGlowing(true)
                .build());
    }
}
