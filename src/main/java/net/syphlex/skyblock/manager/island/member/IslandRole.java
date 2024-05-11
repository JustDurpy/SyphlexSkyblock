package net.syphlex.skyblock.manager.island.member;

import lombok.Getter;
import net.syphlex.skyblock.manager.island.permissions.IslandPermission;
import net.syphlex.skyblock.util.data.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum IslandRole {
    VISITOR("Visitor", Arrays.asList(
            new Pair<>(IslandPermission.PICKUP_AND_DROP_ITEMS, false),
            new Pair<>(IslandPermission.BLOCK_PLACE, false),
            new Pair<>(IslandPermission.BLOCK_BREAK, false),
            new Pair<>(IslandPermission.ACCESS_CONTAINERS, false),
            new Pair<>(IslandPermission.INTERACT, false),
            new Pair<>(IslandPermission.DAMAGE_ENTITIES, false),
            new Pair<>(IslandPermission.INVITE_MEMBER, false),
            new Pair<>(IslandPermission.KICK_MEMBER, false),
            new Pair<>(IslandPermission.KICK_VISITOR, false),
            new Pair<>(IslandPermission.BAN_VISITOR, false))),
    MEMBER("Member", Arrays.asList(
            new Pair<>(IslandPermission.PICKUP_AND_DROP_ITEMS, true),
            new Pair<>(IslandPermission.BLOCK_PLACE, true),
            new Pair<>(IslandPermission.BLOCK_BREAK, true),
            new Pair<>(IslandPermission.ACCESS_CONTAINERS, true),
            new Pair<>(IslandPermission.INTERACT, true),
            new Pair<>(IslandPermission.DAMAGE_ENTITIES, true),
            new Pair<>(IslandPermission.INVITE_MEMBER, false),
            new Pair<>(IslandPermission.KICK_MEMBER, false),
            new Pair<>(IslandPermission.KICK_VISITOR, false),
            new Pair<>(IslandPermission.BAN_VISITOR, false))),
    MODERATOR("Moderator", Arrays.asList(
            new Pair<>(IslandPermission.PICKUP_AND_DROP_ITEMS, true),
            new Pair<>(IslandPermission.BLOCK_PLACE, true),
            new Pair<>(IslandPermission.BLOCK_BREAK, true),
            new Pair<>(IslandPermission.ACCESS_CONTAINERS, true),
            new Pair<>(IslandPermission.INTERACT, true),
            new Pair<>(IslandPermission.DAMAGE_ENTITIES, true),
            new Pair<>(IslandPermission.INVITE_MEMBER, true),
            new Pair<>(IslandPermission.KICK_MEMBER, true),
            new Pair<>(IslandPermission.KICK_VISITOR, true),
            new Pair<>(IslandPermission.BAN_VISITOR, true))),
    LEADER("Leader", Arrays.asList(
            new Pair<>(IslandPermission.PICKUP_AND_DROP_ITEMS, true),
            new Pair<>(IslandPermission.BLOCK_PLACE, true),
            new Pair<>(IslandPermission.BLOCK_BREAK, true),
            new Pair<>(IslandPermission.ACCESS_CONTAINERS, true),
            new Pair<>(IslandPermission.INTERACT, true),
            new Pair<>(IslandPermission.DAMAGE_ENTITIES, true),
            new Pair<>(IslandPermission.INVITE_MEMBER, true),
            new Pair<>(IslandPermission.KICK_MEMBER, true),
            new Pair<>(IslandPermission.KICK_VISITOR, true),
            new Pair<>(IslandPermission.BAN_VISITOR, true)));

    private final String identifier;
    private final ArrayList<Pair<IslandPermission, Boolean>> permissions = new ArrayList<>();

    IslandRole(String identifier, List<Pair<IslandPermission, Boolean>> permissions){
        this.identifier = identifier;
        this.permissions.addAll(permissions);
    }

    public boolean hasPermission(IslandPermission permission){
        for (Pair<IslandPermission, Boolean> pair : this.permissions)
            if (pair.getX() == permission)
                return pair.getY();
        return false;
    }

    public int getPermissionIndex(IslandPermission permission){
        int i = 0;
        for (Pair<IslandPermission, Boolean> pair : this.permissions) {
            if (pair.getX() == permission)
                return i;
            i++;
        }
        return -1;
    }

    public static IslandRole get(String input){
        for (IslandRole r : values()) {
            if (r.name().equalsIgnoreCase(input)
                    || r.getIdentifier().equalsIgnoreCase(input))
                return r;
        }
        return VISITOR;
    }
}
