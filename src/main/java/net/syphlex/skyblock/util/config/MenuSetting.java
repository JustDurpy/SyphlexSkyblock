package net.syphlex.skyblock.util.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuSetting {
    private final boolean enabled, fillMenu;
    private final ItemStack fillItem;
    private final String menuTitle;
    private final int menuSize;
    private final List<GuiItem> items;
}
