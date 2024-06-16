package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProfileFile extends SimpleConfig {

    public ProfileFile() {
        super("/profiles/", true);
    }

    public void read(Profile profile) {

        //if (getFile().listFiles().length <= 0)
        //    return;

        Skyblock.get().getThreadHandler().fire(() -> {
            try {
                File f = new File(getFile().getPath() + "/"
                        + profile.getPlayer().getUniqueId() + ".yml");

                if (!f.exists()) {
                    f.createNewFile();

                    FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                    config.options().copyDefaults(true);
                    config.addDefault("profile.island", "null");
                    save();
                }

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                String identifier = config.getString("profile.island");

                int[] id = IslandUtil.getId(identifier);

                if (id[0] != -1 && id[1] != -1)
                    profile.setIsland(Skyblock.get().getIslandHandler().getGrid().get(id));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void write(Profile profile) {
        Skyblock.get().getThreadHandler().fire(() -> {
            try {
                if (profile == null)
                    return;

                if (!getFile().exists())
                    getFile().mkdirs();

                File f = new File(getFile().getPath() + "/"
                        + profile.getPlayer().getUniqueId() + ".yml");

                if (!f.exists())
                    f.createNewFile();

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                if (profile.getIsland() != null) {
                    config.set("profile.island", profile.getIsland().getIdentifier());
                } else {
                    config.set("profile.island", "null");
                }

                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isRecorded(UUID uuid) {
        File f = new File(getFile().getPath() + "/" + uuid.toString() + ".yml");
        return f.exists();
    }

    public void set(UUID uuid, String path, Object o){
        Skyblock.get().getThreadHandler().fire(() -> {
            try {
                if (!getFile().exists())
                    getFile().mkdirs();

                File f = new File(getFile().getPath() + "/" + uuid.toString() + ".yml");

                if (!isRecorded(uuid))
                    return;

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                config.set("profile." + path, o);
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
