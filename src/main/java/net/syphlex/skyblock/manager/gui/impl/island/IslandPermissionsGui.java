package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.permissions.IslandPermission;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class IslandPermissionsGui extends SimpleGui {

    private final IslandRole role;

    public IslandPermissionsGui(IslandRole role) {
        super(role.getIdentifier() + " Permissions:", 27);

        this.role = role;

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        int i = 0;
        for (Pair<IslandPermission, Boolean> pair : role.getPermissions()) {

            IslandPermission permission = pair.getX();
            boolean has = pair.getY();

            List<String> lore = Arrays.asList(
                    "",
                    "&e&lDescription",
                    " &f" + permission.getDescription()
                            .replace("%island_role%", role.getIdentifier()),
                    "",
                    (has
                            ? "&aThis role currently has this permission."
                            : "&cThis role does not have have this permission."),
                    "",
                    "&7&o( ( Click to manipulate this permission ) )",
                    ""
            );

            this.inventory.setItem(i, new ItemBuilder()
                    .setMaterial(has ? Material.LIME_DYE : Material.GRAY_DYE)
                    .setName((has ? "&a" : "&c") + permission.getName())
                    .setLore(lore)
                    .build());

            i++;
        }

        inventory.setItem(22, new ItemBuilder()
                .setMaterial(Material.OAK_SIGN)
                .setName("&c&lGo Back")
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        if (e.getSlot() == 22) {
            Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsRolesGui());
            return;
        }

        int i = 0;
        for (Pair<IslandPermission, Boolean> pair : this.role.getPermissions()) {

            if (e.getSlot() == i) {

                pair.setY(!pair.getY());

                IslandPermission permission = pair.getX();
                boolean has = pair.getY();

                List<String> lore = Arrays.asList(
                        "",
                        "&e&lDescription",
                        " &f" + permission.getDescription()
                                .replace("%island_role%", role.getIdentifier()),
                        "",
                        (has
                                ? "&aThis role currently has this permission."
                                : "&cThis role does not have have this permission."),
                        "",
                        "&7&o( ( Click to manipulate this permission ) )",
                        ""
                );

                this.inventory.setItem(i, new ItemBuilder()
                        .setMaterial(has ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName((has ? "&a" : "&c") + permission.getName())
                        .setLore(lore)
                        .build());

                Skyblock.get().getGuiHandler().openGui(profile, new IslandPermissionsGui(this.role));

                profile.getPlayer().sendMessage(StringUtil.CC(has
                        ? "&aThe " + this.role.getIdentifier() + " role now has the permission '"
                        + permission.getName() + "'."
                        : "&cThe" + this.role.getIdentifier() + " role no longer has the permission '"
                        + permission.getName() + "'."));
            }

            i++;
        }
    }
}
