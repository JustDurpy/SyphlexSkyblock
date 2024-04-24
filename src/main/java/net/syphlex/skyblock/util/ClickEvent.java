package net.syphlex.skyblock.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.syphlex.skyblock.profile.IslandProfile;

@Getter
@AllArgsConstructor
public class ClickEvent {
    private final IslandProfile profile;
    private final int slot;

}
