package net.syphlex.skyblock.manager.mine.data;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.data.Position;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class Mine {

    private final int id;
    private final String mineName;
    private Position pos1, pos2, corner1, corner2, spawn;
    private final List<MineBlockData> blocks;

    public Mine(int id, String mineName, List<MineBlockData> blockData, Position corner1, Position corner2, Position spawn){
        this.id = id;
        this.mineName = mineName;
        this.blocks = blockData;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.spawn = spawn;
    }

    public Mine(int id, String mineName, List<MineBlockData> blockData){
        this.id = id;
        this.mineName = mineName;
        this.blocks = blockData;
    }

    public Mine(int id, String mineName){
        this.id = id;
        this.mineName = mineName;
        this.blocks = new ArrayList<>();
    }

    public String getConfigName(){
        return this.mineName + this.id;
    }

    public boolean hasBlock(Material material){
        return getBlockData(material) != null;
    }

    public MineBlockData getBlockData(Material material){
        for (MineBlockData data : this.blocks)
            if (data.getMaterial() == material)
                return data;
        return null;
    }

    public MineBlockData generateBlock(){
        List<MineBlockData> compositions = blocks;
        int totalPercentage = 0;
        for(MineBlockData comp : compositions) {
            totalPercentage = totalPercentage + (int)Math.round(comp.getChance());
        }

        if(totalPercentage == 0) return null;

        Random random = new Random();
        int index =  random.nextInt(totalPercentage);
        int sum = 0, i = 0;
        while (sum < index) {
            sum = sum + (int)Math.round(compositions.get(i++).getChance());
        }
        return compositions.get(Math.max(0, i - 1));
    }

    public boolean isInside(Location location){
        return location.getX() >= getMinX() && location.getY() >= getMinY() && location.getZ() >= getMinZ()
                && location.getX() <= getMaxX() && location.getY() <= getMaxY() && location.getZ() <= getMaxZ();
    }

    public int getMaxX(){
        return Math.max(corner1.getBlockX(), corner2.getBlockX());
    }

    public int getMaxY(){
        return Math.max(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMaxZ(){
        return Math.max(corner1.getBlockZ(), corner2.getBlockZ());
    }

    public int getMinX(){
        return Math.min(corner1.getBlockX(), corner2.getBlockX());
    }

    public int getMinY(){
        return Math.min(corner1.getBlockY(), corner2.getBlockY());
    }

    public int getMinZ(){
        return Math.min(corner1.getBlockZ(), corner2.getBlockZ());
    }
}
