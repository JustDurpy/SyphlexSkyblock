package net.syphlex.skyblock.util.config;

import net.syphlex.skyblock.util.utilities.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ConfigEnum {
    MAIN_WORLD("world"),
    ISLAND_WORLD("Skyblock"),
    ISLAND_NETHER_WORLD("Skyblock_nether"),
    ISLAND_END_WORLD("Skyblock_end"),
    ISLAND_DISTANCE_APART(300),
    DEFAULT_SCHEMATIC_NAME("TODO"),
    DEFAULT_ISLAND_SIZE(25),
    DEFAULT_ISLAND_SPAWNRATE(1),
    DEFAULT_ISLAND_SPAWN_AMOUNT_RATE(1),
    DEFAULT_ISLAND_HARVESTRATE(1),
    DEFAULT_ISLAND_MAX_MEMBERS(4),
    DEFAULT_ISLAND_GENERATOR_TIER(1),
    DEFAULT_Y_POSITION(100),
    SCOREBOARD_TITLE("&5&lSyphlex Skyblock"),
    ISLAND_SCOREBOARD_LINES(Arrays.asList(
            "&7&m-------------------",
            "&5&lYou",
            " &fMoney: &d$%vault_eco_balance_formatted%",
            "&5&lIsland",
            " &fLeader:%island_leader%",
            "",
            "&7&m-------------------")),
    NORMAL_SCOREBOARD(Arrays.asList(
            "&7&m-------------------",
            "&cNo island found.",
            "&7&m-------------------"));

    private Object object;

    ConfigEnum(Object o){
        this.object = o;
    }

    public Object get(){
        return this.object;
    }

    public void set(Object o){
        this.object = o;
    }

    public List<String> getAsList(){

        if (!(this.object instanceof List))
            return new ArrayList<>();

        return (List<String>) this.object;
    }

    public String getAsString(){
        return this.object.toString();
    }

    public String getAsColoredString(){
        return StringUtil.CC(this.object.toString());
    }

    public int getAsInteger(){
        try {
            return Integer.parseInt(this.object.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getAsDouble(){
        try {
            return Double.parseDouble(this.object.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isNumber(){
        try {
            Double.parseDouble(this.object.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
