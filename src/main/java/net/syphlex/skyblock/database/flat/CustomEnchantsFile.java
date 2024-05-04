package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.customenchant.*;
import net.syphlex.skyblock.util.simple.SimpleConfig;

import java.util.*;

public class CustomEnchantsFile extends SimpleConfig {


    public CustomEnchantsFile() {
        super("customenchants.yml", false);
    }

    @SuppressWarnings("deprecation")
    public void read() {

        config.options().copyHeader(true);
        config.options().header(Arrays.asList(
                "example:",
                "enchantments:",
                "  poisonivy:",
                "    display-name: 'Poison Ivy'",
                "    category: COMMON",
                "    triggers:",
                "    - ATTACK",
                "    applies-to:",
                "    - SWORD",
                "    max-lvl: 3",
                "    levels:",
                "      '1':",
                "         cooldown: 0",
                "         effects:",
                "         - POTION:%victim%:POISON:3:1",
                "         chance: 3",
                "      '2':",
                "         cooldown: 10",
                "         effects:",
                "         - POTION:%victim%:POISON:5:1",
                "         chance: 6",
                "      '3':",
                "         cooldown: 20",
                "         effects:",
                "         - POTION:%victim%:POISON:7:1",
                "         chance: 9",
                "-----------------------------------------------",
                "Triggers:",
                " - ATTACK : %player% attacking an entity %victim%",
                " - DEFENSE : any damage the %player% is receiving",
                " - SHOOT : %player% shooting a projctile at an entity %victim%",
                " - INTERACT_ENTITY : %player% interacting with an entity %victim%",
                " - RIGHT_CLICK : %player% right clicking block & air",
                " - LEFT_CLICK : %player% left clicking block & air",
                " - SNEAK : %player% is sneaking",
                " - MINING : %player% is breaking a block",
                " - BROKEN_BLOCK : %player% post breaking the block",
                " - STATIC : active while %player% has item in use",
                "-----------------------------------------------",
                "Appliances:",
                " - SWORD",
                " - AXE",
                " - PICKAXE",
                " - SHOVEL",
                " - ARMOR",
                " - HELMET",
                " - BOOTS",
                " - BOW",
                " - TOOLS",
                "-----------------------------------------------",
                "Effects:",
                " - POTION : 'POTION:%player%:%duration%:%amplifier%' - apply a potion effect",
                " - LIGHTNING : 'LIGHTNING:%player%' - strike lightning",
                " - INVINCIBLE : 'INVINCIBLE:%player%:%duration%' - make an entity invincible",
                " - FIRE : 'FIRE:%player%:%duration%' - set an entity on fire",
                " - INCREASE_DAMAGE : 'INCREASE_DAMAGE:%player%:%damage-multiplier%' - multiply damage (2 is double)",
                " - REDUCE_DAMAGE : 'REDUCE_DAMAGE:%player%:%reduction%' - reduce damage (not multiplied)",
                " - SET_DAMAGE : 'SET_DAMAGE:%victim%:%damage%' - set damage on the event",
                " - STEAL_HEALTH : 'STEAL_HEALTH:%victim%:%health%' - steal victims health",
                " - INSTANT_BREAK : 'INSTANT_BREAK:*' or 'INSTANT_BREAK:DIRT:DIAMOND_ORE' - instant break block players mining",
                " - PUSH : 'PUSH:%victim%:%multiplier%' - push an entity away from player",
                " - PULL : 'PULL:%victim%:%multiplier%' - pull an entity towards player",
                " - LAUNCH : 'LAUNCH:%victim%:%multiplier%' - launch an entity in the air",
                " - MOB_DROP : 'MOB_DROP:%multiplier%' - multiply mob drops",
                " - EXP : 'EXP:%amount%' - give extra EXP",
                " - FEED : 'FEED:%amount%' - give extra food to the player",
                " - HEAL : 'HEAL:%amount%' - give extra health to the player",
                " - HEALTH_BOOST : 'HEALTH_BOOST:%amount%' - give extra hearts to player",
                " - BLACKHOLE : 'BLACKHOLE:%victim%:%radius%:%damage%:%duration%' - create a blackhole at %player% or %victim%",
                " - DURABILITY : 'DURABILITY:%amount%' - add durability to item with enchant",
                " - TRAY : 'TRAY:%radius%:%blocks%' - mine out blocks in a certain radius",
                "").toString().replace(",", "\n"));
        config.options().copyHeader(true);
        config.options().copyDefaults(true);
        if (config.getConfigurationSection("enchantments") == null) {
            config.addDefault("enchantments.poisonivy.display-name", "Poison Ivy");
            config.addDefault("enchantments.poisonivy.category", "COMMON");
            config.addDefault("enchantments.poisonivy.triggers",
                    Collections.singletonList("ATTACK"));
            config.addDefault("enchantments.poisonivy.applies-to",
                    Collections.singletonList("SWORD"));
            config.addDefault("enchantments.poisonivy.max-lvl", 3);
            config.addDefault("enchantments.poisonivy.levels.1.cooldown", 10);
            config.addDefault("enchantments.poisonivy.levels.1.effects",
                    Collections.singletonList("POTION:%victim%:POISON:3:1"));
            config.addDefault("enchantments.poisonivy.levels.1.chance", 3);
            config.addDefault("enchantments.poisonivy.levels.2.cooldown", 10);
            config.addDefault("enchantments.poisonivy.levels.2.effects",
                    Collections.singletonList("POTION:%victim%:POISON:6:1"));
            config.addDefault("enchantments.poisonivy.levels.2.chance", 6);
            config.addDefault("enchantments.poisonivy.levels.3.cooldown", 10);
            config.addDefault("enchantments.poisonivy.levels.3.effects",
                    Collections.singletonList("POTION:%victim%:POISON:9:2"));
            config.addDefault("enchantments.poisonivy.levels.3.chance", 9);
        }
        config.options().copyDefaults(true);
        save();

        String path = "enchantments.";

        for (String enchant : config.getConfigurationSection("enchantments").getKeys(false)) {

            String enchantName = get(path + enchant + ".display-name").toString();
            Category category = Skyblock.get().getEnchantHandler().getCategoryFromName(
                    get(path + enchant + ".category").toString());
            if (category == null) {
                Skyblock.log("ERROR: CATEGORY NOT FOUND IN CONFIG FOR " + enchant);
                Skyblock.log("ERROR: SKIPPING OVER ENCHANTMENT!");
                continue;
            }
            Set<Trigger> triggers = new HashSet<>();
            Set<Appliance> appliances = new HashSet<>();

            for (String s : config.getStringList(path + enchant + ".triggers")) {
                Trigger trigger = null;
                try {
                    trigger = Trigger.valueOf(s.toUpperCase());
                } catch (IllegalArgumentException e) {
                    Skyblock.log("ERROR: INVALID TRIGGER FOR " + enchantName);
                    Skyblock.log("ERROR: SKIPPING OVER TRIGGER");
                    continue;
                }
                triggers.add(trigger);
            }

            for (String s : config.getStringList(path + enchant + ".applies-to")) {
                Appliance appliance = null;
                try {
                    appliance = Appliance.valueOf(s.toUpperCase());
                } catch (IllegalArgumentException e) {
                    Skyblock.log("ERROR: INVALID APPLIANCE FOR " + enchantName);
                    Skyblock.log("ERROR: SKIPPING OVER APPLIANCE");
                    continue;
                }
                appliances.add(appliance);
            }

            if (triggers.isEmpty()) {
                Skyblock.log("ERROR: NO TRIGGERS FOUND FOR " + enchantName);
                Skyblock.log("ERROR: SKIPPING OVER ENCHANTMENT");
                continue;
            }

            if (appliances.isEmpty()) {
                Skyblock.log("ERROR: NO APPLIANCES FOUND FOR " + enchantName);
                Skyblock.log("ERROR: SKIPPING OVER ENCHANTMENT");
                continue;
            }

            int maxLvl = config.getInt(path + enchant + ".max-lvl");

            ArrayList<EnchantmentMethod> methods = new ArrayList<>();

            for (String enchantLvl : config.getConfigurationSection(path + enchant + ".levels").getKeys(false)) {

                int lvl = Integer.parseInt(enchantLvl);
                double chance = config.getDouble(path + enchant + ".levels." + enchantLvl + ".chance");
                int cooldown = config.getInt(path + enchant + ".levels." + enchantLvl + ".cooldown");
                EnchantmentMethod method = new EnchantmentMethod(lvl, enchant, chance, cooldown);

                for (String effect : config.getStringList(path + enchant + ".levels." + enchantLvl + ".effects"))
                    method.getEffects().add(effect);

                methods.add(method);
            }

            CustomEnchant customEnchant = new CustomEnchant(enchantName,
                    category, maxLvl, triggers, appliances, methods);

            Skyblock.get().getEnchantHandler().getEnchants().add(customEnchant);
            Skyblock.info("Loaded enchantment: " + enchantName);
        }
    }
}
