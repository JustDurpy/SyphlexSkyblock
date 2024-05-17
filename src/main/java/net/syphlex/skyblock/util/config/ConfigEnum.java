package net.syphlex.skyblock.util.config;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.utilities.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ConfigEnum {
    MAIN_WORLD("world"),
    ISLAND_WORLD("Skyblock"),
    ISLAND_NETHER_WORLD("Skyblock_nether"),
    ISLAND_END_WORLD("Skyblock_end"),
    ISLAND_DISTANCE_APART(350),
    DEFAULT_SCHEMATIC_NAME("default.schem"),
    DEFAULT_STARTING_Y_POSITION(100),
    MINIMUM_Y_LIMIT(-64),
    STARTING_ISLAND_SIZE(25),
    STARTING_ISLAND_SPAWN_RATE(1),
    STARTING_ISLAND_SPAWN_AMOUNT(1),
    STARTING_ISLAND_HARVEST_RATE(1),
    STARTING_ISLAND_TEAM_SIZE(4),
    STARTING_ISLAND_GENERATOR_TIER(0),
    STARTING_ISLAND_VOID_CHEST_SIZE(0),
    SCOREBOARD_ENABLED(true),
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
            "&7play.syphlex.net")),
    FEATURES_MOBCOINS(true),
    FEATURES_CUSTOM_ENCHANTS(true),
    FEATURES_BLOCK_STACKER(true),
    FEATURES_MINIONS(true),
    FEATURES_MINES(true);

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

        try {
            return (List<String>) this.object;
        } catch (ClassCastException e) {
            Skyblock.log("Config value was invalid and parsed incorrectly at " + this.name());
        }
        return new ArrayList<>();
    }

    public String getAsString(){
        return this.object.toString();
    }

    public String getAsColoredString(){
        return StringUtil.CC(this.object.toString());
    }

    public boolean getAsBoolean(){
        try {
            return (Boolean)this.object;
        } catch (ClassCastException e) {
            //e.printStackTrace();
            Skyblock.log("Config value was invalid and parsed incorrectly at " + this.name());
        }
        return false;
    }

    public int getAsInteger(){
        try {
            return Integer.parseInt(this.object.toString());
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            Skyblock.log("Config value was invalid and parsed incorrectly at " + this.name());
        }
        return 0;
    }

    public double getAsDouble(){
        try {
            return Double.parseDouble(this.object.toString());
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            Skyblock.log("Config value was invalid and parsed incorrectly at " + this.name());
        }
        return 0;
    }

    public boolean isNumber(){
        try {
            Double.parseDouble(this.object.toString());
            return true;
        } catch (NumberFormatException e) {
            Skyblock.log("Config value was invalid and parsed incorrectly at " + this.name());
            return false;
        }
    }

}
