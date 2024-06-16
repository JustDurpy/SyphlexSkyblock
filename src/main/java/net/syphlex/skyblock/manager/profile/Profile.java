package net.syphlex.skyblock.manager.profile;

import lombok.Getter;
import lombok.Setter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.cmd.IslandCmd;
import net.syphlex.skyblock.manager.gui.impl.island.manage.IslandPanelGui;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.island.member.IslandRole;
import net.syphlex.skyblock.manager.island.member.MemberProfile;
import net.syphlex.skyblock.manager.island.request.InviteRequest;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.config.Messages;
import net.syphlex.skyblock.util.utilities.StringUtil;
import net.syphlex.skyblock.util.board.FastBoard;
import net.syphlex.skyblock.util.config.ConfigEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
@Setter
public class Profile {
    private final Player player;
    private FastBoard scoreboard;

    private Island island = null;

    private boolean respawnAtIsland = false, adminMode = false;

    private final ArrayList<InviteRequest> inviteRequests = new ArrayList<>();

    public Profile(final Player player){
        this.player = player;
        //this.memberProfile = new MemberProfile(this.player.getUniqueId());

        this.scoreboard = new FastBoard(player);
        this.scoreboard.updateTitle(StringUtil.CC(ConfigEnum.SCOREBOARD_TITLE.getAsString()));
    }

    public void openIslandPanel(){

        if (!hasIsland()) {
            Messages.DOES_NOT_HAVE_ISLAND.send(this);
            return;
        }

        if (!ConfigMenu.ISLAND_PANEL_MENU.getMenuSetting().isEnabled()) {
            this.player.closeInventory();
            IslandCmd.handleHelp(this.player);
            return;
        }

        Skyblock.get().getGuiHandler().openGui(this, new IslandPanelGui());
    }

    public void joinIsland(Island island) {

        if (hasIsland())
            return;

        //this.memberProfile.setRole(IslandRole.MEMBER);

        this.island = island;
        //this.island.getMembers().add(this.memberProfile);
        this.island.teleport(this.player);

        this.inviteRequests.clear();

        sendMessage(Messages.JOIN_ISLAND.get()
                .replace("%player%", island.getLeader().getUsername()));
        island.broadcast(Messages.JOIN_ISLAND_BROADCAST.get()
                .replace("%player%", this.player.getName()));
    }

    public void leaveIsland(){

        if (!hasIsland())
            return;

        MemberProfile profile = island.getMember(this.player.getUniqueId());

        if (profile == null)
            return;

        Messages.LEFT_ISLAND.send(this.player);
        this.island.broadcast(Messages.LEFT_ISLAND_BROADCAST.get()
                .replace("%player%", this.player.getName()));

        this.island.getMembers().remove(profile);
        this.island = null;

        //this.memberProfile.setRole(IslandRole.VISITOR);

        this.player.teleport(Skyblock.get().getMainSpawn());
    }

    public void sendMessage(String msg){
        this.player.sendMessage(StringUtil.HexCC(msg));
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

    /*
    improvised this.
    used to use player-specific data caching
    would eventually at times be not in sync with island
    member/permission management.
     */
    public MemberProfile getMemberProfile(){

        if (!hasIsland()) return new MemberProfile(
                this.player.getUniqueId(),
                IslandRole.VISITOR);

        if (isIslandLeader()) return new MemberProfile(
                this.player.getUniqueId(),
                IslandRole.LEADER);

        return this.island.getMember(this.player.getUniqueId());
    }

}
