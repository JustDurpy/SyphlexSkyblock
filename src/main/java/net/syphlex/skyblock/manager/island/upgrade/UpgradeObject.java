package net.syphlex.skyblock.manager.island.upgrade;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.manager.gui.type.GuiItem;

@Getter
@Setter
public class UpgradeObject {

    private final boolean enabled;
    private final double[] costs;
    private final double[] values;

    private final GuiItem guiItem;

    /*
    this is for each an every individual island data
     */
    private int level = 0;

    public UpgradeObject(boolean enabled, double[] costs, double[] values, GuiItem guiItem){
        this.enabled = enabled;
        this.costs = costs;
        this.values = values;
        this.guiItem = guiItem;
    }

    public double get(){
        return this.values[this.level];
    }

    public int getAsInt(){
        return (int)this.values[this.level];
    }

    public int getCost(){
        return (int)this.costs[this.level];
    }

    public boolean canUpgrade(){
        return this.level < this.values.length - 1;
    }

    /*
    NOTE: must check if the object CAN upgrade prior to being called
     */

    public int getNextCost(){
        return (int)this.costs[this.level + 1];
    }

    public double getNextValue(){
        return this.values[this.level + 1];
    }
}
