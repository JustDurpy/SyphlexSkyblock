package net.syphlex.skyblock.manager.island.upgrade.oregenerator;

import lombok.Getter;
import net.syphlex.skyblock.manager.island.block.OreGeneratorBlockData;
import net.syphlex.skyblock.util.utilities.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class IslandOreGenerator {

    private final int id;
    private final String name;
    private final ArrayList<OreGeneratorBlockData> blocks = new ArrayList<>();

    public IslandOreGenerator(final int id, final String name){
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public OreGeneratorBlockData generate(){
        List<OreGeneratorBlockData> compositions = blocks;
        int totalPercentage = 0;
        for(OreGeneratorBlockData comp : compositions) {
            totalPercentage = totalPercentage + (int)Math.round(comp.getChance());
        }

        if(totalPercentage == 0) return null;

        Random random = new Random();
        int index =  random.nextInt(totalPercentage);
        int sum = 0, i = 0;
        while (sum < index) {
            sum = sum + (int)Math.round(compositions.get(i++).getChance());
        }
        return compositions.get(Math.max(0, i - 1));
    }
}
