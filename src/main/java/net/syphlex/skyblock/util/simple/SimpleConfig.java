package net.syphlex.skyblock.util.simple;

import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class SimpleConfig {

    private final File file;
    public final FileConfiguration config;


    public SimpleConfig(String path, boolean dir){
        this.file = new File(Skyblock.get().getDataFolder() + path);

        if (!Skyblock.get().getDataFolder().exists())
            Skyblock.get().getDataFolder().mkdirs();

        try {
            if (!this.file.exists()){
                if (dir) this.file.mkdirs();
                this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save(){
        try {
            if (this.file.isDirectory())
                return;
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
