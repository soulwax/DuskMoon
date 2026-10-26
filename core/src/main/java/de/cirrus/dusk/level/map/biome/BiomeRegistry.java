package de.cirrus.dusk.level.map.biome;

/**
 * Classifies geographic coordinates into biomes based on climate and elevation models.
 */
public class BiomeRegistry {

    public static Biome getBiome(double elevation, double moisture, double temperature) {
        if (elevation < -0.5) {
            return OceanBiome.INSTANCE;
        }
        if (elevation > 0.5 && moisture < -1.5) {
            return MountainBiome.INSTANCE;
        }
        if (moisture > 0.4) {
            return ForestBiome.INSTANCE;
        }
        if (moisture < -0.8 && temperature > 0.1) {
            return DesertBiome.INSTANCE;
        }
        return PlainsBiome.INSTANCE;
    }
}
