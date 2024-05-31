package net.syphlex.skyblock.manager.gui.impl.island.settings;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.impl.island.settings.type.IslandBorderColorGui;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandSettingsGui extends SimpleGui {

    public IslandSettingsGui() {
        super("Island Settings", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        this.inventory.setItem(10, new ItemBuilder()
                .setMaterial(Material.CLOCK)
                .setName("&dTime Lock")
                .build());

        this.inventory.setItem(12, new ItemBuilder()
                .setMaterial(Material.SUNFLOWER)
                .setName("&dWeather Lock")
                .build());

        this.inventory.setItem(14, new ItemBuilder()
                .setMaterial(Material.BARRIER)
                .setName("&dIsland Border Color")
                .build());

        this.inventory.setItem(16, new ItemBuilder()
                .setMaterial(Material.REPEATER)
                .setName("&dExtra Settings")
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile p = e.getProfile();

        switch (e.getSlot()) {
            case 11:
                break;
            case 15:
                Skyblock.get().getGuiHandler().openGui(p, new IslandBorderColorGui());
                break;
        }
    }
}
