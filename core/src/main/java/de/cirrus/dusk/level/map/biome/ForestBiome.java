package de.cirrus.dusk.level.map.biome;

import de.cirrus.dusk.level.tile.Tile;

public class ForestBiome extends Biome {
    public static final ForestBiome INSTANCE = new ForestBiome();

    public ForestBiome() {
        super(BiomeType.FOREST, "Forest", Tile.grass);
    }
}
