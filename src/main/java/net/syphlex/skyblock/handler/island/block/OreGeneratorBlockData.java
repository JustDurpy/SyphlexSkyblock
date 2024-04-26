package net.syphlex.skyblock.handler.island.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class OreGeneratorBlockData {
    private final Material material;
    private final double chance;
}
