package net.syphlex.skyblock.manager.cooldown.impl;

import net.syphlex.skyblock.manager.cooldown.type.Cooldown;
import net.syphlex.skyblock.util.config.ConfigEnum;

public class IslandCreateCooldown extends Cooldown {
    public IslandCreateCooldown() {
        super(ConfigEnum.ISLAND_CREATE_COOLDOWN.getAsInteger());
    }
}
