package net.syphlex.skyblock.manager.island.block;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.hologram.Hologram;
import net.syphlex.skyblock.util.utilities.StringUtil;

@Getter
@Setter
public class IslandBlockData {
    private final Position position;
    private final SpecialBlockData blockData;
    private final Hologram hologram;
    private int amount;

    public IslandBlockData(Position position, SpecialBlockData blockData, int amount) {
        this.position = position;
        this.blockData = blockData;
        this.amount = amount;

        this.hologram = new Hologram(position.clone().add(0.5, -0.5, 0.5));
        this.hologram.updateLine(0, StringUtil.CC(blockData.getDisplayName()
                .replace("%amount%", String.valueOf(amount))));
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
