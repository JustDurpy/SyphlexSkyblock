package net.syphlex.skyblock.manager.island.member;

import lombok.Getter;

@Getter
public enum IslandRole {
    DEFAULT("default"),
    MODERATOR("moderator"),
    LEADER("leader");

    private final String identifier;

    IslandRole(String identifier){
        this.identifier = identifier;
    }

    public static IslandRole get(String input){
        for (IslandRole r : values()) {
            if (r.name().equalsIgnoreCase(input)
                    || r.getIdentifier().equalsIgnoreCase(input))
                return r;
        }
        return DEFAULT;
    }
}
