package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

public class PlainsBiome extends Biome {
    public static final PlainsBiome INSTANCE = new PlainsBiome();

    public PlainsBiome() {
        super(BiomeType.PLAINS, "Plains", Tile.grass);
    }
}
