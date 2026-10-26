package de.cirrus.dusk.level;

import de.cirrus.dusk.entities.Mob;
import de.cirrus.dusk.level.tile.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DepthMapTest {

    private Level level;

    @BeforeEach
    public void setUp() {
        level = new Level(32, 32);
        level.map = new byte[32 * 32];
        level.data = new byte[32 * 32];

        // Fill top half with grass (y: 0..15) and bottom half with water (y: 16..31)
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                if (y < 16) {
                    level.map[x + y * 32] = Tile.grass.id;
                } else {
                    level.map[x + y * 32] = Tile.water.id;
                }
            }
        }
        level.initDepthMap();
    }

    @Test
    public void testLandTileHasZeroDepth() {
        // Tile (10, 10) is grass. Pixel position: (10 * 32 + 16, 10 * 32 + 16)
        float depth = level.getWaterDepth(10 * 32 + 16, 10 * 32 + 16);
        assertEquals(0.0f, depth, 0.001f, "Land tile should have zero depth");
    }

    @Test
    public void testDeepWaterTileHasFullDepth() {
        // Tile (16, 28) is deep water far from shore
        float depth = level.getWaterDepth(16 * 32 + 16, 28 * 32 + 16);
        assertEquals(1.0f, depth, 0.05f, "Deep water tile should reach full depth");
    }

    @Test
    public void testDepthIncreasesMonotonicallyIntoWater() {
        // Move along vertical axis crossing from shore into deep water at x = 16 * 32
        // Shoreline boundary is at y = 16 * 32 = 512
        float previousDepth = 0f;
        for (int py = 16 * 32; py <= 20 * 32; py += 4) {
            float currentDepth = level.getWaterDepth(16 * 32 + 16, py);
            assertTrue(currentDepth >= previousDepth,
                    "Depth should increase monotonically moving deeper into water: at py=" + py
                            + ", current=" + currentDepth + " < previous=" + previousDepth);
            previousDepth = currentDepth;
        }
        assertTrue(previousDepth >= 0.95f, "Should reach near full depth several tiles away from shore");
    }

    @Test
    public void testMobSwimmingStateMatchesWaterPosition() {
        Mob mob = new Mob();
        level.add(mob);

        // Position on grass: (10 * 32 + 16, 10 * 32 + 16)
        mob.x = 10 * 32 + 16;
        mob.y = 10 * 32 + 16;
        mob.updateSwimming();
        assertFalse(mob.swimming, "Mob on grass should not be swimming");
        assertEquals(0.0f, mob.depth, 0.001f);

        // Position in water tile near shore: (16 * 32 + 16, 16 * 32 + 16)
        mob.x = 16 * 32 + 16;
        mob.y = 16 * 32 + 16;
        mob.updateSwimming();
        assertTrue(mob.swimming, "Mob on water tile should be swimming");
        assertTrue(mob.depth > 0.0f && mob.depth < 1.0f, "Mob near shore should have intermediate immersion depth");

        // Position in deep water: (16 * 32 + 16, 28 * 32 + 16)
        mob.x = 16 * 32 + 16;
        mob.y = 28 * 32 + 16;
        mob.updateSwimming();
        assertTrue(mob.swimming, "Mob in deep water should be swimming");
        assertEquals(1.0f, mob.depth, 0.05f);
    }
}
