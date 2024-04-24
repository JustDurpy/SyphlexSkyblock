package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.Island;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.island.member.IslandRole;
import net.syphlex.skyblock.util.Position;
import net.syphlex.skyblock.util.SimpleConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandFile extends SimpleConfig {


    public IslandFile() {
        super("/islands/", true);
    }

    public ArrayList<Island> read(){

        ArrayList<Island> islands = new ArrayList<>();

        if (getFile().listFiles().length <= 0)
            return islands;

        for (File f : getFile().listFiles()) {

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);

            String islandIdentifier = config.getString("island.identifier");
            MemberProfile owner = new MemberProfile(
                    UUID.fromString(config.getString("island.owner")),
                    IslandRole.LEADER);
            Position corner1 = new Position(config.getString("island.corner1"));
            Position corner2 = new Position(config.getString("island.corner2"));
            Position home = new Position(config.getString("island.home"));
            Position center = new Position(config.getString("island.center"));

            int generatorTier = config.getInt("island.upgrades.generator-tier");
            double spawnRate = config.getDouble("island.upgrades.spawn-rate");
            double harvestRate = config.getDouble("island.upgrades.harvest-rate");
            double size = config.getDouble("island.upgrades.size");

            ArrayList<MemberProfile> members = new ArrayList<>();

            if (config.getStringList("island.members").size() > 0) {
                for (String uuid : config.getStringList("island.members")) {
                    members.add(new MemberProfile(
                            UUID.fromString(uuid),
                            IslandRole.get(config.getString("island.members."
                                    + UUID.fromString(uuid) + ".role"))
                    ));
                }
            }

            Island island = new Island(islandIdentifier, owner, corner1, corner2, center, members);
            island.getUpgrades().setGenerator(Skyblock.get().getUpgradeHandler().getOreGenerator(generatorTier));
            island.getUpgrades().setSpawnRate(spawnRate);
            island.getUpgrades().setHarvestRate(harvestRate);
            island.getUpgrades().setSize(size);

            islands.add(island);
        }

        return islands;
    }

    public void write(Island island) {

        if (island == null)
            return;

        if (!getFile().exists())
            getFile().mkdirs();

        try {
            File f = new File(getFile().getPath() + "/"
                    + island.getIdentifier() + ".yml");

            f.createNewFile();

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("island.identifier", island.getIdentifier());
            config.set("island.owner", island.getOwner().getUuid().toString());
            config.set("island.corner1", island.getCorner1().getAsString());
            config.set("island.corner2", island.getCorner2().getAsString());
            config.set("island.center", island.getCenter().getAsString());
            config.set("island.home", island.getHome().getAsString());

            config.set("island.upgrades.generator-tier", island.getUpgrades().getGenerator().getTier());
            config.set("island.upgrades.spawn-rate", island.getUpgrades().getSpawnRate());
            config.set("island.upgrades.harvest-rate", island.getUpgrades().getHarvestRate());
            config.set("island.upgrades.size", island.getUpgrades().getSize());

            List<String> uuids = new ArrayList<>();
            if (island.getMembers().size() > 0) {
                for (MemberProfile profiles : island.getMembers()) {
                    uuids.add(profiles.getUuid().toString());
                    config.set("island.members." + profiles.getUuid().toString() + ".role",
                            profiles.getRole().getIdentifier());
                }
            }
            config.set("island.members", uuids);

            config.save(f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
