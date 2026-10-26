package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

public class MountainBiome extends Biome {
    public static final MountainBiome INSTANCE = new MountainBiome();

    public MountainBiome() {
        super(BiomeType.MOUNTAIN, "Mountain", Tile.rock);
    }
}
