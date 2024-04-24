package net.syphlex.skyblock.handler.mine.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@AllArgsConstructor
public class MineBlockData {
    private final Material material;
    private double chance;
}
