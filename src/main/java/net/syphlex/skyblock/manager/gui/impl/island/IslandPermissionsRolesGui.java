package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandPermissionsRolesGui extends SimpleGui {

    public IslandPermissionsRolesGui() {
        super("Select A Role", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        this.inventory.setItem(11, new ItemBuilder()
                .setMaterial(Material.LIGHT_GRAY_DYE)
                .setGlowing(true)
                .setName("&fVisitors Permissions")
                .setLore(Arrays.asList(
                        "",
                        "&7&o(( &f&oClick &7&oto edit island permissions for &f&oVisitors&7&o ))",
                        ""))
                .build());

        this.inventory.setItem(13, new ItemBuilder()
                .setMaterial(Material.GRAY_DYE)
                .setGlowing(true)
                .setName("&7Member Role")
                .setLore(Arrays.asList(
                        "",
                        "&7&o(( &f&oClick &7&oto edit island permissions for the &f&oMember&7&o role ))",
                        ""))
                .build());

        this.inventory.setItem(15, new ItemBuilder()
                .setMaterial(Material.PURPLE_DYE)
                .setGlowing(true)
                .setName("&dModerator Role")
                .setLore(Arrays.asList(
                        "",
                        "&7&o(( &f&oClick &7&oto edit island permissions for the &f&oModerator&7&o role ))",
                        ""))
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (!profile.isIslandLeader())
            return;

        switch (e.getSlot()) {
            case 11:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsGui(profile.getIsland().getVisitorRole()));
                break;
            case 13:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsGui(profile.getIsland().getMemberRole()));
                break;
            case 15:
                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsGui(profile.getIsland().getModeratorRole()));
                break;
        }
    }
}
