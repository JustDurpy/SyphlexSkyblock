package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.customenchant.Category;
import net.syphlex.skyblock.manager.gui.impl.enchanter.EnchanterGui;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.upgrade.oregenerator.IslandOreGenerator;
import net.syphlex.skyblock.manager.island.block.OreGeneratorBlockData;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.manager.minion.MinionData;
import net.syphlex.skyblock.manager.mobcoin.MobCoinEntity;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.config.CEConfigData;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class SkyblockSettingsFile extends SimpleConfig {

    private final ArrayList<SpecialBlockData> specialBlockData = new ArrayList<>();
    private final ArrayList<IslandOreGenerator> islandOreGenerators = new ArrayList<>();
    private final ArrayList<MinionData> minionDataList = new ArrayList<>();
    private final ArrayList<MobCoinEntity> mobCoinEntities = new ArrayList<>();

    private CEConfigData ceConfigData;

    public SkyblockSettingsFile() {
        super("/settings.yml", false);
    }

    public void read() {

        config.options().copyDefaults(true);

        /*
        SPECIAL BLOCKS SECTION
         */
        specialBlocksConfig();

        /*
        ORE GENERATOR SECTION
         */
        generatorConfig();

        /*
        MINION SECTION
         */
        minionConfig();

        /*
        MOBCOINS SECTION
         */
        mobCoinConfig();

        /*
        CUSTOM ENCHANTS SECTION
         */
        customEnchantsConfig();

        /*
        save defaults
         */
        save();

        readSpecialBlocksConfig();
        readGeneratorConfig();
        readMinionConfig();
        readMobCoinConfig();
        readCustomEnchantsConfig();
    }

    public void write(){
        // todo write if needed
    }

    private void readSpecialBlocksConfig(){
        for (String specialBlocks : config.getStringList("special-blocks")) {
            SpecialBlockData blockData = Skyblock.get().getUpgradeHandler().getSpecialBlockDataFromString(specialBlocks);
            Skyblock.get().getUpgradeHandler().getSpecialBlocks().add(blockData);
        }
    }

    private void readGeneratorConfig(){
        for (String section : config.getConfigurationSection("generators").getKeys(false)) {

            int tier = config.getInt("generators." + section + ".tier");
            String name = config.getString("generators." + section + ".name");

            IslandOreGenerator generator = new IslandOreGenerator(tier, name);

            for (String blockSec : config.getConfigurationSection("generators." + section + ".blocks").getKeys(false)) {
                String materialName = config.getString("generators." + section + ".blocks." + blockSec + ".material");
                double chance = config.getDouble("generators." + section + ".blocks." + blockSec + ".chance");
                generator.getBlocks().add(new OreGeneratorBlockData(Material.getMaterial(materialName), chance));
            }

            Skyblock.get().getUpgradeHandler().getOreGenerators().add(generator);
        }
    }

    private void readMinionConfig(){
        for (String minionType : config.getConfigurationSection("minion").getKeys(false)) {

            Minion.Type type = Skyblock.get().getMinionHandler().getTypeFromName(minionType);
            int maxLvl = config.getInt("minion." + minionType + ".max-level");
            int radius = config.getInt("minion." + minionType + ".radius");

            MinionData data = new MinionData();
            data.setType(type);
            data.setMaxLvl(maxLvl);
            data.setRadius(radius);

            for (String s : config.getConfigurationSection("minion." + minionType + ".levels").getKeys(false)) {

                int level = Integer.parseInt(s);

                Pair<Integer, Object> o = new Pair<>(level, config.get("minion." + minionType + ".levels." + level));
                data.getObjects().add(o);
            }

            Skyblock.get().getMinionHandler().getMinionDataList().add(data);
        }
    }

    private void readMobCoinConfig(){
        for (String mobcoinMobs : config.getStringList("mobcoins.mobs")) {

            String[] split = mobcoinMobs.split(":");

            String mobName = split[0];
            double chance = Double.parseDouble(split[1]);
            EntityType type = EntityType.valueOf(mobName);

            Skyblock.get().getMobCoinHandler().getMobCoinEntities().add(new MobCoinEntity(type, chance));
        }
    }

    private void readCustomEnchantsConfig(){
        this.ceConfigData = new CEConfigData();
        this.ceConfigData.runeCost = config.getInt("enchant.rune-cost");

        for (String categoryType : config.getConfigurationSection("enchant.categories").getKeys(false)) {

            String name = config.getString("enchant.categories." + categoryType + ".name");
            String color = config.getString("enchant.categories." + categoryType + ".color");
            double chance = config.getDouble("enchant.categories." + categoryType + ".chance");

            Category category = new Category(name, color, chance);
            Skyblock.get().getEnchantHandler().getCategories().add(category);
        }

        {
            String name = config.getString("rune-item.name");
            List<String> lore = config.getStringList("rune-item.lore");
            Material material = PluginUtil.getMaterial(config.getString("rune-item.material"),
                    Material.BOOK);
            boolean glow = config.getBoolean("rune-item.glow");

            this.ceConfigData.runeItem = new ItemBuilder()
                    .setName(name)
                    .setLore(lore)
                    .setMaterial(material)
                    .setGlowing(glow)
                    .build();
        }

        {
            String name = config.getString("shard-item.name");
            List<String> lore = config.getStringList("shard-item.lore");
            Material material = PluginUtil.getMaterial(config.getString("shard-item.material"),
                    Material.PRISMARINE_SHARD);
            boolean glow = config.getBoolean("shard-item.glow");

            this.ceConfigData.shardItem = new ItemBuilder()
                    .setName(name)
                    .setLore(lore)
                    .setMaterial(material)
                    .setGlowing(glow)
                    .build();
        }

        {
            String name = config.getString("angel-dust-item.name");
            List<String> lore = config.getStringList("angel-dust-item.lore");
            Material material = PluginUtil.getMaterial(config.getString("angel-dust-item.material"),
                    Material.SUGAR);
            boolean glow = config.getBoolean("angel-dust-item.glow");

            this.ceConfigData.angelDust = new ItemBuilder()
                    .setName(name)
                    .setLore(lore)
                    .setMaterial(material)
                    .setGlowing(glow)
                    .build();

            this.ceConfigData.angelDustMax = config.getInt("angel-dust-item.max-rate");
            this.ceConfigData.angelDustMin = config.getInt("angel-dust-item.min-rate");
        }

        {
            String name = config.getString("whitescroll-item.name");
            List<String> lore = config.getStringList("whitescroll-item.lore");
            Material material = PluginUtil.getMaterial(config.getString("whitescroll-item.material"));
            boolean glow = config.getBoolean("whitescroll-item.glow");

            this.ceConfigData.whiteScrollItem = new ItemBuilder()
                    .setName(name)
                    .setLore(lore)
                    .setMaterial(material)
                    .setGlowing(glow)
                    .build();
        }

        {
            String title = config.getString("gui.enchanter.title");
            int size = config.getInt("gui.enchanter.size");

            Skyblock.get().getGuiHandler().setEnchanterGui(new EnchanterGui(title, size));

            for (String key : config.getConfigurationSection("gui.enchanter.items").getKeys(false)) {

                String name = config.getString("gui.enchanter.items." + key + ".name");
                List<String> lore = config.getStringList("gui.enchanter.items." + key + ".lore");
                String command = config.getString("gui.enchanter.items." + key + ".command");
                Material material = PluginUtil.getMaterial(config.getString("gui.enchanter.items." + key + ".material"));
                boolean glow = config.getBoolean("gui.enchanter.items." + key + ".glow");
                int cost = config.getInt("gui.enchanter.items." + key + ".cost");
                int slot = config.getInt("gui.enchanter.items." + key + ".slot");

                GuiItem guiItem = new GuiItem(new ItemBuilder()
                        .setName(name)
                        .setLore(lore)
                        .setMaterial(material)
                        .setGlowing(glow).build(),
                        command, cost, slot);

                Skyblock.get().getGuiHandler().getEnchanterGui().getGuiItems().add(guiItem);
            }
            Skyblock.get().getGuiHandler().getEnchanterGui().setupItems();
        }
    }

    private void specialBlocksConfig(){
        config.addDefault("special-blocks", Arrays.asList(
                "DIAMOND_BLOCK:10000:Diamond Blocks",
                "IRON_BLOCK:5000:Iron Blocks"));
    }

    private void generatorConfig(){
        config.addDefault("generators.example.tier", 1);
        config.addDefault("generators.example.name", "&6Example Generator");
        config.addDefault("generators.example.blocks.1.material", "COBBLESTONE");
        config.addDefault("generators.example.blocks.1.chance", 80);
        config.addDefault("generators.example.blocks.2.material", "COAL_ORE");
        config.addDefault("generators.example.blocks.2.chance", 20);
    }

    private void minionConfig(){
        config.addDefault("minion.miner.radius", 1);
        config.addDefault("minion.miner.max-level", 3);
        config.addDefault("minion.miner.levels.1", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.miner.levels.2", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.miner.levels.3", "COBBLESTONE:80%,GOLD_ORE:20%");
        config.addDefault("minion.slayer.radius", 1);
        config.addDefault("minion.slayer.max-level", 3);
        config.addDefault("minion.slayer.levels.1", 1);
        config.addDefault("minion.slayer.levels.2", 3);
        config.addDefault("minion.slayer.levels.3", 5);
        config.addDefault("minion.lumberjack.radius", 1);
        config.addDefault("minion.lumberjack.max-level", 3);
        config.addDefault("minion.lumberjack.levels.1", "radius:2,amount:1");
        config.addDefault("minion.lumberjack.levels.2", "radius:3,amount:3");
        config.addDefault("minion.lumberjack.levels.3", "radius:5,amount:5");
    }

    private void mobCoinConfig(){
        config.addDefault("mobcoins.mobs", Arrays.asList("ZOMBIE:4.5", "SKELETON:7.5"));
        // todo make the shop here.
    }

    private void customEnchantsConfig(){
        config.addDefault("gui.enchanter.title", "&8Enchanter");
        config.addDefault("gui.enchanter.size", 9);
        config.addDefault("gui.enchanter.items.rune.name", "&dEnchanted Rune");
        config.addDefault("gui.enchanter.items.rune.lore",
                Arrays.asList("&7Click to purchase an enchanted rune."));
        config.addDefault("gui.enchanter.items.rune.material", "BOOK");
        config.addDefault("gui.enchanter.items.rune.glow", true);
        config.addDefault("gui.enchanter.items.rune.command", "rune");
        config.addDefault("gui.enchanter.items.rune.slot", 4);
        config.addDefault("gui.enchanter.items.rune.cost", 300);
        config.addDefault("enchant.categories.Common.name", "Common");
        config.addDefault("enchant.categories.Common.color", "&a");
        config.addDefault("enchant.categories.Common.chance", 70);
        config.addDefault("enchant.categories.Rare.name", "Rare");
        config.addDefault("enchant.categories.Rare.color", "&b");
        config.addDefault("enchant.categories.Rare.chance", 25);
        config.addDefault("enchant.categories.Legendary.name", "Legendary");
        config.addDefault("enchant.categories.Legendary.color", "&6");
        config.addDefault("enchant.categories.Legendary.chance", 5);
        config.addDefault("shard-item.name", "&r%color%%enchantment% %level%");
        config.addDefault("shard-item.lore",
                Arrays.asList("&7click shard on an item to enchant the item.",
                        "&7Rarity: %rarity%",
                        "&7",
                        "&aSuccess Rate: %success_rate%%",
                        "&cDestroy Rate: %destroy_rate%%"));
        config.addDefault("shard-item.material", "PRISMARINE_SHARD");
        config.addDefault("shard-item.glow", true);
        config.addDefault("rune-item.name", "&3Enchanted Rune");
        config.addDefault("rune-item.lore",
                Arrays.asList("&7Right click to receive a random",
                        "&7rarity of an enchanted shard."));
        config.addDefault("rune-item.material", "BOOK");
        config.addDefault("rune-item.glow", true);
        config.addDefault("angel-dust-item.name", "&fAngel Dust &a+%success%%");
        config.addDefault("angel-dust-item.lore",
                Arrays.asList("&7Click on an enchantment shard",
                        "&7to increase the success rate.",
                        "&a+%success%%"));
        config.addDefault("angel-dust-item.material", "SUGAR");
        config.addDefault("angel-dust-item.glow", true);
        config.addDefault("angel-dust-item.max-rate", 20);
        config.addDefault("angel-dust-item.min-rate", 1);
        config.addDefault("whitescroll-item.name", "&fWhitescroll");
        config.addDefault("whitescroll-item.lore",
                Arrays.asList("&7Click on an item, to protect it"));
        config.addDefault("whitescroll-item.material", "EMPTY_MAP");
        config.addDefault("whitescroll-item.glow", true);
    }
}
