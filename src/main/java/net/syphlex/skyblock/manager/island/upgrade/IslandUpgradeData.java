package net.syphlex.skyblock.manager.island.upgrade;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class IslandUpgradeData {
    private final UpgradeObject islandSize;
    private final UpgradeObject spawnRate;
    private final UpgradeObject spawnAmount;
    private final UpgradeObject harvest;
    private final UpgradeObject teamSize;
    private final UpgradeObject generator;
    private final ArrayList<UpgradeObject> list = new ArrayList<>();

    public IslandUpgradeData(UpgradeObject islandSize,
                             UpgradeObject spawnRate,
                             UpgradeObject spawnAmount,
                             UpgradeObject harvest,
                             UpgradeObject teamSize,
                             UpgradeObject generator){
        this.islandSize = islandSize;
        this.spawnRate = spawnRate;
        this.spawnAmount = spawnAmount;
        this.harvest = harvest;
        this.teamSize = teamSize;
        this.generator = generator;
        list.add(this.islandSize);
        list.add(this.spawnRate);
        list.add(this.spawnAmount);
        list.add(this.harvest);
        list.add(this.teamSize);
        list.add(this.generator);
    }
}
