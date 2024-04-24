package net.syphlex.skyblock.handler.island.upgrade.oregenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class OreGeneratorBlock {
    private final Material material;
    private final double chance;
}
