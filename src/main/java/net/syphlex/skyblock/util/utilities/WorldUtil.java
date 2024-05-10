package net.syphlex.skyblock.util.utilities;

import lombok.experimental.UtilityClass;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.mine.data.MineBlockData;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class WorldUtil {

    public void createWorld(World.Environment environment, String name) {
        WorldCreator worldCreator = new WorldCreator(name)
                .generator(Skyblock.get().getDefaultWorldGenerator(name, null))
                .environment(environment);
        World world = Bukkit.createWorld(worldCreator);
        Bukkit.createWorld(worldCreator);
    }

    public boolean isWorld(World world1, World world2) {

        if (world1 == null || world2 == null)
            return false;

        return world1.getName().equalsIgnoreCase(world2.getName());
    }

    public List<LivingEntity> getLivingEntities(Location l, double radius){
        List<LivingEntity> entities = new ArrayList<>();
        for (double y = -radius; y <= radius; y += radius)
            for (double x = -radius; x <= radius; x += radius)
                for (double z = -radius; z <= radius; z += radius)
                    for (Entity e : l.getWorld().getNearbyEntities(l, x, y, z))
                        if (e instanceof LivingEntity)
                            entities.add((LivingEntity) e);
        return entities;
    }
}
