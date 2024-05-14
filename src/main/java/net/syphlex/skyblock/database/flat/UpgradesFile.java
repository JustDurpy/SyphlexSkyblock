package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.upgrade.UpgradeObject;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class UpgradesFile extends SimpleConfig {

    private UpgradeObject harvestUpgrade, spawnRateUpgrade,
            spawnAmountUpgrade, islandSizeUpgrade,
            teamSizeUpgrade, generatorUpgrade;

    private final ArrayList<String> maxedOutLore = new ArrayList<>();

    public UpgradesFile() {
        super("/upgrades.yml", false);
    }

    public void read(){
        setupDefaultConfig();

        double[] harvestCosts, harvestValues;
        double[] spawnRateCosts, spawnRateValues;
        double[] spawnAmountCosts, spawnAmountValues;
        double[] islandSizeCosts, islandSizeValues;
        double[] teamSizeCosts, teamSizeValues;
        double[] generatorCosts, generatorValues;

        for (String upgradeSection : config.getConfigurationSection("upgrades").getKeys(false)) {

            int i = 0;
            for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false))
                i++;

            boolean enabled = config.getBoolean("upgrades." + upgradeSection + ".enabled");

            String itemName = config.getString("upgrades." + upgradeSection + ".gui-item.name");
            Material itemMaterial = PluginUtil.getMaterial(config.getString("upgrades." + upgradeSection + ".gui-item.material"));
            int itemSlot = config.getInt("upgrades." + upgradeSection + ".gui-item.slot");
            List<String> itemLore = config.getStringList("upgrades." + upgradeSection + ".gui-item.lore");

            GuiItem guiItem = new GuiItem(new ItemBuilder()
                    .setMaterial(itemMaterial)
                    .setName(itemName)
                    .setGlowing(true)
                    .setLore(itemLore)
                    .build(), itemSlot);

            switch (upgradeSection) {
                case "harvest":

                    harvestCosts = new double[i+1];
                    harvestValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        harvestCosts[level] = cost;
                        harvestValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    harvestValues[0] = ConfigEnum.DEFAULT_ISLAND_HARVESTRATE.getAsDouble();

                    this.harvestUpgrade = new UpgradeObject(enabled, harvestCosts, harvestValues, guiItem);
                    break;
                case "spawner-rate":

                    spawnRateCosts = new double[i+1];
                    spawnRateValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        spawnRateCosts[level] = cost;
                        spawnRateValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    spawnRateValues[0] = ConfigEnum.DEFAULT_ISLAND_SPAWNRATE.getAsDouble();

                    this.spawnRateUpgrade = new UpgradeObject(enabled, spawnRateCosts, spawnRateValues, guiItem);
                    break;
                case "spawner-amount":

                    spawnAmountCosts = new double[i+1];
                    spawnAmountValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        spawnAmountCosts[level] = cost;
                        spawnAmountValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    spawnAmountValues[0] = ConfigEnum.DEFAULT_ISLAND_SPAWN_AMOUNT_RATE.getAsDouble();

                    this.spawnAmountUpgrade = new UpgradeObject(enabled, spawnAmountCosts, spawnAmountValues, guiItem);
                    break;
                case "island-size":

                    islandSizeCosts = new double[i+1];
                    islandSizeValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        islandSizeCosts[level] = cost;
                        islandSizeValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    islandSizeValues[0] = ConfigEnum.DEFAULT_ISLAND_SIZE.getAsDouble();

                    this.islandSizeUpgrade = new UpgradeObject(enabled, islandSizeCosts, islandSizeValues, guiItem);
                    break;
                case "team-size":

                    teamSizeCosts = new double[i+1];
                    teamSizeValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        teamSizeCosts[level] = cost;
                        teamSizeValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    teamSizeValues[0] = ConfigEnum.DEFAULT_ISLAND_MAX_MEMBERS.getAsInteger();

                    this.teamSizeUpgrade = new UpgradeObject(enabled, teamSizeCosts, teamSizeValues, guiItem);
                    break;
                case "generator":

                    generatorCosts = new double[i+1];
                    generatorValues = new double[i+1];

                    for (String levelSection : config.getConfigurationSection("upgrades." + upgradeSection + ".levels").getKeys(false)) {

                        int level = Integer.parseInt(levelSection);
                        double cost = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".cost");
                        double value = config.getDouble("upgrades." + upgradeSection + ".levels." + levelSection + ".value");

                        generatorCosts[level] = cost;
                        generatorValues[level] = value;
                    }

                    // sets the default value for the upgrade
                    generatorValues[0] = ConfigEnum.DEFAULT_ISLAND_GENERATOR_TIER.getAsDouble();

                    this.generatorUpgrade = new UpgradeObject(enabled, generatorCosts, generatorValues, guiItem);
                    break;
            }
        }

        this.maxedOutLore.addAll(config.getStringList("cannot-upgrade.lore"));
    }

    private void setupDefaultConfig(){
        config.options().copyDefaults(true);
        config.addDefault("cannot-upgrade.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&cYou cannot upgrade this anymore.",
                "&cYour island has reached this upgrades",
                "&chighest level.",
                "",
                " &6➥ &e&lValue: &f%upgrade%",
                "",
                "&f&m------------------------"
        ));
        config.addDefault("upgrades.harvest.enabled", true);
        config.addDefault("upgrades.harvest.gui-item.name", "&e&lHarvest Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.harvest.gui-item.material", "WHEAT");
        config.addDefault("upgrades.harvest.gui-item.slot", 10);
        config.addDefault("upgrades.harvest.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade the rate at which",
                "  &fthe crops on your island grow.",
                "",
                " &6➥ &e&lMultiplier: &fx%upgrade%",
                " &6➥ &e&lNext Upgrade: &fx%next_upgrade%",
                "",
                "&e&lUpgrades",
                " &6- &fx1.5 multiplier",
                " &6- &fx2.0 multiplier",
                " &6- &fx2.5 multiplier",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "",
                "&f&m------------------------"));
        config.addDefault("upgrades.harvest.levels.1.value", 1.5);
        config.addDefault("upgrades.harvest.levels.1.cost", 15000);
        config.addDefault("upgrades.harvest.levels.2.value", 2.0);
        config.addDefault("upgrades.harvest.levels.2.cost", 25000);
        config.addDefault("upgrades.harvest.levels.3.value", 2.5);
        config.addDefault("upgrades.harvest.levels.3.cost", 50000);

        config.addDefault("upgrades.spawner-rate.enabled", true);
        config.addDefault("upgrades.spawner-rate.gui-item.name", "&e&lSpawner Rate Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.spawner-rate.gui-item.material", "SPAWNER");
        config.addDefault("upgrades.spawner-rate.gui-item.slot", 11);
        config.addDefault("upgrades.spawner-rate.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade the rate at which",
                "  &fspawners on your island spawn mobs.",
                "",
                " &6➥ &e&lMultiplier: &fx%upgrade%",
                " &6➥ &e&lNext Upgrade: &fx%next_upgrade%",
                "",
                "&e&lUpgrades",
                " &6- &fx1.5 multiplier",
                " &6- &fx2.0 multiplier",
                " &6- &fx2.5 multiplier",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "",
                "&f&m------------------------"));
        config.addDefault("upgrades.spawner-rate.levels.1.value", 1.5);
        config.addDefault("upgrades.spawner-rate.levels.1.cost", 20000);
        config.addDefault("upgrades.spawner-rate.levels.2.value", 2.0);
        config.addDefault("upgrades.spawner-rate.levels.2.cost", 50000);
        config.addDefault("upgrades.spawner-rate.levels.3.value", 2.5);
        config.addDefault("upgrades.spawner-rate.levels.3.cost", 80000);

        config.addDefault("upgrades.spawner-amount.enabled", true);
        config.addDefault("upgrades.spawner-amount.gui-item.name", "&e&lSpawner Rate Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.spawner-amount.gui-item.material", "ZOMBIE_HORSE_SPAWN_EGG");
        config.addDefault("upgrades.spawner-amount.gui-item.slot", 12);
        config.addDefault("upgrades.spawner-amount.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade the amount of mobs",
                "  &fspawn on your island.",
                "",
                " &6➥ &e&lMultiplier: &fx%upgrade%",
                " &6➥ &e&lNext Upgrade: &fx%next_upgrade%",
                "",
                "&e&lUpgrades",
                " &6- &fx1.5 multiplier",
                " &6- &fx2.0 multiplier",
                " &6- &fx2.5 multiplier",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "",
                "&f&m------------------------"));
        config.addDefault("upgrades.spawner-amount.levels.1.value", 1.5);
        config.addDefault("upgrades.spawner-amount.levels.1.cost", 25000);
        config.addDefault("upgrades.spawner-amount.levels.2.value", 2.0);
        config.addDefault("upgrades.spawner-amount.levels.2.cost", 65000);
        config.addDefault("upgrades.spawner-amount.levels.3.value", 2.5);
        config.addDefault("upgrades.spawner-amount.levels.3.cost", 100000);

        config.addDefault("upgrades.island-size.enabled", true);
        config.addDefault("upgrades.island-size.gui-item.name", "&e&lIsland Size Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.island-size.gui-item.material", "BARRIER");
        config.addDefault("upgrades.island-size.gui-item.slot", 14);
        config.addDefault("upgrades.island-size.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade the size of your island.",
                "",
                " &6➥ &e&lSize: &f%upgrade%x%upgrade%",
                " &6➥ &e&lNext Size: &f%next_upgrade%x%next_upgrade%",
                "",
                "&e&lSizes",
                " &6- &f50x50",
                " &6- &f100x100",
                " &6- &f150x150",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "",
                "&f&m------------------------"));
        config.addDefault("upgrades.island-size.levels.1.value", 50);
        config.addDefault("upgrades.island-size.levels.1.cost", 75000);
        config.addDefault("upgrades.island-size.levels.2.value", 100);
        config.addDefault("upgrades.island-size.levels.2.cost", 150000);
        config.addDefault("upgrades.island-size.levels.3.value", 150);
        config.addDefault("upgrades.island-size.levels.3.cost", 350000);

        config.addDefault("upgrades.team-size.enabled", true);
        config.addDefault("upgrades.team-size.gui-item.name", "&e&lTeam Size Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.team-size.gui-item.material", "PLAYER_HEAD");
        config.addDefault("upgrades.team-size.gui-item.slot", 15);
        config.addDefault("upgrades.team-size.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade the team size of your island.",
                "",
                " &6➥ &e&lMax Members: &f%upgrade% Members",
                " &6➥ &e&lNext Upgrade: &f%next_upgrade% Members",
                "",
                "&e&lUpgrades",
                " &6- &f8 Members",
                " &6- &f10 Members",
                " &6- &f12 Members",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "",
                "&f&m------------------------"
        ));
        config.addDefault("upgrades.team-size.levels.1.value", 8);
        config.addDefault("upgrades.team-size.levels.1.cost", 10000);
        config.addDefault("upgrades.team-size.levels.2.value", 10);
        config.addDefault("upgrades.team-size.levels.2.cost", 20000);
        config.addDefault("upgrades.team-size.levels.3.value", 12);
        config.addDefault("upgrades.team-size.levels.3.cost", 30000);

        config.addDefault("upgrades.generator.enabled", true);
        config.addDefault("upgrades.generator.gui-item.name", "&e&lOre Generator Upgrade &7(Lvl %level%)");
        config.addDefault("upgrades.generator.gui-item.material", "DIAMOND_ORE");
        config.addDefault("upgrades.generator.gui-item.slot", 16);
        config.addDefault("upgrades.generator.gui-item.lore", Arrays.asList(
                "&f&m------------------------",
                "",
                "&e&lDescription",
                "  &fUpgrade your island's ore generator.",
                "",
                " &6➥ &e&lGenerator Tier: &f%upgrade%",
                " &6➥ &e&lNext Tier: &f%next_upgrade%",
                "",
                " &6➥ &e&lUpgrade Cost: &f$%cost%",
                "",
                "&7&o(( Left click to upgrade ))",
                "&7&o(( Right click to view generators ))",
                "",
                "&f&m------------------------"
        ));
        config.addDefault("upgrades.generator.levels.1.value", 1);
        config.addDefault("upgrades.generator.levels.1.cost", 10000);

        save();
    }
}
