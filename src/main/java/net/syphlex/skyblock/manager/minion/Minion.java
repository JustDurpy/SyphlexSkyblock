package net.syphlex.skyblock.manager.minion;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.PlayerUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public abstract class Minion {

    public final ArrayList<Item> drops = new ArrayList<>();
    public Type type;
    public Position chest, position;
    public ArmorStand armorStand;
    public int level;
    public UUID uuid, owner;

    public Minion(Type type, int level){
        this.type = type;
        this.level = level;
    }

    public abstract void updateData();

    public void update(){
        if (this.armorStand != null) {

            if (!this.position.isSameLocation(this.armorStand.getLocation()))
                this.armorStand.teleport(this.position.getAsBukkit());

            float f = -1f;
            EulerAngle oldRot = this.armorStand.getRightArmPose();
            if (oldRot.getX() >= 1f || oldRot.getX() <= -2f)
                f *= -1;
            EulerAngle newrot = oldRot.add(f, 0, 0);
            this.armorStand.setRightArmPose(newrot);
        }
    }

    public void sendItem(){

        if (!hasChest())
            return;

        if (this.chest.getAsBukkit().getBlock().getState() instanceof DoubleChest) {
            DoubleChest c = (DoubleChest) this.chest.getAsBukkit().getBlock().getState();
            for (Item i : this.drops) {
                if (c.getInventory().firstEmpty() == -1) {
                    this.position.getWorld().dropItemNaturally(this.position.getAsBukkit(), i.getItemStack());
                    continue;
                }
                c.getInventory().addItem(i.getItemStack());
            }
            this.drops.clear();
        } else if (this.chest.getAsBukkit().getBlock().getState() instanceof Chest) {
            Chest c = (Chest) this.chest.getAsBukkit().getBlock().getState();
            for (Item i : this.drops) {
                if (c.getInventory().firstEmpty() == -1) {
                    this.position.getWorld().dropItemNaturally(this.position.getAsBukkit(), i.getItemStack());
                    continue;
                }
                c.getInventory().addItem(i.getItemStack());
            }
            this.drops.clear();
        }
    }

    public void attachChest(Location l){

        Block b = l.getBlock();

        if (b.getType() != Material.CHEST)
            return;

        this.chest = new Position(l);
    }

    public boolean hasChest(){
        if (this.chest != null) {
            if (!(this.chest.getAsBukkit().getBlock().getState() instanceof Chest
                    || this.chest.getAsBukkit().getBlock().getState() instanceof DoubleChest)) {
                this.chest = null;
            }
        }
        return this.chest != null
                && (this.chest.getAsBukkit().getBlock().getState() instanceof Chest
                || this.chest.getAsBukkit().getBlock().getState() instanceof DoubleChest);
    }

    public void placeMinion(Location placedAt){
        this.uuid = Skyblock.get().getMinionHandler().generateUUID();
        this.position = new Position(placedAt);
        createStand();
    }

    public void pickUpMinion(Player p){
        ItemStack egg = Skyblock.get().getMinionHandler().pickUpMinion(this);
        PlayerUtil.giveItem(p, egg);
        destroy();
    }

    public void destroy(){
        Skyblock.get().getMinionHandler().getMinions().remove(this);
        removeStand();
    }

    public void removeStand(){
        this.armorStand.remove();
    }

    public void createStand() {
        this.armorStand = this.position.getWorld().spawn(this.position.getAsBukkit(), ArmorStand.class);

        String name = this.type.getDisplayName().replace("%player%",
                Bukkit.getOfflinePlayer(this.owner).getName());

        this.armorStand.setCustomName(StringUtil.CC(ChatColor.GOLD + name));
        this.armorStand.setCustomNameVisible(true);
        {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(Bukkit.getOfflinePlayer(this.owner).getName());
            skull.setItemMeta(meta);
            this.armorStand.setHelmet(skull);
        }
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(Color.fromRGB(255, 255, 85));
        armor.setItemMeta(meta);
        this.armorStand.setChestplate(armor);

        armor = new ItemStack(Material.LEATHER_LEGGINGS);
        meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(Color.fromRGB(85, 85, 255));
        armor.setItemMeta(meta);
        this.armorStand.setLeggings(armor);

        armor = new ItemStack(Material.LEATHER_BOOTS);
        meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(Color.fromRGB(85, 85, 255));
        armor.setItemMeta(meta);
        this.armorStand.setBoots(armor);

        this.armorStand.setGravity(false);
        this.armorStand.setArms(true);
        this.armorStand.setSmall(true);
    }

    public MinionData getData(){
        return Skyblock.get().getMinionHandler().getMinionDataOf(this);
    }

    public enum Type {
        MINER("&c%player%'s Miner Minion"),
        SLAYER("&c%player%'s Minion"),
        LUMBERJACK("&c%player%'s Lumberjack Minion");

        private final String displayName;

        Type(String displayName){
            this.displayName = displayName;
        }

        public String getDisplayName(){
            return StringUtil.CC(this.displayName);
        }
    }
}
