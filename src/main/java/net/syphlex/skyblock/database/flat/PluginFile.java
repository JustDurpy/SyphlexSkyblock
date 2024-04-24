package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.util.SimpleConfig;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;

public class PluginFile extends SimpleConfig {
    public PluginFile() {
        super("/skyblock.yml", false);
    }

    public void read(){

        config.options().copyDefaults(true);

        for (ConfigEnum configEnum : ConfigEnum.values()) {
            String identifier = configEnum.name().toLowerCase();
            config.addDefault("settings." + identifier, configEnum.get());
        }

        for (Messages messages : Messages.values()) {
            String identifier = messages.name().toLowerCase();
            config.addDefault("messages." + identifier, messages.getMsg());
        }

        for (Permissions permissions : Permissions.values()) {
            String identifier = permissions.name().toLowerCase();
            config.addDefault("permissions." + identifier, permissions.get());
        }

        save();

        for (ConfigEnum configEnum : ConfigEnum.values()) {
            String identifier = configEnum.name().toLowerCase();
            configEnum.set(config.get("settings." + identifier));
        }

        for (Messages messages : Messages.values()) {
            String identifier = messages.name().toLowerCase();
            messages.set(config.getString("messages." + identifier));
        }

        for (Permissions permissions : Permissions.values()) {
            String identifier = permissions.name().toLowerCase();
            permissions.set(config.getString("permissions." + identifier));
        }


    }

    public void write(){

    }
}
