package net.syphlex.skyblock.handler.mine;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.Position;
import org.bukkit.Material;

import java.util.ArrayList;

@Setter
@Getter
public class Mine {

    private final int id;
    private final String mineName;
    private Position corner1, corner2;

    private final ArrayList<Material> blocks = new ArrayList<>();

    public Mine(int id, String mineName){
        this.id = id;
        this.mineName = mineName;
    }
}
