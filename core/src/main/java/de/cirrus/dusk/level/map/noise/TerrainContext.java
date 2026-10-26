package de.cirrus.dusk.level.map.noise;

import de.cirrus.dusk.level.map.biome.Biome;

/**
 * Encapsulates the climate and elevation noise maps for a generated world.
 */
public class TerrainContext {
    public final int w;
    public final int h;
    public final double[] elevation;
    public final double[] moisture;
    public final double[] temperature;
    public final Biome[] biomes;

    public TerrainContext(int w, int h) {
        this.w = w;
        this.h = h;
        this.elevation = new double[w * h];
        this.moisture = new double[w * h];
        this.temperature = new double[w * h];
        this.biomes = new Biome[w * h];
    }

    public int getIndex(int x, int y) {
        return x + y * w;
    }
}
