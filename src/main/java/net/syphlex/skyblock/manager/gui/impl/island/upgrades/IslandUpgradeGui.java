package net.syphlex.skyblock.manager.gui.impl.island.upgrades;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.upgrade.UpgradeObject;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IslandUpgradeGui extends SimpleGui {

    private final Island island;

    public IslandUpgradeGui(Island island) {
        super("Island Upgrades", 27);

        this.island = island;

        fill(new ItemBuilder()
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setName(" ")
                .build());

        /*
        todo let player know if they can afford the upgrade IN THE GUI
         */

        if (island == null) return;

        for (UpgradeObject upgrade : this.island.getUpgrades().getList()) {

            if (!upgrade.isEnabled())
                continue;

            GuiItem guiItem = upgrade.getGuiItem();

            ItemStack itemStack = guiItem.getItem().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null || itemMeta.getLore() == null)
                continue;
            String name = new String(itemMeta.getDisplayName()).replace("%level%", String.valueOf(upgrade.getLevel()));
            List<String> lore = itemMeta.getLore();
            List<String> newLore = new ArrayList<>();

            if (upgrade.canUpgrade()) {
                for (String line : lore) {
                    newLore.add(new String(line).replace("%upgrade%", String.valueOf(upgrade.get()))
                            .replace("%next_upgrade%", String.valueOf(upgrade.getNextValue()))
                            .replace("%cost%", String.valueOf(String.format("%,d", (int) upgrade.getNextCost()))));
                }
            } else {
                for (String line : Skyblock.get().getUpgradeHandler().getUpgradesFile().getMaxedOutLore())
                    newLore.add(StringUtil.CC(line.replace("%upgrade%", String.valueOf(upgrade.get()))));
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(newLore);
            itemStack.setItemMeta(itemMeta);

            this.inventory.setItem(guiItem.getSlot(), itemStack);
        }

        setIslandPanelButton(22);
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();
        final Player p = profile.getPlayer();

        int i = -1;
        for (UpgradeObject upgrade : this.island.getUpgrades().getList()) {
            i++;

            if (!upgrade.isEnabled())
                continue;

            GuiItem guiItem = upgrade.getGuiItem();

            if (e.getSlot() != guiItem.getSlot())
                continue;

            // should be generator menu
            if (i == this.island.getUpgrades().getList().size() - 1) {
                Skyblock.get().getGuiHandler().openGui(profile, new IslandOreGeneratorGui(profile, this.island));
                break;
            }

            // upgrade is maxed out
            if (!upgrade.canUpgrade()) {
                break;
            }

            int cost = (int) upgrade.getNextCost();

            // does not have sufficient funds
            if (!Skyblock.economy().has(p, cost)) {
                Messages.ISLAND_UPGRADE_INSUFFICIENT_FUNDS.send(profile);
                break;
            }

            Skyblock.economy().withdrawPlayer(p, cost);
            Messages.ISLAND_UPGRADE_SUCCESS.send(profile);
            upgrade.setLevel(upgrade.getLevel() + 1);

            Skyblock.get().getGuiHandler().openGui(profile, new IslandUpgradeGui(island));

            this.island.refreshBorder();
        }
    }
}
