package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

/**
 * Represents an environmental biome governing surface tiles and characteristics.
 */
public abstract class Biome {
    private final BiomeType type;
    private final String name;
    private final Tile surfaceTile;

    public Biome(BiomeType type, String name, Tile surfaceTile) {
        this.type = type;
        this.name = name;
        this.surfaceTile = surfaceTile;
    }

    public BiomeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Tile getSurfaceTile() {
        return surfaceTile;
    }

    @Override
    public String toString() {
        return name;
    }
}
