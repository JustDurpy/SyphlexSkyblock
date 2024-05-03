package net.syphlex.skyblock.util;

import lombok.experimental.UtilityClass;
import net.syphlex.skyblock.manager.mine.data.MineBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class PluginUtil {

    public Material generateRandomMineBlock(List<MineBlockData> mineBlocks) {
        List<MineBlockData> compositions = new ArrayList<>(mineBlocks);
        int totalPercentage = 0;
        for(MineBlockData mineComposition : compositions) {
            totalPercentage = totalPercentage + (int)Math.round(mineComposition.getChance());
        }

        if(totalPercentage == 0) return null;

        Random random = new Random();
        int index =  random.nextInt(totalPercentage);
        int sum = 0, i = 0;
        while (sum < index) {
            sum = sum + (int)Math.round(compositions.get(i++).getChance());
        }
        return compositions.get(Math.max(0, i - 1)).getMaterial();
    }

    public boolean isSameLocation(Location l1, Location l2){
        return l1.getX() == l2.getX() && l1.getY() == l2.getY() && l1.getZ() == l2.getZ();
    }

    public boolean isMob(Entity e){

        switch (e.getType()) {
            case UNKNOWN:
            case PLAYER:
            case ARMOR_STAND:
            case EGG:
            case BOAT:
            case ARROW:
            case FIREBALL:
            case FIREWORK:
            case MINECART:
            case PAINTING:
            case SNOWBALL:
            case LIGHTNING:
            case ITEM_FRAME:
            case PRIMED_TNT:
            case LEASH_HITCH:
            case ENDER_PEARL:
            case DROPPED_ITEM:
            case ENDER_DRAGON:
            case ENDER_SIGNAL:
            case FISHING_HOOK:
            case MINECART_TNT:
            case WITHER_SKULL:
            case ENDER_CRYSTAL:
            case FALLING_BLOCK:
            case SPLASH_POTION:
            case EXPERIENCE_ORB:
            case MINECART_CHEST:
            case SMALL_FIREBALL:
            case MINECART_HOPPER:
            case MINECART_COMMAND:
            case MINECART_FURNACE:
            case THROWN_EXP_BOTTLE:
            case MINECART_MOB_SPAWNER:
                return false;
        }
        return true;
    }

}
