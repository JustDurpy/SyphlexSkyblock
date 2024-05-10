package net.syphlex.skyblock.manager.gui.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.syphlex.skyblock.manager.profile.Profile;

@Getter
@AllArgsConstructor
public class ClickEvent {
    private final Profile profile;
    private final int slot;

}
