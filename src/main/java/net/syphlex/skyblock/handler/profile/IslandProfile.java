package net.syphlex.skyblock.handler.profile;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.handler.island.data.Island;
import net.syphlex.skyblock.handler.island.member.IslandRole;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.handler.island.request.InviteRequest;
import net.syphlex.skyblock.util.StringUtil;
import net.syphlex.skyblock.util.board.FastBoard;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
@Setter
public class IslandProfile {
    private final Player player;
    private final MemberProfile memberProfile;
    private FastBoard scoreboard;

    private Island island = null;

    private final ArrayList<InviteRequest> inviteRequests = new ArrayList<>();

    public IslandProfile(final Player player){
        this.player = player;
        this.memberProfile = new MemberProfile(this.player.getUniqueId());

        this.scoreboard = new FastBoard(player);
        this.scoreboard.updateTitle(StringUtil.CC(ConfigEnum.SCOREBOARD_TITLE.getAsString()));
    }

    public void joinIsland(Island island){
        if (hasIsland())
            return;
        this.island = island;
        this.island.getMembers().add(new MemberProfile(
                this.player.getUniqueId(),
                IslandRole.DEFAULT));
        this.island.teleport(this.player);
    }

    public void leaveIsland(){
        if (!hasIsland())
            return;

        MemberProfile profile = island.getMember(this.player.getUniqueId());

        if (profile == null)
            return;

        this.island.getMembers().remove(profile);
        this.island = null;

        this.player.teleport(Skyblock.get().getMainSpawn());
    }

    public InviteRequest getIslandInvite(Island island){
        for (InviteRequest request : this.inviteRequests) {
            if (request.getIsland().getIdentifier().equalsIgnoreCase(island.getIdentifier()))
                return request;
        }
        return null;
    }

    public boolean hasIslandInviteRequest(Island island){
        return getIslandInvite(island) != null;
    }

    public boolean isIslandLeader(){
        if (!hasIsland())
            return false;
        return this.island.getOwner().getUuid().equals(this.player.getUniqueId());
    }

    public boolean hasIsland(){
        return island != null;
    }

}
