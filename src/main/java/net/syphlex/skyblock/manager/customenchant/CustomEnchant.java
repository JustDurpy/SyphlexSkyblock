package net.syphlex.skyblock.manager.customenchant;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@AllArgsConstructor
public class CustomEnchant
{

    private final String name;
    private final Category category;
    private final int maxLvl;
    private final Set<Trigger> triggers;
    private final Set<Appliance> appliances;
    private final ArrayList<EnchantmentMethod> methods;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public int getLevel(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getInteger(this.name) == null ? -1 : nbt.getInteger(this.name);
    }

    public void onEvent(Player player, ItemStack i, CustomEnchant enchant, int level, Event e) {

        for (EnchantmentMethod method : this.methods) {

            if (method.hasCooldown()) {
                for (Map.Entry<UUID, Long> entry : cooldowns.entrySet()) {
                    long started = entry.getValue();
                    if ((System.currentTimeMillis() - started) / 1000L >= method.getCooldown()) {
                        cooldowns.remove(entry.getKey());
                    }
                }
            }

            if (method.getLevel() == level) {

                if (method.hasCooldown()) {

                    if (cooldowns.containsKey(player.getUniqueId())) {
                        //long started = cooldowns.get(player.getUniqueId());
                        //long secondsLeft = (method.getCooldown() - ((System.currentTimeMillis() - started) / 1000L));
                        return;
                    }

                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                }

                if ((int) (Math.random() * 101) <= method.getChance()) {

                    if (e instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                        if (event.getEntity() instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity)event.getEntity();
                            method.run(player, i, living, enchant, event);
                        }
                    } else if (e instanceof EntityDeathEvent) {
                        EntityDeathEvent event = (EntityDeathEvent) e;
                        if (event.getEntity() != null) {
                            LivingEntity living = (LivingEntity)event.getEntity();
                            method.run(player, i, living, enchant, event);
                        }
                    } else {
                        method.run(player, i, null, enchant, e);
                    }
                }
            }
        }
    }
}
