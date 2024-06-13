package net.syphlex.skyblock.database.flat;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.block.SpecialBlockData;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.block.IslandBlockData;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.permissions.IslandPermission;
import net.syphlex.skyblock.manager.island.settings.impl.IslandBorderColor;
import net.syphlex.skyblock.manager.island.settings.impl.IslandTimeLock;
import net.syphlex.skyblock.manager.island.settings.impl.IslandWeatherLock;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.data.Position;
import net.syphlex.skyblock.util.simple.SimpleConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IslandFile extends SimpleConfig {


    public IslandFile() {
        super("/islands/", true);
    }

    public ArrayList<Island> read(){

        ArrayList<Island> islands = new ArrayList<>();

        for (File f : Objects.requireNonNull(getFile().listFiles())) {

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);

            if (config.getConfigurationSection("island") == null || config.get("island") == null) {
                f.delete();
                continue;
            }

            String islandIdentifier = config.getString("island.identifier");
            MemberProfile leader = new MemberProfile(
                    UUID.fromString(config.getString("island.leader")),
                    IslandRole.LEADER);
            Position corner1 = new Position(config.getString("island.corner1"));
            Position corner2 = new Position(config.getString("island.corner2"));
            Position home = new Position(config.getString("island.home"));
            Position center = new Position(config.getString("island.center"));

            int islandSize = config.getInt("island.upgrades.island-size");
            int spawnRate = config.getInt("island.upgrades.spawn-rate");
            int spawnAmount = config.getInt("island.upgrades.spawn-amount");
            int harvest = config.getInt("island.upgrades.harvest");
            int teamSize = config.getInt("island.upgrades.team-size");
            int generator = config.getInt("island.upgrades.generator");

            boolean allowVisitors = config.getBoolean("island.settings.allow-visitors");
            IslandBorderColor borderColor = IslandBorderColor.of(config.getString("island.settings.border-color"));
            IslandTimeLock timeLock = IslandTimeLock.of(config.getString("island.settings.time-lock"));
            IslandWeatherLock weatherLock = IslandWeatherLock.of(config.getString("island.settings.weather-lock"));

            ArrayList<MemberProfile> members = new ArrayList<>();
            ArrayList<IslandBlockData> storedBlocks = new ArrayList<>();
            ArrayList<String> bannedPlayers = new ArrayList<>();

            if (config.getStringList("island.members").size() > 0) {
                for (String uuid : config.getStringList("island.members")) {
                    String[] split = uuid.split(":");
                    members.add(new MemberProfile(UUID.fromString(split[0]),
                            IslandRole.get(split[1])));
                }
            }

            members.add(leader);

            if (config.getStringList("island.banned-players").size() > 0){
                bannedPlayers.addAll(config.getStringList("island.banned-players"));
            }

            for (String section : config.getStringList("island.stored-blocks")) {
                IslandBlockData blockData = getIslandBlockDataFromString(section);
                if (blockData.isNull()) continue;
                storedBlocks.add(blockData);
            }

            int[] id = IslandUtil.getId(islandIdentifier);

            Island island = new Island(id, islandIdentifier, leader, corner1, corner2, center, members, bannedPlayers, storedBlocks);
            island.setHome(home);

            island.getUpgrades().getIslandSize().setLevel(islandSize);
            island.getUpgrades().getSpawnRate().setLevel(spawnRate);
            island.getUpgrades().getSpawnAmount().setLevel(spawnAmount);
            island.getUpgrades().getHarvest().setLevel(harvest);
            island.getUpgrades().getTeamSize().setLevel(teamSize);
            island.getUpgrades().getGenerator().setLevel(generator);

            island.getSettings().getAllowVisitors().setEnabled(allowVisitors);
            island.getSettings().setIslandBorderColor(borderColor);
            island.getSettings().setTimeLock(timeLock);
            island.getSettings().setWeatherLock(weatherLock);

            for (IslandRole role : island.getRoles()) {
                for (IslandPermission permission : IslandPermission.values()) {

                    if (config.get("island.roles." + role.getIdentifier() + "." + permission.getName()) == null)
                        continue;

                    boolean value = config.getBoolean("island.roles."
                            + role.getIdentifier() + "." + permission.getName());

                    role.getPermissions().get(role.getPermissionIndex(permission)).setY(value);
                }
            }

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
            config.set("island.leader", island.getLeader().getUuid().toString());
            config.set("island.corner1", island.getCorner1().getAsString());
            config.set("island.corner2", island.getCorner2().getAsString());
            config.set("island.center", island.getCenter().getAsString());
            config.set("island.home", island.getHome().getAsString());

            config.set("island.upgrades.island-size", island.getUpgrades().getIslandSize().getLevel());
            config.set("island.upgrades.spawn-rate", island.getUpgrades().getSpawnRate().getLevel());
            config.set("island.upgrades.spawn-amount", island.getUpgrades().getSpawnAmount().getLevel());
            config.set("island.upgrades.harvest", island.getUpgrades().getHarvest().getLevel());
            config.set("island.upgrades.team-size", island.getUpgrades().getTeamSize().getLevel());
            config.set("island.upgrades.generator", island.getUpgrades().getGenerator().getLevel());

            config.set("island.settings.allow-visitors", island.getSettings().getAllowVisitors().get());
            config.set("island.settings.border-color", island.getSettings().getIslandBorderColor().name());
            config.set("island.settings.time-lock", island.getSettings().getTimeLock().name());
            config.set("island.settings.weather-lock", island.getSettings().getWeatherLock().name());

            List<String> uuids = new ArrayList<>();
            if (island.getMembers().size() > 0) {
                for (MemberProfile profiles : island.getMembers()) {
                    if (profiles.getRole() == IslandRole.LEADER) continue;
                    uuids.add(profiles.getUuid().toString() + ":" + profiles.getRole().getIdentifier());
                }
            }
            config.set("island.members", uuids);

            List<String> storedBlocks = new ArrayList<>();
            if (island.getStoredBlocks().size() > 0) {
                for (IslandBlockData data : island.getStoredBlocks()) {
                    if (data.isNull()) continue;
                    storedBlocks.add(data.getAsString());
                }
            }
            config.set("island.stored-blocks", storedBlocks);

            for (IslandRole role : island.getRoles()) {
                for (Pair<IslandPermission, Boolean> perm : role.getPermissions()) {
                    config.set("island.roles." + role.getIdentifier() + "." + perm.getX().getName(), perm.getY());
                }
            }

            config.save(f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IslandBlockData getIslandBlockDataFromString(String s){
        String[] split = s.split(":");

        Material material = Material.getMaterial(split[1]);

        SpecialBlockData blockData = null;

        for (SpecialBlockData blockDatas : Skyblock.get().getUpgradeHandler().getSpecialBlocks()) {
            if (blockDatas.getMaterial() == material) {
                blockData = blockDatas;
                break;
            }
        }

        return new IslandBlockData(
                new Position(split[0]),
                blockData,
                Integer.parseInt(split[2]));
    }
}
