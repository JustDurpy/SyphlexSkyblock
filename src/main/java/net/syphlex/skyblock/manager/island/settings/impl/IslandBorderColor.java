package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandBorderColorGui;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.Permissions;
import org.bukkit.Color;
import org.bukkit.Material;

@Getter
public enum IslandBorderColor {
    BLUE("&bBlue Island Border",
            Permissions.ISLAND_SETTINGS_BLUE_BORDER.get(),
            new GuiItem(new ItemBuilder()
                    .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .setName("&bBlue Island Border")
                    .build(), 11),
            Color.BLUE),
    GREEN("&aGreen Island Border",
            Permissions.ISLAND_SETTINGS_GREEN_BORDER.get(),
            new GuiItem(new ItemBuilder()
                    .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                    .setName("&aGreen Island Border")
                    .build(), 13),
            Color.GREEN),
    RED("&cRed Island Border",
            Permissions.ISLAND_SETTINGS_RED_BORDER.get(),
            new GuiItem(new ItemBuilder()
                    .setMaterial(Material.RED_STAINED_GLASS_PANE)
                    .setName("&cRed Island Border")
                    .build(), 15),
            Color.RED);

    private final String identifier, permission;
    private final GuiItem guiItem;
    private final Color color;
    @Setter private boolean toggled;

    IslandBorderColor(String identifier, String permission, GuiItem guiItem, Color color){
        this.identifier = identifier;
        this.permission = permission;
        this.guiItem = guiItem;
        this.color = color;
    }

    public static IslandBorderColor of(String identifier){
        if (identifier == null) return BLUE;
        return valueOf(identifier);
    }
}
