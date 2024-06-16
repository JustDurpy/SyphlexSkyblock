package net.syphlex.skyblock.manager.island.member;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Setter
@Getter
public class MemberProfile {

    private final UUID uuid;
    private IslandRole role;

    public MemberProfile(UUID uuid){
        this.uuid = uuid;
        this.role = IslandRole.VISITOR;
    }

    public MemberProfile(UUID uuid, IslandRole rank){
        this.uuid = uuid;
        this.role = rank;
    }

    public String getUsername(){
        return Bukkit.getOfflinePlayer(this.uuid).getName();
    }

    /**
     * You can only promote 'member' roles
     * if you would like to make another player a leader
     * you can '/island setleader <player>'
     *
     * return true if the user successfully was promoted
     * return false if the user was not promoted
     * @return
     */
    public boolean promote(){
        if (this.role == IslandRole.MEMBER) {
            this.role = IslandRole.MODERATOR;
            return true;
        }
        return false;
    }

    /**
     * You can only demote 'moderator' roles
     *
     * return true if the demotion was a success
     * return false if the demotion was not a success
     */
    public boolean demote(){
        if (this.role == IslandRole.MODERATOR){
            this.role = IslandRole.MEMBER;
            return true;
        }
        return false;
    }

}
