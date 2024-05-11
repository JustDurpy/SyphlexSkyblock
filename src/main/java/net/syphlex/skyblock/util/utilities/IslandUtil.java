package net.syphlex.skyblock.util.utilities;

import lombok.experimental.UtilityClass;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.data.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@UtilityClass
public class IslandUtil {

    public Island getIsland(int[] id){
        return Skyblock.get().getIslandHandler().getGrid().get(id);
    }
    public int[] getId(String identifier) {

        if (identifier == null
                || identifier.length() <= 0
                || identifier.equalsIgnoreCase("null"))
            return new int[]{-1, -1};

        String[] split = identifier.split(";");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }

    public Island getIslandAtLocation(Location location){
        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {
                Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);
                if (island != null && island.isInside(location))
                    return island;
            }
        }
        return null;
    }

    public Island getIslandAtXZ(double x, double z){
        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {
                Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);
                if (island != null) {
                    if (island.isInside(x, z))
                        return island;
                }
            }
        }
        return null;
    }

    public String idToString(int[] id){
        return id[0] + ";" + id[1];
    }

    public IslandBlockData getIslandBlockDataFromPos(Island island, Location location) {
        for (IslandBlockData blockData : island.getStoredBlocks()) {
            if (blockData.getPosition().getBlockX() == location.getBlockX()
                    && blockData.getPosition().getBlockY() == location.getBlockY()
                    && blockData.getPosition().getBlockZ() == location.getBlockZ())
                return blockData;
        }
        return null;
    }

    public Island getIslandFromOwnerName(String leader){
        UUID uuid = Bukkit.getOfflinePlayer(leader).getUniqueId();
        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {
                Island island = Skyblock.get().getIslandHandler().getGrid().get(r, c);
                if (island.getLeader().getUuid().equals(uuid))
                    return island;
            }
        }
        return null;
    }

    public String printGrid(){
        StringBuilder gridPrint = new StringBuilder();

        for (int r = 0; r < Skyblock.get().getIslandHandler().getGrid().length(); r++) {
            for (int c = 0; c < Skyblock.get().getIslandHandler().getGrid().width(r); c++) {
                gridPrint.append((Skyblock.get().getIslandHandler().getGrid().get(r, c) == null ? "o " : "x "));
            }
            gridPrint.append("\n");
        }

        return gridPrint.toString();
    }

}
