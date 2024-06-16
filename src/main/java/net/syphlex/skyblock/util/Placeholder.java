package net.syphlex.skyblock.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {

    private final Skyblock plugin;

    public Placeholder(Skyblock plugin){
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "skyblock";
    }

    @Override
    public @NotNull String getAuthor() {
        return "iDurpy";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.1.0-BETA";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier){

        final Profile profile = this.plugin.getDataHandler().get(p);

        String placeholder = null;

        switch (identifier) {
            case "island_leader": {
                placeholder = (profile.hasIsland() ? profile.isIslandLeader() ? "You" : profile.getIsland().getLeader().getUsername() : "No Island");
                break;
            }
            case "island_team_size": {
                placeholder = String.valueOf(profile.hasIsland() ? profile.getIsland().getMembers().size() : "N/A");
                break;
            }
            case "island_max_team_size": {
                placeholder = String.valueOf(profile.hasIsland() ? profile.getIsland().getUpgrades().getTeamSize().getAsInt() : "N/A");
                break;
            }
            case "island_size": {
                placeholder = String.valueOf(profile.hasIsland() ? profile.getIsland().getUpgrades().getIslandSize().getAsInt() : "N/A");
                break;
            }
            case "island_worth": {
                placeholder = String.valueOf(profile.hasIsland() ? String.format("%,d", (int) profile.getIsland().getWorth()) : "N/A");
                break;
            }
            case "island_level": {
                placeholder = "0"; // todo
                break;
            }
        }

        return placeholder;
    }
}
