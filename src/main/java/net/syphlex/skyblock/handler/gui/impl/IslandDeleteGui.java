package net.syphlex.skyblock.handler.gui.impl;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.ClickEvent;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.SimpleGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class IslandDeleteGui extends SimpleGui {
    public IslandDeleteGui() {
        super("Delete Your Island", 27);

        fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        setItem(new ItemBuilder()
                .setMaterial(Material.RED_CONCRETE)
                .setName("&cDelete Island")
                .setLore(Arrays.asList("&fClick to delete your island!"))
                .build(), 13);
    }

    @Override
    public void onClickEvent(ClickEvent e) {
        switch (e.getSlot()) {
            case 13:
                Skyblock.get().getIslandHandler().degenerateIsland(e.getProfile());
                closeInventory(e.getProfile().getPlayer());
                break;
        }
    }
}
