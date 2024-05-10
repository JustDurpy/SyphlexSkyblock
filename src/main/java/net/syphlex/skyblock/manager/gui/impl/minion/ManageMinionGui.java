package net.syphlex.skyblock.manager.gui.impl.minion;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;

public class ManageMinionGui extends SimpleGui
{

    private final Minion minion;

    public ManageMinionGui(Minion minion) {
        super("Manage Minion", 9);
        this.minion = minion;
        this.inventory.setItem(0, new ItemBuilder()
                .setMaterial(Material.RED_DYE)
                .setGlowing(true)
                .setAmount(1)
                .setName("&cPick Up Minion")
                .build());
        this.inventory.setItem(4, new ItemBuilder()
                .setMaterial(Material.EXPERIENCE_BOTTLE)
                .setGlowing(true)
                .setAmount(minion.getLevel())
                .setName("&f&l* &e&lUpgrade Minion &f&l*")
                .setLore(Arrays.asList(
                        "",
                        " &f&l* &eLevel: &6" + minion.level,
                        " &f&l* &eMax Level: &6" + minion.getData().getMaxLvl(),
                        "",
                        (minion.canUpgrade() ? "&aClick to upgrade."
                                : "&cThis minion is already at max level.")))
                .build());
        this.inventory.setItem(8, new ItemBuilder()
                .setMaterial(Material.ENDER_CHEST)
                .setGlowing(true)
                .setAmount(1)
                .setName("&eAttach A Chest")
                .build());
    }

    @Override
    public void onClickEvent(ClickEvent click) {

        final Profile profile = click.getProfile();

        switch (click.getSlot()) {
            case 0:
                this.minion.pickUpMinion(profile.getPlayer());
                this.closeInventory(profile.getPlayer());
                break;
            case 4:
                Skyblock.get().getMinionHandler().upgrade(profile.getPlayer(), this.minion);
                this.inventory.setItem(4, new ItemBuilder()
                        .setMaterial(Material.EXPERIENCE_BOTTLE)
                        .setGlowing(true)
                        .setAmount(minion.getLevel())
                        .setName("&f&l* &e&lUpgrade Minion &f&l*")
                        .setLore(Arrays.asList(
                                "",
                                " &f&l* &eLevel: &6" + minion.level,
                                " &f&l* &eMax Level: &6" + minion.getData().getMaxLvl(),
                                "",
                                (minion.canUpgrade() ? "&aClick to upgrade."
                                        : "&cThis minion is already at max level.")))
                        .build());
                break;
            case 8:
                if (profile.getAttachingChest() != null) {
                    profile.getPlayer().sendMessage(StringUtil.CC("&cStill attempting to attach a chest"));
                    return;
                }
                profile.setAttachingChest(this.minion);
                profile.getPlayer().sendMessage(StringUtil.CC("&aSelect a chest..."));
                this.closeInventory(profile.getPlayer());
                Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.get(), () -> {
                    profile.setAttachingChest(null);
                }, 300L);
                break;
        }
    }
}
