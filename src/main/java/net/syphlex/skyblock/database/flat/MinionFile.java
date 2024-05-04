package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.Position;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("all")
public class MinionFile extends SimpleConfig {

    public MinionFile() {
        super("/miniondata/", true);
    }

    public ArrayList<Minion> read(){

        for (File f : getFile().listFiles()) {

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);

            UUID minionUuid = UUID.fromString(config.getString("uuid"));
            Position position = new Position(config.getString("position"));
            Position chest;
            if (!config.getString("chest").equalsIgnoreCase("null")) {
                chest = new Position(config.getString("chest"));
            } else {
                chest = null;
            }


        }
        return null;
    }

    public void write(Minion minion){



    }
}
