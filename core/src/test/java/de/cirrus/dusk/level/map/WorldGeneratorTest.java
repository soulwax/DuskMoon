package de.cirrus.dusk.level.map;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.biome.Biome;
import de.cirrus.dusk.level.map.biome.BiomeRegistry;
import de.cirrus.dusk.level.map.biome.BiomeType;
import de.cirrus.dusk.level.map.feature.CactusFeature;
import de.cirrus.dusk.level.map.feature.DungeonStairsFeature;
import de.cirrus.dusk.level.map.feature.FlowerFeature;
import de.cirrus.dusk.level.map.feature.SandPatchFeature;
import de.cirrus.dusk.level.map.feature.TreeFeature;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldGeneratorTest {

    private WorldGenerator generator;
    private Random random;

    @BeforeEach
    public void setUp() {
        random = new Random(42L);
        generator = new WorldGenerator(random);
    }

    @Test
    @DisplayName("WorldGenerator populates LevelMap and respects dimensions")
    public void testBasicGeneration() {
        int w = 128;
        int h = 128;
        LevelMap map = generator.generate(w, h);

        assertNotNull(map);
        assertEquals(w, map.w);
        assertEquals(h, map.h);
        assertEquals(w * h, map.tiles.length);
        assertEquals(w * h, map.data.length);
    }

    @Test
    @DisplayName("TerrainContext generates diverse biomes based on climate and elevation")
    public void testTerrainContextAndBiomes() {
        int w = 128;
        int h = 128;
        TerrainContext context = generator.generateTerrainContext(w, h, random);

        assertNotNull(context);
        assertEquals(w * h, context.elevation.length);
        assertEquals(w * h, context.moisture.length);
        assertEquals(w * h, context.temperature.length);
        assertEquals(w * h, context.biomes.length);

        int oceanCount = 0;
        int landCount = 0;

        for (int i = 0; i < w * h; i++) {
            Biome biome = context.biomes[i];
            assertNotNull(biome);
            if (biome.getType() == BiomeType.OCEAN) {
                oceanCount++;
            } else {
                landCount++;
            }
        }

        // Island shape should have significant ocean perimeter and central landmass
        assertTrue(oceanCount > 1000, "Should have sufficient ocean surrounding island");
        assertTrue(landCount > 1000, "Should have sufficient landmass for island");
    }

    @Test
    @DisplayName("BiomeRegistry correctly maps elevation, moisture, and temperature")
    public void testBiomeRegistry() {
        assertEquals(BiomeType.OCEAN, BiomeRegistry.getBiome(-0.8, 0.0, 0.0).getType());
        assertEquals(BiomeType.MOUNTAIN, BiomeRegistry.getBiome(0.8, -1.8, 0.0).getType());
        assertEquals(BiomeType.FOREST, BiomeRegistry.getBiome(0.0, 0.6, 0.0).getType());
        assertEquals(BiomeType.DESERT, BiomeRegistry.getBiome(0.0, -1.0, 0.5).getType());
        assertEquals(BiomeType.PLAINS, BiomeRegistry.getBiome(0.0, 0.0, 0.0).getType());
    }

    @Test
    @DisplayName("World features place expected tiles onto terrain")
    public void testFeaturePlacement() {
        int w = 64;
        int h = 64;
        LevelMap map = new LevelMap(w, h);
        TerrainContext context = new TerrainContext(w, h);

        // Fill map with grass
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                map.setTile(x, y, Tile.grass);
            }
        }

        // Test FlowerFeature
        FlowerFeature flowerFeature = new FlowerFeature();
        flowerFeature.place(map, context, random);

        int flowerCount = 0;
        for (int i = 0; i < w * h; i++) {
            if (map.tiles[i] == Tile.flower.id) {
                flowerCount++;
            }
        }
        assertTrue(flowerCount > 0, "FlowerFeature should place flowers on grass");

        // Test SandPatchFeature
        SandPatchFeature sandFeature = new SandPatchFeature();
        sandFeature.place(map, context, random);

        int sandCount = 0;
        for (int i = 0; i < w * h; i++) {
            if (map.tiles[i] == Tile.sand.id) {
                sandCount++;
            }
        }
        assertTrue(sandCount > 0, "SandPatchFeature should place sand patches");

        // Test CactusFeature
        CactusFeature cactusFeature = new CactusFeature();
        cactusFeature.place(map, context, random);

        int cactusCount = 0;
        for (int i = 0; i < w * h; i++) {
            if (map.tiles[i] == Tile.cactus.id) {
                cactusCount++;
            }
        }
        assertTrue(cactusCount > 0, "CactusFeature should place cacti on sand");

        // Test DungeonStairsFeature
        // Create solid rock area
        for (int ry = 10; ry <= 30; ry++) {
            for (int rx = 10; rx <= 30; rx++) {
                map.setTile(rx, ry, Tile.rock);
            }
        }

        DungeonStairsFeature stairsFeature = new DungeonStairsFeature(1);
        stairsFeature.place(map, context, random);

        int stairsCount = 0;
        for (int i = 0; i < w * h; i++) {
            if (map.tiles[i] == Tile.stairsDown.id) {
                stairsCount++;
            }
        }
        assertTrue(stairsCount >= 1, "DungeonStairsFeature should place stairs within solid rock");
    }

    @Test
    @DisplayName("LevelGen validation satisfies all threshold constraints")
    public void testLevelGenValidation() {
        int w = 128;
        int h = 128;
        byte[][] result = LevelGen.createAndValidateTopMap(w, h);

        assertNotNull(result);
        assertEquals(2, result.length);

        byte[] map = result[0];
        int[] count = new int[256];
        for (byte b : map) {
            count[b & 0xff]++;
        }

        assertTrue(count[Tile.rock.id & 0xff] >= 100, "Rock count constraint");
        assertTrue(count[Tile.sand.id & 0xff] >= 100, "Sand count constraint");
        assertTrue(count[Tile.grass.id & 0xff] >= 100, "Grass count constraint");
        assertTrue(count[Tile.tree.id & 0xff] >= 100, "Tree count constraint");
        assertTrue(count[Tile.stairsDown.id & 0xff] >= 2, "Stairs count constraint");
        assertTrue(count[Tile.flower.id & 0xff] >= 10, "Flower count constraint");
        assertTrue(count[Tile.cactus.id & 0xff] >= 10, "Cactus count constraint");
    }
}
