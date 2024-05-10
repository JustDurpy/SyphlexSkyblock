package net.syphlex.skyblock.manager.island.upgrade;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.util.config.ConfigEnum;

@Getter
@Setter
public class IslandUpgradeData {
    private double size = ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble();
    private double spawnRateMult = ConfigEnum.DEFAULT_ISLAND_SPAWNRATE.getAsDouble();
    private double spawnAmtMult = ConfigEnum.DEFAULT_ISLAND_SPAWN_AMOUNT_RATE.getAsDouble();
    private double harvestMult = ConfigEnum.DEFAULT_ISLAND_HARVESTRATE.getAsDouble();
    private int maxMembers = ConfigEnum.DEFAULT_ISLAND_MAX_MEMBERS.getAsInteger();
    private IslandOreGenerator generator = Skyblock.get().getUpgradeHandler().getOreGenerator(ConfigEnum.DEFAULT_ISLAND_GENERATOR_TIER.getAsInteger());

    public int getNextMemberUpgrade(){
        switch (maxMembers){
            case 4:
                return 8;
            case 8:
                return 10;
            case 10:
                return 12;
        }
        return -1;
    }

    public int getUpgradeCostForMaxMembers(){
        switch (maxMembers){
            case 4:
                return 10000;
            case 8:
                return 20000;
            case 10:
                return 30000;
        }
        return -1;
    }

    public double getNextHarvestMultUpgrade(){
        if (harvestMult == 1.0)
            return 1.25;
        if (harvestMult == 1.25)
            return 1.75;
        if (harvestMult == 1.75)
            return 2.5;
        return -1;
    }

    public int getUpgradeCostForHarvestMult(){
        if (harvestMult == 1.0)
            return 15000;
        if (harvestMult == 1.25)
            return 25000;
        if (harvestMult == 1.75)
            return 50000;
        return -1;
    }

    public double getNextSpawnAmtMultUpgrade(){
        if (spawnAmtMult == 1.0)
            return 1.25;
        if (spawnAmtMult == 1.25)
            return 1.75;
        if (spawnAmtMult == 1.75)
            return 2.5;
        return -1;
    }

    public int getUpgradeCostForSpawnAmtMult(){
        if (spawnAmtMult == 1.0)
            return 25000;
        if (spawnAmtMult == 1.25)
            return 65000;
        if (spawnAmtMult == 1.75)
            return 95000;
        return -1;
    }

    public double getNextSpawnRateMultUpgrade(){
        if (spawnRateMult == 1.0)
            return 1.25;
        if (spawnRateMult == 1.25)
            return 1.75;
        if (spawnRateMult == 1.75)
            return 2.5;
        return -1;
    }

    public int getUpgradeCostForSpawnRateMult(){
        if (spawnRateMult == 1.0)
            return 20000;
        if (spawnRateMult == 1.25)
            return 50000;
        if (spawnRateMult == 1.75)
            return 80000;
        return -1;
    }

    public int getNextSizeUpgrade(){
        switch ((int) this.size) {
            case 25:
                return 50;
            case 50:
                return 100;
            case 100:
                return 150;
        }
        return -1;
    }

    public int getUpgradeCostForSizeUpgrade(){
        switch ((int)this.size){
            case 25:
                return 75000;
            case 50:
                return 150000;
            case 100:
                return 350000;
        }
        return -1;
    }
}
