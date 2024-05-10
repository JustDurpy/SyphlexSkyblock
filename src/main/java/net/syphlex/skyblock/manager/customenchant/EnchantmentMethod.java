package net.syphlex.skyblock.manager.customenchant;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.utilities.PluginUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.utilities.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EnchantmentMethod {

    private final int cooldown;
    private final int level;
    private final ArrayList<String> effects;
    private final String enchantName;
    private final double chance;

    public EnchantmentMethod(int level, String enchantName, double chance, int cooldown){
        this.level = level;
        this.enchantName = enchantName;
        this.effects = new ArrayList<>();
        this.chance = chance;
        this.cooldown = cooldown;
    }

    public void run(Player player, ItemStack item, LivingEntity entity, CustomEnchant enchant, Event event) {

        List<CustomEnchantTask> tasks = new ArrayList<>();

        for (String effect : effects) {

            String[] args = effect.split(":");
            String type = args[0];
            String targetString = args[1];

            LivingEntity targetVar = null;

            if (entity == null) {
                //Skyblock.log("ERROR: " + this.enchantName + " enchantments applied to does not have a victim variable");
                //Skyblock.log("ERROR: automatically forwarding to player.");
                targetVar = player;
            }

            if (targetString.equalsIgnoreCase("%victim%")
                    || type.equalsIgnoreCase("mob_drop")) {
                targetVar = entity;
            }

            if (targetString.equalsIgnoreCase("%player%")) {
                targetVar = player;
            }

            if (targetVar == null) {
                Skyblock.log("ERROR: TARGET WAS FOUND NULL FOR " + this.enchantName);
                return;
            }

            final LivingEntity target = targetVar;

            if (type.equalsIgnoreCase("potion")) {

                if (args.length < 5) {
                    errorEffect();
                    return;
                }

                String potionEffectString = args[2];
                PotionEffectType potionEffect = PotionEffectType.getByName(potionEffectString);

                int duration;
                int amplifier;

                if (!StringUtil.isNumber(args[3])) {
                    duration = PotionEffect.INFINITE_DURATION;
                } else {
                    duration = Integer.parseInt(args[3]) * 20;
                }

                if (!StringUtil.isNumber(args[4])){
                    Skyblock.log("ERROR: AMPLIFIER MUST BE A NUMBER FOR " + this.enchantName);
                    return;
                }

                amplifier = Integer.parseInt(args[4]) - 1;

                if (potionEffect == null) {
                    Skyblock.log("ERROR: INVALID POTION EFFECT FOR " + this.enchantName);
                    return;
                }

                tasks.add(new CustomEnchantTask(() -> target.addPotionEffect(new PotionEffect(potionEffect, duration, amplifier))));
                //target.addPotionEffect(new PotionEffect(potionEffect, duration, amplifier));

            }
            if (type.equalsIgnoreCase("lightning")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: DAMAGE MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double damage = Double.parseDouble(args[2]);
                tasks.add(new CustomEnchantTask(() -> {
                    target.damage(damage);
                    target.getWorld().strikeLightningEffect(target.getLocation());
                }));
            }
            if (type.equalsIgnoreCase("fire")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: DURATION MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                int duration = (int) Double.parseDouble(args[2]);
                tasks.add(new CustomEnchantTask(() -> target.setFireTicks(duration * 20)));

            }
            if (type.equalsIgnoreCase("invincible")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: DURATION MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                int duration = (int) Double.parseDouble(args[2]);
                target.setNoDamageTicks(duration * 20);

            }
            if (type.equalsIgnoreCase("increase_damage")) {
                if (event instanceof EntityDamageByEntityEvent) {

                    EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;

                    if (args.length != 3) {
                        errorEffect();
                        return;
                    }

                    if (!StringUtil.isNumber(args[2])) {
                        Skyblock.log("ERROR: DAMAGE MULTIPLIER MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                        return;
                    }

                    double multiplier = Double.parseDouble(args[2]);
                    damageEvent.setDamage(damageEvent.getDamage() * multiplier);
                }
            }
            if (type.equalsIgnoreCase("reduce_damage")) {

                if (event instanceof EntityDamageEvent) {

                    EntityDamageEvent damageEvent = (EntityDamageEvent) event;

                    if (args.length != 3) {
                        errorEffect();
                        return;
                    }

                    if (!StringUtil.isNumber(args[2])) {
                        Skyblock.log("ERROR: DAMAGE REDUCTION MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                        return;
                    }

                    double reduction = Double.parseDouble(args[2]);
                    damageEvent.setDamage(damageEvent.getDamage() - reduction);
                }
            }
            if (type.equalsIgnoreCase("set_damage")) {

                if (!(event instanceof EntityDamageByEntityEvent))
                    return;

                EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;

                if (args.length != 3) {
                    errorEffect();
                    return;
                }


                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: DAMAGE MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double damage = Double.parseDouble(args[2]);
                damageEvent.setDamage(damage);

            }
            if (type.equalsIgnoreCase("steal_health")) {

                if (!(event instanceof EntityDamageByEntityEvent))
                    return;

                EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: STEAL AMOUNT MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double steal = Double.parseDouble(args[2]) * 2;
                damageEvent.setDamage(steal);
                player.setHealth(player.getHealth() + steal);

            }
            if (type.equalsIgnoreCase("instant_break")) {

                if (event instanceof BlockDamageEvent) {

                    BlockDamageEvent damageEvent = (BlockDamageEvent) event;

                    if (!args[1].equalsIgnoreCase("*")) {

                        List<Material> materials = new ArrayList<>();

                        for (int i = 1; i < args.length; i++) {
                            Material m = PluginUtil.getMaterial(args[i], null);
                            if (m == null) {
                                Skyblock.log("ERROR: BLOCK MATERIAL IS UNKNOWN IN CONFIG FOR " + this.enchantName);
                                continue;
                            }
                            materials.add(m);
                        }

                        if (materials.isEmpty()) {
                            Skyblock.log("ERROR: NO BLOCKS FOUND IN CONFIG FOR " + this.enchantName);
                            return;
                        }

                        for (Material m : materials) {
                            if (damageEvent.getBlock().getType() == m)
                                damageEvent.setInstaBreak(true);
                        }
                    }
                    damageEvent.setInstaBreak(true);
                }

            }
            if (type.equalsIgnoreCase("push")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: PUSH MULTIPLIER MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double multiplier = Double.parseDouble(args[2]);

                Location start = player.getLocation().clone();
                start.setY(0);
                Location targetLoc = target.getLocation().clone();
                targetLoc.setY(0);

                Location difference = player.getLocation().subtract(target.getLocation());
                Vector vector = difference.toVector().normalize().multiply(-multiplier).setY(multiplier * (1.0 / 4.0));
                tasks.add(new CustomEnchantTask(() -> target.setVelocity(vector)));
            }
            if (type.equalsIgnoreCase("pull")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: PULL MULTIPLIER MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double multiplier = Double.parseDouble(args[2]);

                Location difference = player.getLocation().subtract(target.getLocation());
                Vector vector = difference.toVector().normalize().multiply(multiplier);
                tasks.add(new CustomEnchantTask(() -> target.setVelocity(vector)));
            }
            if (type.equalsIgnoreCase("launch")) {

                if (args.length != 3) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: LAUNCH MULTIPLIER MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double multiplier = Double.parseDouble(args[2]);

                tasks.add(new CustomEnchantTask(() -> {
                    target.setVelocity(target.getVelocity().setY(multiplier).normalize());
                }));
            }
            if (type.equalsIgnoreCase("mob_drop")) {

                if (!(event instanceof EntityDeathEvent))
                    return;

                EntityDeathEvent deathEvent = (EntityDeathEvent) event;

                ArrayList<ItemStack> drops = new ArrayList<>(deathEvent.getDrops());

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: MOB DROP MULTIPLIER MUST BE A NUMBER FOR " + this.enchantName);
                    return;
                }

                double multiplier = Double.parseDouble(args[1]);
                deathEvent.getDrops().clear();
                for (ItemStack i : drops) {
                    i.setAmount((int) (i.getAmount() * multiplier));
                    deathEvent.getEntity().getWorld().dropItemNaturally(deathEvent.getEntity().getLocation(), i);
                }
            }
            if (type.equalsIgnoreCase("exp")) {

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: EXP AMOUNT MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                float exp = Float.parseFloat(args[1]);
                tasks.add(new CustomEnchantTask(() -> player.setExp(player.getExp() + exp)));
            }
            if (type.equalsIgnoreCase("heal")) {

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: HEAL AMOUNT MUST BE A NUMBER IN CONFIG FOR " + this.enchantName);
                    return;
                }

                double amount = Double.parseDouble(args[1]);
                tasks.add(new CustomEnchantTask(() -> player.setHealth(player.getHealth() + amount)));

            }
            if (type.equalsIgnoreCase("feed")) {

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: FEED AMOUNT MUST BE A NUMBER IN CONFIG " + this.enchantName);
                    return;
                }

                int amount = (int) Double.parseDouble(args[1]);
                tasks.add(new CustomEnchantTask(() -> player.setFoodLevel(player.getFoodLevel() + amount)));

            }
            if (type.equalsIgnoreCase("health_boost")) {

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: HEALTH BOOST AMOUNT MUST BE A NUMBER IN CONFIG " + this.enchantName);
                    return;
                }

                int boost = (int) Double.parseDouble(args[1]);
                tasks.add(new CustomEnchantTask(() -> player.setMaxHealth(boost + 20)));
            }
            if (type.equalsIgnoreCase("blackhole")) {

                if (args.length != 5) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[2])) {
                    Skyblock.log("ERROR: BLACK HOLE RADIUS MUST BE A NUMBER IN THE CONFIG " + this.enchantName);
                    return;
                }

                if (!StringUtil.isNumber(args[3])) {
                    Skyblock.log("ERROR: BLACK HOLE DAMAGE MUST BE A NUMBER IN THE CONFIG " + this.enchantName);
                    return;
                }

                if (!StringUtil.isNumber(args[4])) {
                    Skyblock.log("ERROR: BLACK HOLE DURATION MUST BE A NUMBER IN THE CONFIG " + this.enchantName);
                    return;
                }

                double radius = Double.parseDouble(args[2]);
                double damage = Double.parseDouble(args[3]);
                double duration = Double.parseDouble(args[4]);

                Location blackhole = target.getLocation();
                Location blackHoleBlock = blackhole.clone().add(0, 1, 0);
                Material before = blackHoleBlock.getBlock().getType();
                if (before == Material.AIR)
                    blackHoleBlock.getBlock().setType(Material.COAL_BLOCK);

                new CustomEnchantTimerTask(() -> {
                    for (LivingEntity e : WorldUtil.getLivingEntities(blackhole, radius)) {
                        if (e.getEntityId() == player.getEntityId())
                            continue;
                        e.teleport(blackhole);
                    }
                }, () -> {
                    for (LivingEntity e : WorldUtil.getLivingEntities(blackhole, radius)) {

                        if (e.getEntityId() == player.getEntityId()
                                || PluginUtil.distanceXZ(e.getLocation(), blackhole) > 2)
                            continue;

                        e.damage(damage);
                    }
                }, () -> {
                    if (blackHoleBlock.getBlock().getType() == Material.COAL_BLOCK)
                        blackHoleBlock.getBlock().setType(before);
                }, duration);
            }
            if (type.equalsIgnoreCase("durability")) {

                if (args.length != 2) {
                    errorEffect();
                    return;
                }

                if (!StringUtil.isNumber(args[1])) {
                    Skyblock.log("ERROR: DURABILITY AMOUNT MUST BE A NUMBER IN CONFIG FOR: " + this.enchantName);
                    return;
                }

                double amount = Double.parseDouble(args[1]);
                tasks.add(new CustomEnchantTask(() -> item.setDurability((short) (item.getDurability() + amount))));
            }
            if (type.equalsIgnoreCase("tray")) {

                if (event instanceof BlockBreakEvent) {

                    BlockBreakEvent breakEvent = (BlockBreakEvent) event;

                    if (args.length != 3) {
                        errorEffect();
                        return;
                    }

                    if (!StringUtil.isNumber(args[1])) {
                        Skyblock.log("ERROR: RADIUS MUST BE A NUMBER IN CONFIG FOR: " + this.enchantName);
                        return;
                    }

                    String[] blockSplit = args[2].split(",");
                    List<Material> allowed = new ArrayList<>();
                    for (String s : blockSplit) {
                        Material m = PluginUtil.getMaterial(s, Material.AIR);
                        allowed.add(m);
                    }

                    int radius = (int)Double.parseDouble(args[1]);
                    Location l = breakEvent.getBlock().getLocation();
                    int minX = l.getBlockX() - radius / 2;
                    int minY = l.getBlockY() - radius / 2;
                    int minZ = l.getBlockZ() - radius / 2;
                    int x = minX;
                    while (x < minX + radius) {
                        int y = minY;
                        while (y < minY + radius) {
                            int z = minZ;
                            while (z < minZ + radius) {
                                for (Material m : allowed) {
                                    if (breakEvent.getBlock().getWorld().getBlockAt(x, y, z).getType() == m) {
                                        Block block = breakEvent.getBlock().getWorld().getBlockAt(x, y, z);
                                        //if (Main.wGuard.canBuild(p, e.getBlock().getWorld().getBlockAt(x, y, z))) {
                                        for (ItemStack i : block.getDrops()) {
                                            player.getInventory().addItem(i);
                                        }
                                        if (player.getInventory().firstEmpty() != -1) {
                                            block.breakNaturally(new ItemStack(Material.AIR));
                                        } else {
                                            block.breakNaturally();
                                        }
                                        player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 0.5));
                                    }
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
            }
            if (type.equalsIgnoreCase("replant")) {

                if (event instanceof BlockBreakEvent) {

                    BlockBreakEvent breakEvent = (BlockBreakEvent) event;

                    final Block block = breakEvent.getBlock();

                    if (!(block.getBlockData() instanceof Ageable))
                        return;

                    Ageable age = (Ageable) block.getBlockData();

                    if (block.getType() == Material.SUGAR_CANE
                            || block.getType() == Material.CACTUS) {

                        Block blockUnder = block.getLocation().clone().add(0, -1, 0).getBlock();

                        if (!blockUnder.getType().isSolid())
                            return;
                    }

                    if (age.getAge() >= age.getMaximumAge()) {
                        for (ItemStack i : block.getDrops())
                            block.getWorld().dropItemNaturally(block.getLocation(), i);
                    }

                    breakEvent.setCancelled(true);
                    age.setAge(0);
                    block.setBlockData(age);
                }
                if (event instanceof BlockDamageEvent) {

                    BlockDamageEvent damageEvent = (BlockDamageEvent) event;

                    if (!args[1].equalsIgnoreCase("*")) {

                        List<Material> materials = new ArrayList<>();

                        for (int i = 1; i < args.length; i++) {
                            Material m = PluginUtil.getMaterial(args[i], null);
                            if (m == null) {
                                Skyblock.log("ERROR: BLOCK MATERIAL IS UNKNOWN IN CONFIG FOR " + this.enchantName);
                                continue;
                            }
                            materials.add(m);
                        }

                        if (materials.isEmpty()) {
                            Skyblock.log("ERROR: NO BLOCKS FOUND IN CONFIG FOR " + this.enchantName);
                            return;
                        }

                        for (Material m : materials) {
                            if (damageEvent.getBlock().getType() == m)
                                damageEvent.setInstaBreak(true);
                        }
                    }
                    damageEvent.setInstaBreak(true);
                }
            }
        }

        for (CustomEnchantTask task : tasks) {
            task.runnable().run();
        }
    }

    private void errorEffect(){
        Skyblock.log(" ");
        Skyblock.log("ERROR: INVALID EFFECT SYNTAX FOR " + this.enchantName);
        Skyblock.log("Cancelling effect process.");
        Skyblock.log(" ");
    }

    public boolean hasCooldown(){
        return this.cooldown > 0;
    }
}
