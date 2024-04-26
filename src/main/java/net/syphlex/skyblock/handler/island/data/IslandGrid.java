package net.syphlex.skyblock.handler.island.data;

import net.syphlex.skyblock.Skyblock;

public class IslandGrid {

    private Island[][] grid;

    public IslandGrid(int size){
        this.grid = new Island[size][size];
    }

    public void insert(Island island){

        //int[] nextSpot = getNextSpot();
        int[] id = Skyblock.get().getIslandHandler().getId(island.getIdentifier());

        /*
        free[0] is the row of the free spot found
        free[1] is the column of the free spot found
         */
        final int row = id[0];
        final int column = id[1];

        this.grid[row][column] = island;
    }

    public void insert(Island island, int[] nextSpot){

        /*
        free[0] is the row of the free spot found
        free[1] is the column of the free spot found
         */
        final int row = nextSpot[0];
        final int column = nextSpot[1];

        this.grid[row][column] = island;
    }

    public int[] getNextSpot(){
        /*
        Find the next free spot
         */
        int[] freeSpot = getFreeSpot();

        /*
        If there is no free spot found in the virtual grid
        we will bruteforce a free spot by resizing the grid
         */

        while (freeSpot[0] == -1 || freeSpot[1] == -1) {
            resize();
            freeSpot = getFreeSpot();
        }

        return freeSpot;
    }

    public int[] getFreeSpot(){
        /*
        when free[0] or free[1] equals '-1' there was no free spot found (both should be assigned to n>0)
         */
        int[] free = new int[2];
        free[0] = -1;
        free[1] = -1;
        /*
        We will loop  through the new grid and find any spots that are null
        the first spot we find we will cut the search process
         */
        for (int rows = 0; rows < grid.length; rows++) {
            for (int columns = 0; columns < grid[rows].length; columns++) {
                if (grid[rows][columns] == null) {
                    free[0] = rows;
                    free[1] = columns;
                    break;
                }
            }
        }
        /*
        free[0] = the available row id
        free[1] = the available column id
         */
        return free;
    }

    public void resize(){
        /*
        this method will run everytime there is an island being created
        while our virtual grid is full.

        we will create a new row and column in our grid for future islands as well & that way
        we don't have to spam this method.
         */
        Island[][] newGrid = new Island[grid.length + 1][grid.length + 1];

        /*
        loop through the old virtual grid and fill in the new grid.
         */
        for (int rows = 0; rows < grid.length; rows++) {
            System.arraycopy(grid[rows], 0, newGrid[rows], 0, grid[rows].length);
        }

        /*
        set the current grid to the new grid (with data and free spots)
         */
        this.grid = newGrid;

    }

    public Island[][] getGrid(){
        return this.grid;
    }

    public void set(int r, int c, Island island){
        this.grid[r][c] = island;
    }

    public Island get(int[] id){
        for (int r = 0; r < length(); r++) {
            for (int c = 0; c < length(r); c++) {
                Island i = get(r, c);
                if (i == null) continue;
                if (Skyblock.get().getIslandHandler().getId(i.getIdentifier())[0] == id[0]
                        && Skyblock.get().getIslandHandler().getId(i.getIdentifier())[1] == id[1]) {
                    return i;
                }
            }
        }
        return null;
    }

    public Island get(int r, int c){
        return this.grid[r][c];
    }

    public int length(){
        return this.grid.length;
    }

    public int length(int r){
        return this.grid[r].length;
    }

}
