package net.syphlex.skyblock.manager.island.settings;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum IslandSettings {
    ALLOW_VISITORS("Allow Visitors", true),
    WEATHER_OFF("No Weather", false),
    ALWAYS_DAY("Always Day", false),
    ALWAYS_NIGHT("Always Night", false),
    ISLAND_BORDER_BLUE("Blue Island Border", true),
    ISLAND_BORDER_RED("Red Island Border", false),
    ISLAND_BORDER_GREEN("Green Island Border", false);

    private final String identifier;
    @Setter private boolean enabled;

    IslandSettings(String identifier, boolean enabled){
        this.identifier = identifier;
        this.enabled = enabled;
    }
}
