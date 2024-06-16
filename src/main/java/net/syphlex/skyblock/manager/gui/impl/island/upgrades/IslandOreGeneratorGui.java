package net.syphlex.skyblock.manager.gui.impl.island.upgrades;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.simple.SimpleGui;

import java.util.List;

public class IslandOreGeneratorGui extends SimpleGui {

    private final Island island;

    public IslandOreGeneratorGui(final Profile profile, final Island island) {
        super(ConfigMenu.GENERATOR_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.GENERATOR_MENU.getMenuSetting().getMenuSize());

        this.island = island;

        fill(ConfigMenu.GENERATOR_MENU);

        for (GuiItem guiItem : ConfigMenu.GENERATOR_MENU.getMenuSetting().getItems()) {

            if (!guiItem.hasMultipleItems()) {
                setItem(guiItem.getItem(), guiItem.getSlot());
                continue;
            }

            ItemBuilder actualItem = null;

            if (!island.getUpgrades().getGenerator().canUpgrade()) {
                // maxed out
                actualItem = getPair(guiItem.getItems(), "already-upgraded").getY();
            } else if (island.getUpgrades().getGenerator().canUpgrade()
                    && !Skyblock.economy().has(profile.getPlayer(),
                    island.getUpgrades().getGenerator().getNextCost())) {
                // broke asf boi tryna buy
                actualItem = getPair(guiItem.getItems(), "cannot-upgrade").getY();
            } else {
                // rich asf boy tryna buy
                actualItem = getPair(guiItem.getItems(), "can-upgrade").getY();
            }

            if (actualItem == null) continue;

            setItem(actualItem.build(), guiItem.getSlot());
        }
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (this.island == null) return;

        for (GuiItem guiItem : ConfigMenu.GENERATOR_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            // generator name : guiItem.stringId();

            IslandOreGenerator generator = Skyblock.get().getUpgradeHandler().getOreGenerator(guiItem.getStringId());

            if (generator == null) {
                // todo error log
                closeInventory(profile.getPlayer());
                break;
            }

            // todo fix these messages lol

            if (!island.getUpgrades().getGenerator().canUpgrade()) {
                // maxed out
                //actualItem = getPair(guiItem.items(), "already-upgraded").getY();
                profile.sendMessage("bro you too rich for this shit. you already own it cuh");
            } else if (island.getUpgrades().getGenerator().canUpgrade()
                    && !Skyblock.economy().has(profile.getPlayer(),
                    island.getUpgrades().getGenerator().getNextCost())) {
                // broke asf boi tryna buy
                //actualItem = getPair(guiItem.items(), "cannot-upgrade").getY();
                profile.sendMessage("you cant afford it poor little boyyy");
            } else {
                // rich asf boy tryna buy
                //actualItem = getPair(guiItem.items(), "can-upgrade").getY();
                profile.sendMessage("you bought it!");
            }
            break;
        }

    }

    private Pair<String, ItemBuilder> getPair(List<Pair<String, ItemBuilder>> list, String match){
        for (Pair<String, ItemBuilder> pair : list) {
            if (pair.getX().equalsIgnoreCase(match))
                return pair;
        }
        return list.get(0);
    }
}
