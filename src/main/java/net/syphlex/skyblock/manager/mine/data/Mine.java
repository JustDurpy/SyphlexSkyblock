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

    private final String mineName;
    private String displayName = "", permission = "none";
    private Position minePos1, minePos2, areaCorner1, areaCorner2, spawn;
    private final List<MineBlockData> blocks;
    private boolean pvp = false;

    public Mine(String mineName, String displayName, String permission,
                List<MineBlockData> blockData, Position areaCorner1, Position areaCorner2,
                Position minePos1, Position minePos2, Position spawn, boolean pvp){
        this.mineName = mineName;
        this.displayName = displayName;
        this.permission = permission;
        this.blocks = blockData;
        this.areaCorner1 = areaCorner1;
        this.areaCorner2 = areaCorner2;
        this.minePos1 = minePos1;
        this.minePos2 = minePos2;
        this.spawn = spawn;
        this.pvp = pvp;
    }

    public Mine(String mineName, List<MineBlockData> blockData){
        this.mineName = mineName;
        this.blocks = blockData;
    }

    public Mine(String mineName){
        this.mineName = mineName;
        this.blocks = new ArrayList<>();
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

    public boolean isInsideMiningArea(Location l){

        final int minX = Math.min(this.minePos1.getBlockX(), this.minePos2.getBlockX());
        final int minY = Math.min(this.minePos1.getBlockY(), this.minePos2.getBlockZ());
        final int minZ = Math.min(this.minePos1.getBlockZ(), this.minePos2.getBlockZ());

        final int maxX = Math.max(this.minePos1.getBlockX(), this.minePos2.getBlockX());
        final int maxY = Math.max(this.minePos1.getBlockY(), this.minePos2.getBlockY());
        final int maxZ = Math.max(this.minePos1.getBlockZ(), this.minePos2.getBlockZ());

        final int blockX = l.getBlockX();
        final int blockY = l.getBlockY();
        final int blockZ = l.getBlockZ();

        return blockX >= minX && blockY >= minY && blockZ >= minZ
                && blockX <= maxX && blockY <= maxY && blockZ <= maxZ;
    }

    public int getMaxX(){
        return Math.max(this.areaCorner1.getBlockX(), this.areaCorner2.getBlockX());
    }

    public int getMaxY(){
        return Math.max(this.areaCorner1.getBlockY(), this.areaCorner2.getBlockY());
    }

    public int getMaxZ(){
        return Math.max(this.areaCorner1.getBlockZ(), this.areaCorner2.getBlockZ());
    }

    public int getMinX(){
        return Math.min(this.areaCorner1.getBlockX(), this.areaCorner2.getBlockX());
    }

    public int getMinY(){
        return Math.min(this.areaCorner1.getBlockY(), this.areaCorner2.getBlockY());
    }

    public int getMinZ(){
        return Math.min(this.areaCorner1.getBlockZ(), this.areaCorner2.getBlockZ());
    }
}
