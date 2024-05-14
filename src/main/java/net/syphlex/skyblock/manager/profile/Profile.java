package net.syphlex.skyblock.manager.profile;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.request.InviteRequest;
import net.syphlex.skyblock.manager.minion.Minion;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.board.FastBoard;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
@Setter
public class Profile {
    private final Player player;
    private final MemberProfile memberProfile;
    private FastBoard scoreboard;

    private Island island = null;
    private Minion attachingChest = null;

    private int mobCoins;
    private boolean respawnAtIsland = false;

    private final ArrayList<InviteRequest> inviteRequests = new ArrayList<>();

    public Profile(final Player player){
        this.player = player;
        this.memberProfile = new MemberProfile(this.player.getUniqueId());

        this.scoreboard = new FastBoard(player);
        this.scoreboard.updateTitle(StringUtil.CC(ConfigEnum.SCOREBOARD_TITLE.getAsString()));
    }

    public void joinIsland(Island island){

        if (hasIsland())
            return;

        this.memberProfile.setRole(IslandRole.MEMBER);

        this.island = island;
        this.island.getMembers().add(this.memberProfile);
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

        this.memberProfile.setRole(IslandRole.VISITOR);

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
        return this.island.getLeader().getUuid().equals(this.player.getUniqueId());
    }

    public boolean hasIsland(){
        return island != null;
    }

}
