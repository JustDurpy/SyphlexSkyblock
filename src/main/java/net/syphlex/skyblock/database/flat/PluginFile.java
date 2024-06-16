package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.license.License;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import org.bukkit.Bukkit;

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

        save();

        new LicenseFile().e();

        for (ConfigEnum configEnum : ConfigEnum.values()) {
            String identifier = configEnum.name().toLowerCase();
            configEnum.set(config.get("settings." + identifier));
        }

        for (Messages messages : Messages.values()) {
            String identifier = messages.name().toLowerCase();
            messages.set(config.getString("messages." + identifier));
        }
    }

    public void write(){
    }

    public class LicenseFile extends SimpleConfig {

        public LicenseFile() {
            super("/license.yml", false);
        }

        // verify page: https://hebdomadal-contribu.000webhostapp.com/License/verify.php

        public void e(){

            config.options().copyDefaults(true);
            config.addDefault("license", "license here");

            save();

            final String license = config.getString("license");

            boolean valid = true;

            Skyblock.info(" ");
            Skyblock.info("Validating your license...");
            Skyblock.info(" ");

            //valid = new License(license,
            //        "https://hebdomadal-contribu.000webhostapp.com/License/verify.php",
            //        Skyblock.get())
            //        .isValid() == License.ValidationType.VALID;

            if (!valid) {
                Skyblock.info("License is not valid! Disabling plugin! Please contact Durpy for assistance.");
                Bukkit.getScheduler().cancelTasks(Skyblock.get());
                Bukkit.getPluginManager().disablePlugin(Skyblock.get());
            } else {
                Skyblock.info("License is valid! Loading plugin...");
            }
        }
    }
}
