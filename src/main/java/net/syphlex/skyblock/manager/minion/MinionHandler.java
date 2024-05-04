package net.syphlex.skyblock.manager.minion;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.minion.impl.MinerMinion;
import net.syphlex.skyblock.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class MinionHandler {

    private final ArrayList<Minion> minions = new ArrayList<>();
    private final ArrayList<MinionData> minionDataList = new ArrayList<>();

    public final static String EGG_TAG = "minion_egg";
    public final static String LEVEL_TAG = "minion_level";

    public void onEnable(){
        new BukkitRunnable(){
            @Override
            public void run(){
                for (Minion m : minions)
                    m.update();
            }
        }.runTaskTimer(Skyblock.get(), 0L, 20L);
    }

    public void onDisable(){
        for (Minion m : this.minions)
            m.removeStand();
    }

    public void create(Player player, ItemStack item, Location spawn, Minion.Type type){
        Minion minion = null;
        int level = getLevelFromEgg(item);
        switch (type) {
            case MINER:
                minion = new MinerMinion(type, level);
                spawn.add(0, 1, 0);
                break;
            case SLAYER:
                //minion = new SlayerMinion(type, level);
                break;
            case LUMBERJACK:
                //minion = new LumberJackMinion(type, level);
                break;
        }
        if (minion == null)
            return;
        minion.setOwner(player.getUniqueId());
        minion.placeMinion(spawn);
        this.minions.add(minion);
    }

    public ItemStack createMinionEgg(Minion.Type type){

        ItemStack item = new ItemBuilder()
                .setMaterial(Material.PUFFERFISH_SPAWN_EGG)
                .setName("&c" + type.name() + " Minion Egg")
                .build();

        NBTItem nbt = new NBTItem(item);
        nbt.setString(EGG_TAG, type.name());
        nbt.setInteger(LEVEL_TAG, 1);
        nbt.applyNBT(item);

        return item;
    }

    public ItemStack pickUpMinion(Minion minion){

        ItemStack item = new ItemBuilder()
                .setMaterial(Material.PUFFERFISH_SPAWN_EGG)
                .setName("&c" + minion.getType().name() + " Minion Egg")
                .build();

        NBTItem nbt = new NBTItem(item);
        nbt.setString(EGG_TAG, minion.getType().name());
        nbt.setInteger(LEVEL_TAG, minion.getLevel());
        nbt.applyNBT(item);

        return item;
    }

    public boolean isMinionEgg(ItemStack item){

        if (item == null
                || item.getAmount() <= 0
                || item.getType() == Material.AIR)
            return false;

        NBTItem nbt = new NBTItem(item);
        return nbt.getString(EGG_TAG) != null
                && !nbt.getString(EGG_TAG).isEmpty();
    }

    public Minion.Type getTypeFromEgg(ItemStack item){

        if (!isMinionEgg(item))
            return null;

        NBTItem nbt = new NBTItem(item);
        return getTypeFromName(nbt.getString(EGG_TAG));
    }

    public int getLevelFromEgg(ItemStack item){

        if (!isMinionEgg(item))
            return -1;

        NBTItem nbt = new NBTItem(item);
        return nbt.getInteger(LEVEL_TAG);
    }

    public Minion.Type getTypeFromName(String s){
        for (Minion.Type type : Minion.Type.values()) {
            if (type.name().equalsIgnoreCase(s))
                return type;
        }
        return null;
    }

    public MinionData getMinionDataOf(Minion minion){
        for (MinionData data : this.minionDataList){
            if (data.getType() == minion.getType())
                return data;
        }
        return null;
    }

    public UUID generateUUID(){
        UUID uuid = UUID.randomUUID();

        // once we find an available uuid for the minion we return the key
        while (uuidExists(uuid))
            uuid = UUID.randomUUID();

        return uuid;
    }

    /*
    Here we will check to see if the minions generated uuid exists in the current data set.
     */
    private boolean uuidExists(UUID uuid) {
        for (Minion m : this.minions) {
            if (m.getUuid().equals(uuid))
                return true;
        }
        return false;
    }

}
