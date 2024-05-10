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
                    config.addDefault("profile.island-role", "Visitor");
                    config.addDefault("profile.mobcoins", 0);
                    save();
                }

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                String identifier = config.getString("profile.island");
                String islandRole = config.getString("profile.island-role");

                int mobCoins = config.getInt("profile.mobcoins");

                int[] id = IslandUtil.getId(identifier);

                if (id[0] != -1 && id[1] != -1)
                    profile.setIsland(Skyblock.get().getIslandHandler().getGrid().get(id));

                profile.getMemberProfile().setRole(IslandRole.get(islandRole));

                profile.setMobCoins(mobCoins);
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

                config.set("profile.island-role", profile.getMemberProfile().getRole().getIdentifier());
                config.set("profile.mobcoins", profile.getMobCoins());

                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
