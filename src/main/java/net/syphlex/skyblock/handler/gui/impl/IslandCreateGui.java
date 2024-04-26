package net.syphlex.skyblock.handler.gui.impl;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.ClickEvent;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.simple.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandCreateGui extends SimpleGui {
    public IslandCreateGui() {
        super("Create an Island", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        setItem(new ItemBuilder()
                .setMaterial(Material.LIME_CONCRETE)
                .setName("&aCreate Island")
                .setLore(Arrays.asList("&fClick to create an island!"))
                .build(), 13);
    }

    @Override
    public void onClickEvent(ClickEvent e) {
        switch (e.getSlot()) {
            case 13:
                Skyblock.get().getIslandHandler().generateIsland(e.getProfile());
                closeInventory(e.getProfile().getPlayer());
                break;
        }
    }
}
