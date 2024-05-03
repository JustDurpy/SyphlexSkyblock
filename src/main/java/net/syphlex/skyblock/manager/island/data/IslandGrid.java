package net.syphlex.skyblock.manager.island.data;

import net.syphlex.skyblock.util.IslandUtil;
import org.bukkit.Bukkit;

public class IslandGrid {

    private Island[][] grid;

    public IslandGrid(int size){
        this.grid = new Island[size][size];
    }

    public void insert(Island island, int[] spot){
        this.grid[spot[0]][spot[1]] = island;
    }

    public int[] getNextSpot(){

        int[] freeSpot = new int[]{-1, -1};

        for (int r = 0; r < length(); r++) {
            for (int c = 0; c < length(); c++) {
                if (get(r, c) == null) {
                    freeSpot[0] = r;
                    freeSpot[1] = c;
                    break;
                }
            }
        }

        while (freeSpot[0] == -1) {
            resize();
            for (int r = 0; r < length(); r++) {
                for (int c = 0; c < length(); c++) {
                    if (get(r, c) == null) {
                        freeSpot[0] = r;
                        freeSpot[1] = c;
                        break;
                    }
                }
            }
        }

        return freeSpot;
    }

    private void resize(){
        Island[][] resized = new Island[length() + 1][length() + 1];

        for (int r = 0; r < length(); r++) {
            for (int c = 0; c < length(); c++) {
                resized[r][c] = this.grid[r][c];
            }
        }

        this.grid = resized;
    }

    public void set(int r, int c, Island island){
        this.grid[r][c] = island;
    }

    public Island get(int[] id){
        return get(id[0], id[1]);
    }

    public Island get(int r, int c){
        return this.grid[r][c];
    }

    public int length(){
        return this.grid.length;
    }

    public int width(int r){
        return this.grid[r].length;
    }

}
