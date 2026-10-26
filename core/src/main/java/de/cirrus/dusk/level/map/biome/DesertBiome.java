package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

public class DesertBiome extends Biome {
    public static final DesertBiome INSTANCE = new DesertBiome();

    public DesertBiome() {
        super(BiomeType.DESERT, "Desert", Tile.sand);
    }
}
