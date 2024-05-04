package net.syphlex.skyblock.manager.mobcoin;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Getter
public class MobCoinHandler {

    private final ArrayList<MobCoinEntity> mobCoinEntities = new ArrayList<>();
    public final static String MOB_COIN_TAG = "mobcoin";

    public boolean isMobCoin(ItemStack item) {
        if (item == null
                || item.getAmount() <= 0
                || item.getType() == Material.AIR)
            return false;
        NBTItem nbt = new NBTItem(item);
        return nbt.getByte(MOB_COIN_TAG) != null
                && nbt.getByte(MOB_COIN_TAG) == (byte) 1;
    }

    public void handleEntityDeath(Entity e){

        MobCoinEntity mobCoinEntity = getEntityData(e);

        if (mobCoinEntity == null)
            return;

        boolean dropCoin = (Math.random() * 100 <= mobCoinEntity.getChance());

        if (dropCoin) {

            ItemStack item = new ItemBuilder()
                    .setName("&6&lMOB COIN")
                    .setMaterial(Material.SUNFLOWER)
                    .build();

            NBTItem nbt = new NBTItem(item);
            nbt.setByte(MOB_COIN_TAG, (byte)1);
            nbt.applyNBT(item);

            e.getWorld().dropItem(e.getLocation(), item);
        }
    }

    public MobCoinEntity getEntityData(Entity e){
        for (MobCoinEntity mobCoinEntity : this.mobCoinEntities) {

            if (mobCoinEntity.getEntityType() == null) {
                // todo error log
                continue;
            }

            if (e.getType() == mobCoinEntity.getEntityType()) {
                return mobCoinEntity;
            }
        }
        return null;
    }
}
