package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandUpgradeGui extends SimpleGui {

    private final Island island;

    public IslandUpgradeGui(Island island) {
        super("Island Upgrades", 27);

        this.island = island;

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        /*
        todo let player know if they can afford the upgrade IN THE GUI
         */

        this.inventory.setItem(10, new ItemBuilder()
                .setMaterial(Material.WHEAT)
                .setName("&e&lHarvest Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade the rate at which",
                        "  &fthe crops on your island grow.",
                        "",
                        " &6➥ &e&lMultiplier: &fx" + island.getUpgrades().getHarvestMult(),
                        " &6➥ &e&lNext Upgrade: &fx" + island.getUpgrades().getNextHarvestMultUpgrade(),
                        "",
                        "&e&lUpgrades",
                        " &6- &fx1.0 multiplier",
                        " &6- &fx1.25 multiplier",
                        " &6- &f1.75x multiplier",
                        " &6- &f2.5x multiplier",
                        "",
                        " &6➥ &e&lUpgrade Cost: &f$" + String.format("%,d", island.getUpgrades().getUpgradeCostForHarvestMult()),
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"))
                .build());

        this.inventory.setItem(11, new ItemBuilder()
                .setMaterial(Material.SPAWNER)
                .setName("&e&lSpawner Rate Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade the rate at which",
                        "  &fspawners on your island spawn mobs.",
                        "",
                        " &6➥ &e&lMultiplier: &fx" + island.getUpgrades().getSpawnRateMult(),
                        " &6➥ &e&lNext Upgrade: &fx" + island.getUpgrades().getNextSpawnRateMultUpgrade(),
                        "",
                        "&e&lUpgrades",
                        " &6- &fx1.0 multiplier",
                        " &6- &fx1.25 multiplier",
                        " &6- &f1.75x multiplier",
                        " &6- &f2.5x multiplier",
                        "",
                        " &6➥ &e&lUpgrade Cost: &f$" + String.format("%,d", island.getUpgrades().getUpgradeCostForSpawnRateMult()),
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"))
                .build());

        this.inventory.setItem(12, new ItemBuilder()
                .setMaterial(Material.ZOMBIE_HORSE_SPAWN_EGG)
                .setName("&e&lMob Spawning Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade the amount of mobs",
                        "  &fspawn on your island.",
                        "",
                        " &6➥ &e&lMultiplier: &fx" + island.getUpgrades().getSpawnAmtMult(),
                        " &6➥ &e&lNext Upgrade: &fx" + island.getUpgrades().getNextSpawnAmtMultUpgrade(),
                        "",
                        "&e&lUpgrades",
                        " &6- &fx1.0 multiplier",
                        " &6- &fx1.25 multiplier",
                        " &6- &f1.75x multiplier",
                        " &6- &f2.5x multiplier",
                        "",
                        " &6➥ &e&lUpgrade Cost: &f$" + String.format("%,d", island.getUpgrades().getUpgradeCostForSpawnAmtMult()),
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"
                ))
                .build());

        this.inventory.setItem(14, new ItemBuilder()
                .setMaterial(Material.BARRIER)
                .setName("&e&lIsland Size Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade the size of your island.",
                        "",
                        " &6➥ &e&lSize: &fx" + (int)island.getUpgrades().getSize()
                                + "x" + (int)island.getUpgrades().getSize(),
                        " &6➥ &e&lNext Size: &f" + island.getUpgrades().getNextSizeUpgrade()
                                + "x" + island.getUpgrades().getNextSizeUpgrade(),
                        "",
                        "&e&lSizes",
                        " &6- &f25x25",
                        " &6- &f50x50",
                        " &6- &f100x100",
                        " &6- &f150x150",
                        "",
                        " &6➥ &e&lUpgrade Cost: &f$" + String.format("%,d", island.getUpgrades().getUpgradeCostForSizeUpgrade()),
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"
                ))
                .build());

        this.inventory.setItem(15, new ItemBuilder()
                .setMaterial(Material.PLAYER_HEAD)
                .setName("&e&lMax Members Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade the max member count",
                        "  &fon your island.",
                        "",
                        " &6➥ &e&lMax Members: &f" + island.getUpgrades().getMaxMembers() + " Members",
                        " &6➥ &e&lNext Upgrade: &f" + island.getUpgrades().getNextMemberUpgrade() + " Members",
                        "",
                        "&e&lUpgrades",
                        " &6- &f4 Members",
                        " &6- &f8 Members",
                        " &6- &f10 Members",
                        " &6- &f12 Members",
                        "",
                        " &6➥ &e&lUpgrade Cost: &f$" + String.format("%,d", island.getUpgrades().getUpgradeCostForMaxMembers()),
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"
                ))
                .build());

        this.inventory.setItem(16, new ItemBuilder()
                .setMaterial(Material.DIAMOND_ORE)
                .setName("&e&lOre Generator Upgrade")
                .setGlowing(true)
                .setLore(Arrays.asList(
                        "&f&m------------------------",
                        "",
                        "&e&lDescription",
                        "  &fUpgrade your island ore generator.",
                        "",
                        " &6➥ &e&lTier: &f" + StringUtil.intToRoman(island.getUpgrades().getGenerator().getTier()),
                        " &6➥ &e&lNext Upgrade: &ftodo",
                        "",
                        "&e&lUpgrades",
                        " &6- &fRight click to view generators...",
                        "",
                        " &6➥ &e&lUpgrade Cost: &ftodo",
                        "",
                        "&7&o(( Left click to upgrade ))",
                        "",
                        "&f&m------------------------"
                ))
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();
        final Player p = profile.getPlayer();

        int upgradeCost;

        switch (e.getSlot()) {
            case 10:
                /*
                Island harvest upgrade
                 */

                upgradeCost = island.getUpgrades().getUpgradeCostForHarvestMult();

                if (!Skyblock.economy().has(p, upgradeCost)) {
                    Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                    return;
                } else {

                    closeInventory(p);

                    Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
                    Skyblock.economy().withdrawPlayer(p, upgradeCost);

                    island.getUpgrades().setHarvestMult(island.getUpgrades().getNextHarvestMultUpgrade());
                }

                break;
            case 11:
                /*
                Island mob spawn rate upgrade
                 */

                upgradeCost = island.getUpgrades().getUpgradeCostForSpawnRateMult();

                if (!Skyblock.economy().has(p, upgradeCost)) {
                    Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                    return;
                } else {

                    closeInventory(p);

                    Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
                    Skyblock.economy().withdrawPlayer(p, upgradeCost);

                    island.getUpgrades().setSpawnRateMult(island.getUpgrades().getNextSpawnRateMultUpgrade());
                }

                break;
            case 12:
                /*
                Island mob spawn amount upgrade
                 */

                upgradeCost = island.getUpgrades().getUpgradeCostForSpawnAmtMult();

                if (!Skyblock.economy().has(p, upgradeCost)) {
                    Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                    return;
                } else {

                    closeInventory(p);

                    Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
                    Skyblock.economy().withdrawPlayer(p, upgradeCost);

                    island.getUpgrades().setSpawnAmtMult(island.getUpgrades().getNextSpawnAmtMultUpgrade());
                }

                break;
            case 14:
                /*
                Island size upgrade
                 */

                upgradeCost = island.getUpgrades().getUpgradeCostForSizeUpgrade();

                if (!Skyblock.economy().has(p, upgradeCost)) {
                    Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                    return;
                } else {

                    closeInventory(p);

                    Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
                    Skyblock.economy().withdrawPlayer(p, upgradeCost);

                    island.getUpgrades().setSize(island.getUpgrades().getNextSizeUpgrade());
                    island.refreshBorder(p, Color.BLUE);
                }

                break;
            case 15:
                /*
                Island Max Members upgrade
                 */

                upgradeCost = island.getUpgrades().getUpgradeCostForMaxMembers();

                if (!Skyblock.economy().has(p, upgradeCost)) {
                    Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                    return;
                } else {

                    closeInventory(p);

                    Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
                    Skyblock.economy().withdrawPlayer(p, upgradeCost);

                    island.getUpgrades().setMaxMembers(island.getUpgrades().getNextMemberUpgrade());
                }
                break;
            case 16:
                /*
                Island generator upgrade
                 */

                break;
        }
    }
}
