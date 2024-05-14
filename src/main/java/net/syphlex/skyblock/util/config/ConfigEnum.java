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
    DEFAULT_ISLAND_GENERATOR_TIER(0),
    DEFAULT_Y_POSITION(100),
    SCOREBOARD_TITLE("&5&lSyphlex &fSkyblock"),
    ISLAND_SCOREBOARD_LINES(Arrays.asList(
            "&f&m-------------------",
            "&5&lYou",
            " &d➥ &fMoney: &7$%vault_eco_balance_formatted%",
            " &d➥ &fMob Coins: &7%skyblock_player_mobcoins%⛁",
            "",
            "&5&lIsland",
            " &d➥ &fLeader: &7%skyblock_island_leader%",
            " &d➥ &fWorth: &7$%skyblock_island_worth%",
            " &d➥ &fLevel: &7%skyblock_island_level%",
            " &d➥ &fMembers: &7%skyblock_island_team_size%/%skyblock_island_max_team_size%",
            " &d➥ &fSize: &7%skyblock_island_size%x%skyblock_island_size%",
            "&f&m-------------------",
            "&7play.syphlex.net")),
    NORMAL_SCOREBOARD(Arrays.asList(
            "&f&m---------------------",
            "&7You do not have an island.",
            "&7create an island to get started",
            "&7by typing &n/island create.",
            "&f&m---------------------",
            "&7play.syphlex.net"));

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
