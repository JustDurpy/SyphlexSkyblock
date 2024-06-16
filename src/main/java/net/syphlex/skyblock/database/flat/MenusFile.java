package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.MenuSetting;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@SuppressWarnings("all")
public class MenusFile extends SimpleConfig {

    /*
    todo fix "setDsplayName" and all deperecated usages
     */

    public MenusFile() {
        super("/menus/", true);
    }

    public void write(){
        for (ConfigMenu configMenu : ConfigMenu.values()) {

            File file = new File(getFile().getPath()
                    + "/" + configMenu.getFileName() + ".yml");
            try {

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                final MenuSetting setting = configMenu.getMenuSetting();

                config.set("menu.enabled", setting.isEnabled());
                config.set("menu.fill-menu", setting.isFillMenu());
                config.set("menu.fill-item-material", setting.getFillItem().getType().name());
                config.set("menu.title", setting.getMenuTitle());
                config.set("menu.size", setting.getMenuSize());

                int i = 1;
                for (GuiItem guiItem : setting.getItems()) {

                    final ItemStack item = guiItem.getItem();

                    config.set("menu.items." + i + ".name", item.getItemMeta().getDisplayName());
                    config.set("menu.items." + i + ".material", item.getType().name());
                    config.set("menu.items." + i + ".amount", item.getAmount());
                    config.set("menu.items." + i + ".lore", item.getLore());
                    config.set("menu.items." + i + ".slot", guiItem.getSlot());
                    config.set("menu.items." + i + ".command", guiItem.getCommand());
                    config.set("menu.items." + i + ".function-id", guiItem.getId());

                    i++;
                }

                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeDefault(ConfigMenu configMenu, boolean force) {

        File file = new File(getFile().getPath()
                + "/" + configMenu.getFileName() + ".yml");
        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.options().setHeader(Arrays.asList("The function-id is the id for the action performed by the item, '99' ",
                    "will always be regarding to the island panel. If you wish to add an item without an id just leave it as '-1'."));

            final MenuSetting setting = configMenu.getMenuSetting();

            if (force) {

                config.options().copyDefaults(true);
                config.addDefault("menu.enabled", setting.isEnabled());
                config.addDefault("menu.fill-menu", setting.isFillMenu());
                config.addDefault("menu.fill-item-material", setting.getFillItem().getType().name());
                config.addDefault("menu.title", setting.getMenuTitle());
                config.addDefault("menu.size", setting.getMenuSize());

                int i = 1;
                for (GuiItem guiItem : setting.getItems()) {

                    if (configMenu == ConfigMenu.GENERATOR_MENU) {

                        for (Pair<String, ItemBuilder> pair : guiItem.getItems()) {

                            ItemStack stack = pair.getY().build();

                            config.addDefault("menu.items." + i + "."
                                    + pair.getX() + ".name", stack.getItemMeta().getDisplayName());
                            config.addDefault("menu.items." + i + "."
                                    + pair.getX() + ".material", stack.getType().name());
                            config.addDefault("menu.items." + i + "."
                                    + pair.getX() + ".amount", stack.getAmount());
                            config.addDefault("menu.items." + i + "."
                                    + pair.getX() + ".lore", stack.getItemMeta().getLore());
                        }

                        config.addDefault("menu.items." + i + ".slot", guiItem.getSlot());
                        config.addDefault("menu.items." + i + ".command", guiItem.getCommand());
                        config.addDefault("menu.items." + i + ".permission", guiItem.getPermission());
                        config.addDefault("menu.items." + i + ".generator", guiItem.getStringId());

                    } else {

                        final ItemStack item = guiItem.getItem();

                        config.addDefault("menu.items." + i + ".name", item.getItemMeta().getDisplayName());
                        config.addDefault("menu.items." + i + ".material", item.getType().name());
                        config.addDefault("menu.items." + i + ".amount", item.getAmount());
                        config.addDefault("menu.items." + i + ".lore", item.getLore());
                        config.addDefault("menu.items." + i + ".slot", guiItem.getSlot());
                        config.addDefault("menu.items." + i + ".command", guiItem.getCommand());
                        config.addDefault("menu.items." + i + ".permission", guiItem.getPermission());
                        config.addDefault("menu.items." + i + ".function-id", guiItem.getId());
                    }

                    i++;
                }

                config.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {

        for (ConfigMenu configMenu : ConfigMenu.values()) {

            File file = new File(getFile().getPath()
                    + "/" + configMenu.getFileName() + ".yml");
            try {

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                config.options().setHeader(Arrays.asList("The function-id is the id for the action performed by the item, '99' ",
                        "will always be regarding to the island panel. If you wish to add an item without an id just leave it as '-1'."));

                final MenuSetting setting = configMenu.getMenuSetting();

                if (config.get("menu") == null) {

                    config.options().copyDefaults(true);
                    config.addDefault("menu.enabled", setting.isEnabled());
                    config.addDefault("menu.fill-menu", setting.isFillMenu());
                    config.addDefault("menu.fill-item-material", setting.getFillItem().getType().name());
                    config.addDefault("menu.title", setting.getMenuTitle());
                    config.addDefault("menu.size", setting.getMenuSize());

                    int i = 1;
                    for (GuiItem guiItem : setting.getItems()) {

                        if (configMenu == ConfigMenu.GENERATOR_MENU) {

                            for (Pair<String, ItemBuilder> pair : guiItem.getItems()) {

                                ItemStack stack = pair.getY().build();

                                config.addDefault("menu.items." + i + "."
                                        + pair.getX() + ".name", stack.getItemMeta().getDisplayName());
                                config.addDefault("menu.items." + i + "."
                                        + pair.getX() + ".material", stack.getType().name());
                                config.addDefault("menu.items." + i + "."
                                        + pair.getX() + ".amount", stack.getAmount());
                                config.addDefault("menu.items." + i + "."
                                        + pair.getX() + ".lore", stack.getItemMeta().getLore());
                            }

                            config.addDefault("menu.items." + i + ".slot", guiItem.getSlot());
                            config.addDefault("menu.items." + i + ".command", guiItem.getCommand());
                            config.addDefault("menu.items." + i + ".permission", guiItem.getPermission());
                            config.addDefault("menu.items." + i + ".generator", guiItem.getStringId());

                        } else if (configMenu == ConfigMenu.CREATE_ISLAND_MENU) {

                            final ItemStack item = guiItem.getItem();

                            config.addDefault("menu.items." + i + ".name", item.getItemMeta().getDisplayName());
                            config.addDefault("menu.items." + i + ".material", item.getType().name());
                            config.addDefault("menu.items." + i + ".amount", item.getAmount());
                            config.addDefault("menu.items." + i + ".lore", item.getLore());
                            config.addDefault("menu.items." + i + ".slot", guiItem.getSlot());
                            config.addDefault("menu.items." + i + ".permission", guiItem.getPermission());
                            config.addDefault("menu.items." + i + ".schematic-name", guiItem.getStringId());

                        } else {

                            final ItemStack item = guiItem.getItem();

                            config.addDefault("menu.items." + i + ".name", item.getItemMeta().getDisplayName());
                            config.addDefault("menu.items." + i + ".material", item.getType().name());
                            config.addDefault("menu.items." + i + ".amount", item.getAmount());
                            config.addDefault("menu.items." + i + ".lore", item.getLore());
                            config.addDefault("menu.items." + i + ".slot", guiItem.getSlot());
                            config.addDefault("menu.items." + i + ".command", guiItem.getCommand());
                            config.addDefault("menu.items." + i + ".permission", guiItem.getPermission());
                            config.addDefault("menu.items." + i + ".function-id", guiItem.getId());
                        }

                        i++;
                    }

                    config.save(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (ConfigMenu configMenu : ConfigMenu.values()) {

            try {

                File file = new File(getFile().getPath()
                        + "/" + configMenu.getFileName() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                final boolean enabled = config.getBoolean("menu.enabled");
                final boolean fillMenu = config.getBoolean("menu.fill-menu");
                final Material fillItem = PluginUtil.getMaterial(config.getString("menu.fill-item-material"));
                final String title = config.getString("menu.title");
                final int size = config.getInt("menu.size");

                final MenuSetting menuSetting = new MenuSetting(enabled, fillMenu, new ItemStack(fillItem), title, size, new ArrayList<>());

                for (String section : config.getConfigurationSection("menu.items").getKeys(false)) {

                    if (configMenu == ConfigMenu.GENERATOR_MENU) {

                        final int itemSlot = config.getInt("menu.items." + section + ".slot");
                        final String itemCommand = config.getString("menu.items." + section + ".command");
                        final String generator = config.getString("menu.items." + section + ".generator");

                        ArrayList<Pair<String, ItemBuilder>> list = new ArrayList<>();
                        for (String type : config.getConfigurationSection("menu.items." + section).getKeys(false)) {

                            final String itemName = config.getString("menu.items." + section + "." + type + ".name");
                            final Material itemMaterial = PluginUtil.getMaterial(config.getString("menu.items." + section + "." + type + ".material"));
                            final int itemAmount = config.getInt("menu.items." + section + "." + type + ".amount");
                            final List<String> itemLore = config.getStringList("menu.items." + section + "." + type + ".lore");


                            list.add(new Pair<>(type, new ItemBuilder()
                                    .setName(itemName)
                                    .setMaterial(itemMaterial)
                                    .setAmount(itemAmount)
                                    .setLore(itemLore)));
                        }

                        GuiItem guiItem = new GuiItem(list)
                                .setCmd(itemCommand)
                                .setStringId(generator)
                                .setSlot(itemSlot);

                        menuSetting.getItems().add(guiItem);

                        continue;
                    }

                    if (configMenu == ConfigMenu.CREATE_ISLAND_MENU) {

                        final String itemName = config.getString("menu.items." + section + ".name");
                        final Material itemMaterial = PluginUtil.getMaterial(config.getString("menu.items." + section + ".material"));
                        final int itemAmount = config.getInt("menu.items." + section + ".amount");
                        final List<String> itemLore = config.getStringList("menu.items." + section + ".lore");
                        final int itemSlot = config.getInt("menu.items." + section + ".slot");
                        final String itemPermission = config.getString("menu.items." + section + ".permission");
                        final String schemName = config.getString("menu.items." + section + ".schematic-name");

                        GuiItem guiItem = new GuiItem(new ItemBuilder()
                                .setName(itemName)
                                .setMaterial(itemMaterial)
                                .setAmount(itemAmount)
                                .setLore(itemLore))
                                .setStringId(schemName)
                                .setPerm(itemPermission)
                                .setSlot(itemSlot);

                        menuSetting.getItems().add(guiItem);
                        continue;
                    }

                    final String itemName = config.getString("menu.items." + section + ".name");
                    final Material itemMaterial = PluginUtil.getMaterial(config.getString("menu.items." + section + ".material"));
                    final int itemAmount = config.getInt("menu.items." + section + ".amount");
                    final List<String> itemLore = config.getStringList("menu.items." + section + ".lore");
                    final int itemSlot = config.getInt("menu.items." + section + ".slot");
                    final String itemCommand = config.getString("menu.items." + section + ".command");
                    final String itemPermission = config.getString("menu.items." + section + ".permission");
                    final int id = config.getInt("menu.items." + section + ".function-id");

                    GuiItem guiItem = new GuiItem(new ItemBuilder()
                            .setName(itemName)
                            .setMaterial(itemMaterial)
                            .setAmount(itemAmount)
                            .setLore(itemLore))
                            .setCmd(itemCommand)
                            .setPerm(itemPermission)
                            .setId(id)
                            .setSlot(itemSlot);

                    menuSetting.getItems().add(guiItem);
                }

                configMenu.setMenuSetting(menuSetting);
            } catch (Exception e) {
                Skyblock.log("ERROR: Error while parsing menu config! Config: "
                        + configMenu.getFileName() + ".yml!");
                writeDefault(configMenu, true);
            }
        }
    }
}
