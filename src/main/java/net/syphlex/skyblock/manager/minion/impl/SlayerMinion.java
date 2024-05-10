package net.syphlex.skyblock.manager.minion.impl;

import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class SlayerMinion
extends Minion
{

    protected int killAmount;

    public SlayerMinion(Type type, int level) {
        super(type, level);
        updateData();
    }

    @Override
    public void updateData(){
        for (Pair<Integer, Object> pair : getData().getObjects()) {

            if (pair.getX() != getLevel())
                continue;

            this.killAmount = Integer.parseInt(pair.getY().toString());
        }
    }

    @Override
    public void update(){
        super.update();

        int killed = 0;

        for (LivingEntity e : WorldUtil.getLivingEntities(position.getAsBukkit(), getData().getRadius())) {
            if (e == null
                    || e instanceof Player
                    || e instanceof Item
                    || e instanceof Projectile)
                continue;

            if (killed++ <= this.killAmount) {
                e.damage(1000000000D);
            }
        }
    }

    @Override
    public void createStand() {
        super.createStand();
        this.armorStand.setItemInHand(new ItemBuilder()
                .setMaterial(Material.DIAMOND_SWORD)
                //.setGlowing(true)
                .build());
    }
}
