package net.syphlex.skyblock.handler.island.block;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Material;

@Getter
@Setter
public class IslandBlockData {
    private final Position position;
    private final Material material;
    private int amount;

    public IslandBlockData(Position position, Material material, int amount) {
        this.position = position;
        this.material = material;
        this.amount = amount;
    }

    public boolean isSpecialBlock(){
        for (SpecialBlockData special : Skyblock.get().getUpgradeHandler().getSpecialBlocks()) {
            if (special.getMaterial() == material)
                return true;
        }
        return false;
    }

    public String getAsString() {
        return String.valueOf(this.position.getAsString() + ":"
                + this.material.name() + ":"
                + this.amount);
    }
}
