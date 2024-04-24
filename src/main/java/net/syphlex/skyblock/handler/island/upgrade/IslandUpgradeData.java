package net.syphlex.skyblock.handler.island.upgrade;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.util.config.ConfigEnum;

@Getter
@Setter
public class IslandUpgradeData {
    private double size = ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble();
    private double spawnRate = ConfigEnum.DEFAULT_ISLAND_SPAWNRATE.getAsDouble();
    private double harvestRate = ConfigEnum.DEFAULT_ISLAND_HARVESTRATE.getAsDouble();
    private int maxMembers = ConfigEnum.DEFAULT_ISLAND_MAX_MEMBERS.getAsInteger();
    private IslandOreGenerator generator = Skyblock.get().getUpgradeHandler().getOreGenerator(ConfigEnum.DEFAULT_ISLAND_GENERATOR_TIER.getAsInteger());

}
