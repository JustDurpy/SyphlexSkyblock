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
    private final SpecialBlockData blockData;
    private int amount;

    public IslandBlockData(Position position, SpecialBlockData blockData, int amount) {
        this.position = position;
        this.blockData = blockData;
        this.amount = amount;
    }

    public boolean isNull(){
        return this.blockData == null;
    }

    public String getAsString() {
        return String.valueOf(this.position.getAsString() + ":"
                + this.blockData.getMaterial().name() + ":"
                + this.amount);
    }
}
