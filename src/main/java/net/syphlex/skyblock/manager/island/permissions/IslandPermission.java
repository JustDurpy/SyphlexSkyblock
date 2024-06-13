package net.syphlex.skyblock.manager.island.permissions;

import lombok.Getter;
import net.syphlex.skyblock.manager.island.member.IslandRole;

@Getter
public enum IslandPermission {
    PICKUP_AND_DROP_ITEMS("Pick Up/Drop Items", "Allows the %island_role% role to pick up and drop items."),
    BLOCK_PLACE("Place Blocks", "Allows the %island_role% role to place blocks."),
    BLOCK_BREAK("Break Blocks", "Allows the %island_role% role to break blocks."),
    ACCESS_CONTAINERS("Access Containers", "Allows the %island_role% role to access containers."),
    INTERACT("Interact", "Allows the %island_role% role to interact with the island."),
    DAMAGE_ENTITIES("Damage Entities", "Allows the %island_role% role to damage entities."),
    ACCESS_VOID_CHEST("Access Void Chest", "Allows the %island_role% role to access the island's void chest."),
    INVITE_MEMBER("Invite Players", "Allows the %island_role% to invite members."),
    KICK_MEMBER("Kick Members", "Allows the %island_role% to kick members."),
    KICK_VISITOR("Kick Visitors", "Allows the %island_role% role to kick visitors off the island."),
    BAN_VISITOR("Ban Visitors", "Allows the %island_role% role to ban players from visiting the island."),
    UNBAN_VISITOR("Unban Visitors", "Allows the %island_role% role to unban players from visiting the island.");


    private final String name, description;

    IslandPermission(String name, String description){
        this.name = name;
        this.description = description;
    }
}
