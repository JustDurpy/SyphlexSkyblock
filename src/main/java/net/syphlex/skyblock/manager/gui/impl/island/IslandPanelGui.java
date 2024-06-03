package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.settings.IslandSettingsGui;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Material;

public class IslandPanelGui extends SimpleGui {

    public IslandPanelGui() {
        super("Island Panel", 27);

        fill(new ItemBuilder()
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setName(" ")
                .build());

        String[] colors = new String[]{"#A482F8", "#580EAA"};

        this.inventory.setItem(10, new ItemBuilder()
                .setMaterial(Material.GRASS_BLOCK)
                .setName(StringUtil.createGradFromString("Island Home", colors))
                .build());

        this.inventory.setItem(11, new ItemBuilder()
                .setMaterial(Material.BOOK)
                .setName(StringUtil.createGradFromString("Island Information", colors))
                .build());

        this.inventory.setItem(12, new ItemBuilder()
                .setMaterial(Material.DIAMOND)
                .setName(StringUtil.createGradFromString("Top Islands", colors))
                .build());

        this.inventory.setItem(13, new ItemBuilder()
                .setMaterial(Material.CHEST)
                .setName(StringUtil.createGradFromString("Void Chest", colors))
                .build());

        this.inventory.setItem(14, new ItemBuilder()
                .setMaterial(Material.BEACON)
                .setName(StringUtil.createGradFromString("Island Upgrades", colors))
                .build());

        this.inventory.setItem(15, new ItemBuilder()
                .setMaterial(Material.PAPER)
                .setName(StringUtil.createGradFromString("Island Permissions", colors))
                .build());

        this.inventory.setItem(16, new ItemBuilder()
                .setMaterial(Material.REPEATER)
                .setName(StringUtil.createGradFromString("Island Settings", colors))
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(profile);
            return;
        }

        final Island island = profile.getIsland();

        switch (e.getSlot()) {
            case 10:
                closeInventory(profile.getPlayer());
                profile.getIsland().teleport(profile.getPlayer());
                Messages.TELEPORTED_TO_ISLAND.send(profile);
                break;
            case 11:
                profile.getPlayer().sendMessage("in development...");
                break;
            case 12:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandTopGui());
                break;
            case 13:
                profile.getPlayer().sendMessage("in development...");
                break;
            case 14:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandUpgradeGui(island));
                break;
            case 15:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsRolesGui());
                break;
            case 16:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandSettingsGui());
                break;
        }

    }
}
