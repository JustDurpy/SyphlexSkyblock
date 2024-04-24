package net.syphlex.skyblock.profile;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.handler.island.data.Island;
import net.syphlex.skyblock.handler.island.member.MemberProfile;
import net.syphlex.skyblock.util.StringUtil;
import net.syphlex.skyblock.util.board.FastBoard;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.entity.Player;

@Getter
@Setter
public class IslandProfile {
    private final Player player;
    private final MemberProfile memberProfile;
    private FastBoard scoreboard;

    private Island island = null;

    public IslandProfile(final Player player){
        this.player = player;
        this.memberProfile = new MemberProfile(this.player.getUniqueId());

        this.scoreboard = new FastBoard(player);
        this.scoreboard.updateTitle(StringUtil.CC(ConfigEnum.SCOREBOARD_TITLE.getAsString()));
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
