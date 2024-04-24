package net.syphlex.skyblock.util;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Setter
@Getter
public class Position {

    private final World world;
    private double x, y, z;

    public Position(String s) {

        if (s.equalsIgnoreCase("null")){
            this.world = null;
            return;
        }

        String[] split = s.split(";");

        if (split.length < 4) {
            this.world = null;
            return;
        }

        this.world = Bukkit.getWorld(split[0]);
        try {

            this.x = Double.parseDouble(split[1]);
            this.y = Double.parseDouble(split[2]);
            this.z = Double.parseDouble(split[3]);

        } catch (NumberFormatException e) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }

    public Position(World world, double x, double y, double z){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void teleport(Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                Location bukkit = getAsBukkit().add(0, 1, 0);
                player.teleport(bukkit);
                //Skyblock.get().getIslandHandler().generateIslandBorder(player, Color.BLUE);
            }
        }.runTaskLater(Skyblock.get(), 2L);
    }

    public void setBlock(Material material){
        new BukkitRunnable(){
            @Override
            public void run(){

                if (world == null)
                    return;

                world.getBlockAt(
                        MathHelper.floor_double(x),
                        MathHelper.floor_double(y),
                        MathHelper.floor_double(z))
                        .setType(material);
            }
        }.runTask(Skyblock.get());
    }

    public Location getAsBukkit(){
        return new Location(this.world, this.x, this.y, this.z);
    }

    public String getAsString() {
        if (this.world == null)
            return "null";
        return this.world.getName() + ";"
                + this.x + ";"
                + this.y + ";"
                + this.z;
    }

    public int getBlockX(){
        return MathHelper.floor_double(this.x);
    }

    public int getBlockY(){
        return MathHelper.floor_double(this.y);
    }

    public int getBlockZ(){
        return MathHelper.floor_double(this.z);
    }

    public Position add(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Position clone(){
        return new Position(this.world, this.x, this.y, this.z);
    }
}
