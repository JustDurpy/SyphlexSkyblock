package net.syphlex.skyblock.manager.island.request;

import lombok.Getter;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.profile.Profile;

@Getter
public class InviteRequest {

    private final Island island;
    private final Profile invited;
    public final int INVITATION_TIME = 15;
    private final long timeOfRequest;

    public InviteRequest(Island island, Profile invited){
        this.island = island;
        this.invited = invited;
        this.timeOfRequest = System.currentTimeMillis();
    }
}
