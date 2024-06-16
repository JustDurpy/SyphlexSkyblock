package net.syphlex.skyblock.util.config;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.data.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Getter
public enum ConfigMenu {
    ISLAND_INFORMATION_MENU("island-information", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Information",
            45,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.IRON_BLOCK)
                            .setName("&6Visit Island")
                            .setLore(Arrays.asList(
                                    "Click to teleport to this island.")))
                            .setPerm("none")
                            .setCmd("island visit %island%")
                            .setSlot(11)
            )
    )),
    GENERATOR_MENU("generator", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Ore Generators",
            27,
            Arrays.asList(
                    new GuiItem(Arrays.asList(
                            new Pair<>("can-upgrade", new ItemBuilder()
                                    .setName("Example Generator")
                                    .setMaterial(Material.COBBLESTONE)
                                    .setLore(Arrays.asList(" ",
                                            "&6Information:",
                                            " &6➥ &eTier: &fI",
                                            " &6➥ &eCost: &f$%cost%",
                                            " ",
                                            "&6Blocks:",
                                            " &6➥ &eCobblestone &7(80%)",
                                            " &6➥ &eCoal Ore &7(15%)",
                                            " &6➥ &eIron Ore &7(5%)",
                                            " ",
                                            "&aClick to upgrade to this generator.",
                                            " "))),
                            new Pair<>("cannot-upgrade", new ItemBuilder()
                                    .setName("Example Generator")
                                    .setMaterial(Material.COBBLESTONE)
                                    .setLore(Arrays.asList(" ",
                                            "&6Information:",
                                            " &6➥ &eTier: &fI",
                                            " &6➥ &eCost: &f$%cost%",
                                            " ",
                                            "&6Blocks:",
                                            " &6➥ &eCobblestone &7(80%)",
                                            " &6➥ &eCoal Ore &7(15%)",
                                            " &6➥ &eIron Ore &7(5%)",
                                            " ",
                                            "&cYou cannot afford to upgrade to this generator.",
                                            " "))),
                            new Pair<>("already-upgraded", new ItemBuilder()
                                    .setName("Example Generator")
                                    .setMaterial(Material.COBBLESTONE)
                                    .setLore(Arrays.asList(" ",
                                            "&6Information:",
                                            " &6➥ &eTier: &fI",
                                            " &6➥ &eCost: &f$%cost%",
                                            " ",
                                            "&6Blocks:",
                                            " &6➥ &eCobblestone &7(80%)",
                                            " &6➥ &eCoal Ore &7(15%)",
                                            " &6➥ &eIron Ore &7(5%)",
                                            "",
                                            "&cYou already own this generator.",
                                            " ")))))
                            .setStringId("example")
                            .setSlot(11))
    )),
    TOP_ISLANDS_MENU("top-islands", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Top Islands",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setName("#&6%position%. &e%leader_name%")
                            .setLore(Arrays.asList(
                                    "&6Information:",
                                    " &6➥ &eWorth: &f$%worth%",
                                    " &6➥ &eMembers: &f%team_size%/%max_team_size%",
                                    "",
                                    "&7&o(( Click to view island information ))",
                                    "")))
                            .setId(0)
                            .setSlot(0),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.END_PORTAL_FRAME)
                            .setName("&6Island Panel"))
                            .setId(99)
                            .setSlot(22))
    )),
    CREATE_ISLAND_MENU("create-island", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Create an Island",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setName("&aDefault Island")
                            .setMaterial(Material.GRASS_BLOCK)
                            .setLore(Arrays.asList(
                                    "&7Click to create your island",
                                    "&7using the default schematic.")))
                            .setStringId("default")
                            .setPerm("none")
                            .setSlot(13))
    )),
    DELETE_ISLAND_MENU("delete-island", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Delete Your Island?",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.RED_CONCRETE)
                            .setName("&cDelete Island"))
                            .setId(0)
                            .setSlot(13))
    )),
    ISLAND_PANEL_MENU("island-panel", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Panel",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRASS_BLOCK)
                            .setName("&6Island Home"))
                            .setId(0)
                            .setSlot(10),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.BOOK)
                            .setName("&6Island Information"))
                            .setId(1)
                            .setSlot(11),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.DIAMOND)
                            .setName("&6Top Islands"))
                            .setId(2)
                            .setSlot(12),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.BEACON)
                            .setName("&6Island Upgrades"))
                            .setId(3)
                            .setSlot(14),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.PAPER)
                            .setName("&6Island Permissions"))
                            .setId(4)
                            .setSlot(15),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.REPEATER)
                            .setName("&6IIsland Settings"))
                            .setId(5)
                            .setSlot(16))
    )),
    ISLAND_SETTINGS_MENU("island-settings", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Settings",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.CLOCK)
                            .setName("&6Time Lock"))
                            .setId(0)
                            .setSlot(10),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.SUNFLOWER)
                            .setName("&6Weather Lock"))
                            .setId(1)
                            .setSlot(12),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.BARRIER)
                            .setName("&6Island Border Color"))
                            .setId(2)
                            .setSlot(14),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.REPEATER)
                            .setName("&6Extra Settings"))
                            .setId(3)
                            .setSlot(16),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.END_PORTAL_FRAME)
                            .setName("&6Island Panel"))
                            .setId(99)
                            .setSlot(22))
    )),
    ISLAND_BORDER_COLOR_MENU("border-color-setting", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Border Color",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                            .setName("&9Blue Island Border"))
                            .setId(0)
                            .setSlot(11),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                            .setName("&aGreen Island Border"))
                            .setId(1)
                            .setSlot(13),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setName("&cRed Island Border"))
                            .setId(2)
                            .setSlot(15),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.END_PORTAL_FRAME)
                            .setName("&6Island Panel"))
                            .setId(99)
                            .setSlot(22))
    )),
    TIME_LOCK_MENU("time-lock-setting", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Time Lock",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.RED_DYE)
                            .setName("&cNone"))
                            .setId(0)
                            .setSlot(11),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&fSun &eRise"))
                            .setId(1)
                            .setSlot(12),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&eDay"))
                            .setId(2)
                            .setSlot(13),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&9Sun &6Set"))
                            .setId(3)
                            .setSlot(14),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&7Night"))
                            .setId(4)
                            .setSlot(15),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.END_PORTAL_FRAME)
                            .setName("&6Island Panel"))
                            .setId(99)
                            .setSlot(22))
    )),
    WEATHER_LOCK_MENU("weather-lock-setting", new MenuSetting(
            true,
            true,
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
            "Island Weather Lock",
            27,
            Arrays.asList(
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.RED_DYE)
                            .setName("&cNone"))
                            .setId(0)
                            .setSlot(11),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&eSunny"))
                            .setId(1)
                            .setSlot(13),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.GRAY_DYE)
                            .setName("&bRainy"))
                            .setId(2)
                            .setSlot(15),
                    new GuiItem(new ItemBuilder()
                            .setMaterial(Material.END_PORTAL_FRAME)
                            .setName("&6Island Panel"))
                            .setId(99)
                            .setSlot(22))
    ));

    private final String fileName;
    @Setter
    private MenuSetting menuSetting;

    ConfigMenu(String fileName, MenuSetting menuSetting) {
        this.fileName = fileName;
        this.menuSetting = menuSetting;
    }
}
