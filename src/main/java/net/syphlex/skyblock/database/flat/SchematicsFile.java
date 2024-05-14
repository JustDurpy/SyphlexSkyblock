package net.syphlex.skyblock.database.flat;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.schematic.paster.FastAsyncWorldEdit;
import net.syphlex.skyblock.manager.schematic.paster.SchematicPaster;
import net.syphlex.skyblock.util.simple.SimpleConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SchematicsFile extends SimpleConfig {

    @Getter
    private final Map<String, File> schematicFiles = new HashMap<>();

    public SchematicsFile() {
        super("/schematics/", true);
    }

    public void read(){
        for (File file : getFile().listFiles()) {

            int index = !file.getName().contains(".schematic") ?
                    file.getName().indexOf(".schem") : file.getName().indexOf(".schematic");

            if (index < 1) {
                Skyblock.log("Unable to load " + file.getName() + " schematic!");
                Skyblock.log("There seems to be a problem with the file name.");
                continue;
            }

            this.schematicFiles.put(file.getName().substring(0, index - 1), file);
        }
    }

    public void write(){
        this.schematicFiles.clear();
    }
}
