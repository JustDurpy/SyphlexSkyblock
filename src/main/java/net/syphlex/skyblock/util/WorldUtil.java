package net.syphlex.skyblock.util;

import lombok.experimental.UtilityClass;
import net.syphlex.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

@UtilityClass
public class WorldUtil {

    public void createWorld(World.Environment environment, String name) {
        WorldCreator worldCreator = new WorldCreator(name)
                .generator(Skyblock.get().getDefaultWorldGenerator(name, null))
                .environment(environment);
        World world = Bukkit.createWorld(worldCreator);
        Bukkit.createWorld(worldCreator);
    }

    public boolean isWorld(World world1, World world2){
        return world1.getName().equalsIgnoreCase(world2.getName());
    }
}
