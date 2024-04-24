package net.syphlex.skyblock.handler.island.member;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MemberProfile {

    private final UUID uuid;
    private IslandRole role;

    public MemberProfile(UUID uuid){
        this.uuid = uuid;
        this.role = IslandRole.DEFAULT;
    }

    public MemberProfile(UUID uuid, IslandRole rank){
        this.uuid = uuid;
        this.role = rank;
    }
}
