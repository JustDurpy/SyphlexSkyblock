package net.syphlex.skyblock.manager.island.request;

import lombok.Getter;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.profile.IslandProfile;

@Getter
public class InviteRequest {

    private final Island island;
    private final IslandProfile invited;
    public final int INVITATION_TIME = 15;
    private final long timeOfRequest;

    public InviteRequest(Island island, IslandProfile invited){
        this.island = island;
        this.invited = invited;
        this.timeOfRequest = System.currentTimeMillis();
    }
}
