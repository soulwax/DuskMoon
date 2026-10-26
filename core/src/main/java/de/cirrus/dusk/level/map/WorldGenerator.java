package de.cirrus.dusk.level.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.biome.Biome;
import de.cirrus.dusk.level.map.biome.BiomeRegistry;
import de.cirrus.dusk.level.map.feature.CactusFeature;
import de.cirrus.dusk.level.map.feature.DungeonStairsFeature;
import de.cirrus.dusk.level.map.feature.FlowerFeature;
import de.cirrus.dusk.level.map.feature.SandPatchFeature;
import de.cirrus.dusk.level.map.feature.TreeFeature;
import de.cirrus.dusk.level.map.feature.WorldFeature;
import de.cirrus.dusk.level.map.noise.NoiseMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Modular world generator responsible for procedural terrain synthesis,
 * biome distribution, and world feature decoration.
 */
public class WorldGenerator {

    private final List<WorldFeature> features = new ArrayList<>();
    private final Random random;

    public WorldGenerator() {
        this(new Random());
    }

    public WorldGenerator(Random random) {
        this.random = random != null ? random : new Random();
        registerDefaultFeatures();
    }

    /**
     * Registers default world features in decoration order.
     */
    protected void registerDefaultFeatures() {
        features.add(new SandPatchFeature());
        features.add(new TreeFeature());
        features.add(new FlowerFeature());
        features.add(new CactusFeature());
        features.add(new DungeonStairsFeature(4));
    }

    public void addFeature(WorldFeature feature) {
        features.add(feature);
    }

    public List<WorldFeature> getFeatures() {
        return Collections.unmodifiableList(features);
    }

    /**
     * Generates a fully populated LevelMap with terrain, biomes, and features.
     *
     * @param w width in tiles
     * @param h height in tiles
     * @return the generated LevelMap
     */
    public LevelMap generate(int w, int h) {
        return generate(w, h, this.random);
    }

    /**
     * Generates a fully populated LevelMap with the specified random generator.
     */
    public LevelMap generate(int w, int h, Random rng) {
        LevelMap levelMap = new LevelMap(w, h);
        TerrainContext context = generateTerrainContext(w, h, rng);

        // Populate base terrain tiles from biome surface definitions
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int idx = context.getIndex(x, y);
                Biome biome = context.biomes[idx];
                Tile tile = (biome != null) ? biome.getSurfaceTile() : Tile.grass;
                levelMap.setTile(x, y, tile);
            }
        }

        // Apply procedural features
        for (WorldFeature feature : features) {
            feature.place(levelMap, context, rng);
        }

        return levelMap;
    }

    /**
     * Synthesizes elevation, moisture, and temperature noise to construct the TerrainContext.
     */
    public TerrainContext generateTerrainContext(int w, int h, Random rng) {
        TerrainContext context = new TerrainContext(w, h);

        NoiseMap mnoise1 = new NoiseMap(w, h, 16, rng);
        NoiseMap mnoise2 = new NoiseMap(w, h, 16, rng);
        NoiseMap mnoise3 = new NoiseMap(w, h, 16, rng);

        NoiseMap noise1 = new NoiseMap(w, h, 32, rng);
        NoiseMap noise2 = new NoiseMap(w, h, 32, rng);

        NoiseMap tempNoise = new NoiseMap(w, h, 32, rng);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = context.getIndex(x, y);

                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;
                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

                double tval = tempNoise.values[i];

                // Island radial distance attenuation
                double xd = x / (w - 1.0) * 2 - 1;
                double yd = y / (h - 1.0) * 2 - 1;
                if (xd < 0) xd = -xd;
                if (yd < 0) yd = -yd;
                double dist = Math.max(xd, yd);
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1 - dist * 20;

                context.elevation[i] = val;
                context.moisture[i] = mval;
                context.temperature[i] = tval;
                context.biomes[i] = BiomeRegistry.getBiome(val, mval, tval);
            }
        }

        return context;
    }

    /**
     * Backward-compatible generation returning [map, data] byte arrays.
     */
    public byte[][] generateRaw(int w, int h) {
        LevelMap map = generate(w, h, this.random);
        return new byte[][]{map.tiles, map.data};
    }
}
