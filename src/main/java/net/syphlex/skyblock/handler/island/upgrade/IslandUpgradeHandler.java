package net.syphlex.skyblock.handler.island.upgrade;

import net.syphlex.skyblock.database.flat.OreGeneratorFile;
import net.syphlex.skyblock.handler.island.upgrade.oregenerator.IslandOreGenerator;

import java.util.ArrayList;

public class IslandUpgradeHandler {

    private ArrayList<IslandOreGenerator> oreGenerators;

    public void onEnable(){
        OreGeneratorFile oreGeneratorFile = new OreGeneratorFile();
        this.oreGenerators = oreGeneratorFile.read();
    }

    public void onDisable(){

    }

    public IslandOreGenerator getOreGenerator(int tier){
        for (IslandOreGenerator generator : this.oreGenerators) {
            if (generator.getTier() == tier)
                return generator;
        }
        return null;
    }
}
