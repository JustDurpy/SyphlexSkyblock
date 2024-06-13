package net.syphlex.skyblock.manager.cooldown.impl;

import net.syphlex.skyblock.manager.cooldown.type.Cooldown;
import net.syphlex.skyblock.util.config.ConfigEnum;

public class IslandDeleteCooldown extends Cooldown {
    public IslandDeleteCooldown() {
        super(ConfigEnum.ISLAND_DELETE_COOLDOWN.getAsInteger());
    }
}
