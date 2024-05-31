package net.syphlex.skyblock.manager.island.settings.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class IslandSetting {
    private final String identifier;
    @Setter private boolean enabled = false;

    public boolean get(){
        return this.enabled;
    }
}
