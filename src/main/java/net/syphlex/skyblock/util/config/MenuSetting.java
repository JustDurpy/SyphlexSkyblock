package net.syphlex.skyblock.util.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class MenuSetting {
    private final String menuTitle;
    private final int menuSize;
    private final ArrayList<GuiItem> items = new ArrayList<>();
}
