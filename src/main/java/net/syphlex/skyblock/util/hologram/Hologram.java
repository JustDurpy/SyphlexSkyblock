package net.syphlex.skyblock.util.hologram;

import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.utilities.StringUtil;

import java.util.HashMap;

public class Hologram {

    private final Position position;
    private final HashMap<Integer, HologramLine> entries;

    public Hologram(Position position){
        this.position = position;
        this.entries = new HashMap<>();
    }

    public void delete(){
        for (HologramLine line : this.entries.values())
            line.unregisterLine();
        this.entries.clear();
    }

    public void removeLine(int line){
        HologramLine l = this.entries.remove(line);
        if (l != null) l.unregisterLine();
    }

    public void updateLine(int line, String display){
        if (this.entries.get(line) != null) {
            this.entries.get(line).update(StringUtil.CC(display));
        } else {
            this.entries.put(line, new HologramLine(position.getAsBukkit().add(0, 0.28, 0), StringUtil.CC(display)));
        }
    }

}
