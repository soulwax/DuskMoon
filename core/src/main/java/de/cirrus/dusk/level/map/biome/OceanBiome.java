package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

public class OceanBiome extends Biome {
    public static final OceanBiome INSTANCE = new OceanBiome();

    public OceanBiome() {
        super(BiomeType.OCEAN, "Ocean", Tile.water);
    }
}
