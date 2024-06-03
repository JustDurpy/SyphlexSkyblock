package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import net.syphlex.skyblock.util.config.ConfigEnum;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.config.Permissions;
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

        for (Permissions permissions : Permissions.values()) {
            String identifier = permissions.name().toLowerCase();
            config.addDefault("permissions." + identifier, permissions.get());
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

        for (Permissions permissions : Permissions.values()) {
            String identifier = permissions.name().toLowerCase();
            permissions.set(config.getString("permissions." + identifier));
        }
    }

    public void write(){
    }

    public class LicenseFile extends SimpleConfig {

        public LicenseFile() {
            super("license.yml", false);
        }

        private final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY----- MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtrrK4qidlBgCjL4uVtsP Bu6+ajtPvpkFor+tF0Z6Aw1VthPgPm7iWXb6z034I+CSRf4OoEXXMuhEqz1JBo+D BhoOe12sbPBSSh82aZYVJSE/gCdoLpC65FdEOqw0Jal3FgGnFyeWuQjHwzP7XG51 +r9mXSFg/jnN4UaZhFN/5ArZnvsPexWFjfIL0JrtGYLNorNiY7ZvYYqpqqoEYibZ sv4shagqNRWTZMH2BXcu7ZKiqM5+FG/IAduQReT2qYuIXDgMv/YWeTB4HBf9yuW6 f8Lrve7Nz69SNAKU3rsulIztDcKoKR/CHSL9MOI+5p1sBa7JiAs74CXT27CgK+pM VwIDAQAB -----END PUBLIC KEY-----";

        public void e(){

            config.options().copyDefaults(true);
            config.addDefault("license", "license here");

            save();

            final String license = config.getString("license");

            /*
            LicenseGate gate = new LicenseGate("a1c91", PUBLIC_KEY);
            LicenseGate.ValidationType result = gate.verify(license, "SCOPE");

            Skyblock.info(" ");
            Skyblock.info("Validating your license...");
            Skyblock.info(" ");

            switch (result) {
                case VALID:
                    Skyblock.info("License is valid! Loading plugin...");
                    break;
                case EXPIRED:
                    Skyblock.info("License has expired! Disabling plugin...");
                    Bukkit.getPluginManager().disablePlugin(Skyblock.get());
                    break;
                default:
                    Skyblock.info("License not found! Disabling plugin!");
                    Bukkit.getPluginManager().disablePlugin(Skyblock.get());
                    break;
            }
             */
        }
    }
}
