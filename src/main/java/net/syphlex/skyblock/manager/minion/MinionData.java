package net.syphlex.skyblock.manager.minion;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.util.Pair;

import java.util.ArrayList;

@Getter
@Setter
public class MinionData {
    private int maxLvl;
    private int radius;
    private Minion.Type type;
    private final ArrayList<Pair<Integer, Object>> objects = new ArrayList<>();
}
