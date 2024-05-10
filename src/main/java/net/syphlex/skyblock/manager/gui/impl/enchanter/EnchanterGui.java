package net.syphlex.skyblock.manager.gui.impl.enchanter;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.simple.SimpleGui;

public class EnchanterGui extends SimpleGui
{
    public EnchanterGui(String title, int size) {
        super(title, size);
    }

    public void setupItems(){
        if (!getGuiItems().isEmpty()) {
            for (GuiItem item : getGuiItems()) {
                this.inventory.setItem(item.slot(), item.item());
            }
        }
    }

    @Override
    public void onClickEvent(ClickEvent click) {

        for (GuiItem item : getGuiItems()) {

            if (click.getSlot() == item.slot()) {
                if (!item.hasCmd()) {
                    return;
                } else if (item.command().equalsIgnoreCase("rune")) {
                    Skyblock.get().getEnchantHandler().purchaseRune(click.getProfile().getPlayer());
                    return;
                } else {
                    click.getProfile().getPlayer().performCommand(item.command());
                }
                break;
            }

        }

    }
}
