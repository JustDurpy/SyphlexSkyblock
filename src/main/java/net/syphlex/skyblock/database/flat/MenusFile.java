package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.simple.SimpleConfig;

import java.util.ArrayList;

@Getter
public class MenusFile extends SimpleConfig {

    public MenusFile() {
        super("/menus.yml", false);
    }

    public void read(){

        config.options().copyDefaults(true);

        config.addDefault("menus.island-panel.items.1.name", "{#A482F8} Test");
        config.addDefault("menus.island-panel.items.1.material", "");

        loadPanelItems();
    }

    private void loadPanelItems(){

    }
}
