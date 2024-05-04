package net.syphlex.skyblock.manager.island.block;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.data.Position;

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
