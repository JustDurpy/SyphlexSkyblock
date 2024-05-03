package net.syphlex.skyblock.database.flat;

import lombok.Getter;
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
            this.schematicFiles.put(file.getName().substring(0, file.getName().length() - 6), file);
        }
    }

    public void write(){
        this.schematicFiles.clear();
    }
}
