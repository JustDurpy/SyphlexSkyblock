package net.syphlex.skyblock.manager.island.settings;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.manager.island.settings.impl.IslandBorderColor;
import net.syphlex.skyblock.manager.island.settings.impl.IslandSetting;
import net.syphlex.skyblock.manager.island.settings.impl.IslandTimeLock;
import net.syphlex.skyblock.manager.island.settings.impl.IslandWeatherLock;

@Getter
@Setter
public class IslandSettings {

    private final IslandSetting allowVisitors = new IslandSetting("Allow Visitors", true);
    private IslandTimeLock timeLock = IslandTimeLock.NONE;
    private IslandWeatherLock weatherLock = IslandWeatherLock.NONE;
    private IslandBorderColor islandBorderColor = IslandBorderColor.BLUE;
}
