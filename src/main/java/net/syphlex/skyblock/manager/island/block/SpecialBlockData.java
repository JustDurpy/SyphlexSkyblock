package net.syphlex.skyblock.manager.island.block;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class SpecialBlockData {
    private final Material material;
    private final float worth;
    private final String displayName;
    private String entitySpawnerName = "null";

    public SpecialBlockData(Material material, float worth, String displayName){
        this.material = material;
        this.worth = worth;
        this.displayName = displayName;
    }

    public SpecialBlockData(Material material, float worth, String displayName, String entitySpawnerName){
        this.material = material;
        this.worth = worth;
        this.displayName = displayName;
        this.entitySpawnerName = entitySpawnerName;
    }

    public String getAsString(){
        if (entitySpawnerName.equalsIgnoreCase("null")) {
            return this.material.name() + ":" + this.worth + ":" + this.displayName;
        }
        return this.material.name() + ":" + this.worth + ":" + this.displayName + ":" + this.entitySpawnerName;
    }
}
