package net.syphlex.skyblock.manager.island.upgrade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.IslandUpgradeGui;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.util.config.ConfigEnum;

import java.util.ArrayList;

@Getter
public class IslandUpgradeData {
    private final UpgradeObject islandSize;
    private final UpgradeObject spawnRate;
    private final UpgradeObject spawnAmount;
    private final UpgradeObject harvest;
    private final UpgradeObject teamSize;
    private final UpgradeObject generator;
    private final UpgradeObject voidChest;
    private final ArrayList<UpgradeObject> list = new ArrayList<>();

    public IslandUpgradeData(UpgradeObject islandSize,
                             UpgradeObject spawnRate,
                             UpgradeObject spawnAmount,
                             UpgradeObject harvest,
                             UpgradeObject teamSize,
                             UpgradeObject generator,
                             UpgradeObject voidChest){
        this.islandSize = islandSize;
        this.spawnRate = spawnRate;
        this.spawnAmount = spawnAmount;
        this.harvest = harvest;
        this.teamSize = teamSize;
        this.generator = generator;
        this.voidChest = voidChest;
        list.add(this.islandSize);
        list.add(this.spawnRate);
        list.add(this.spawnAmount);
        list.add(this.harvest);
        list.add(this.teamSize);
        list.add(this.generator);
        list.add(this.voidChest);
    }
}
