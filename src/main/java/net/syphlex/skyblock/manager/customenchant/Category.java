package net.syphlex.skyblock.manager.customenchant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {
    private final String name;
    private final String color;
    private final double chance;
}