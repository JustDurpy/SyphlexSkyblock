package net.syphlex.skyblock.util.hologram;

import net.syphlex.skyblock.util.data.Position;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

@SuppressWarnings("all")
public class HologramLine {

    private final ArmorStand armorStand;

    public HologramLine(Location l, String display){
        this.armorStand = l.getWorld().spawn(l, ArmorStand.class);
        this.armorStand.setGravity(false);
        this.armorStand.setVisible(false);
        this.armorStand.setCustomName(display);
        this.armorStand.setCustomNameVisible(true);
    }

    public void unregisterLine(){
        this.armorStand.remove();
    }

    public void update(String display){
        //if (!display.equals(this.armorStand.getCustomName()))
            this.armorStand.setCustomName(display);
    }

}
