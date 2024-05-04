package net.syphlex.skyblock.manager.mobcoin;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;

@Getter
@Setter
public class MobCoinEntity {
    private final EntityType entityType;
    private final double chance;

    public MobCoinEntity(EntityType entityType, double chance){
        this.entityType = entityType;
        this.chance = chance;
    }
}
