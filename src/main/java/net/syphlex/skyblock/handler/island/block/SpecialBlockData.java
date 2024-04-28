package net.syphlex.skyblock.handler.island.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class SpecialBlockData {
    private final Material material;
    private final float worth;
    private final String displayName;

    public String getAsString(){
        return this.material.name() + ":" + this.worth;
    }
}
