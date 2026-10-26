package de.cirrus.dusk.level.tile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.level.Level;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeCollisionTest {

    private Level level;
    private Entity entity;

    @BeforeEach
    public void setUp() {
        level = new Level(32, 32);
        // Fill level with grass
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                level.setTile(x, y, Tile.grass);
            }
        }
        entity = new Entity();
        entity.init(level);
        entity.xr = 5;
        entity.yr = 4;
    }

    @Test
    @DisplayName("Player can walk under tree canopy on top and side edges")
    public void testTreeCanopyPassable() {
        // Place standalone tree at tile (5, 5) -> pixel space [160, 192] x [160, 192]
        level.setTile(5, 5, Tile.tree);

        // Position entity just above the tree canopy and try moving into the canopy area
        // Canopy is at Y in [160, 174]. Stem starts at Y = 174.
        entity.x = 160 + 16; // Center horizontally (176)
        entity.y = 160 + 5;  // In the upper canopy (165)

        // Entity bounding box is [171, 181] x [161, 169] -> should NOT block!
        boolean canMove = entity._move(0, 1);
        assertTrue(canMove, "Moving inside upper tree canopy should be passable");

        // Player to the left of the stem (x = 160 + 4 = 164)
        entity.x = 160 + 4;
        entity.y = 160 + 20; // Y alongside stem
        canMove = entity._move(0, 1);
        assertTrue(canMove, "Moving alongside left edge of tree should be passable");
    }

    @Test
    @DisplayName("Tree stem solidly blocks collision")
    public void testTreeStemBlocks() {
        // Standalone tree at tile (5, 5) -> stem is at [168, 184] x [174, 188]
        level.setTile(5, 5, Tile.tree);

        // Position entity right next to the stem and attempt to move directly into it
        entity.x = 160 + 16; // 176
        entity.y = 160 + 9;  // 169 (feet reach 169 + 4 = 173, just above stem at 174)

        // Moving down by 2 reaches Y = 171, feet reach 175 -> intersects stem [174, 188]!
        boolean canMove = entity._move(0, 2);
        assertFalse(canMove, "Moving directly into tree stem must be blocked");
    }

    @Test
    @DisplayName("Cactus stem blocks while sand edges are passable")
    public void testCactusStemCollision() {
        // Cactus at tile (10, 10) -> [320, 352] x [320, 352]
        level.setTile(10, 10, Tile.cactus);

        // Walk alongside left edge of cactus tile (x = 324)
        entity.x = 320 + 4;
        entity.y = 320 + 20;
        assertTrue(entity._move(0, 1), "Edge of cactus tile should be passable");

        // Walk directly into cactus stem (center 336, 332)
        entity.x = 320 + 16; // 336
        entity.y = 320 + 7;  // feet at 331, stem starts at 332
        assertFalse(entity._move(0, 2), "Direct collision into cactus stem must be blocked");
    }

    @Test
    @DisplayName("Deep forest interior remains fully solid")
    public void testForestInteriorSolid() {
        // Create 3x3 grove of trees
        for (int y = 4; y <= 6; y++) {
            for (int x = 4; x <= 6; x++) {
                level.setTile(x, y, Tile.tree);
            }
        }

        // Center tree at (5, 5) is surrounded on all 4 sides
        entity.x = 160 + 16;
        entity.y = 160 + 2; // Even upper canopy of interior tree should be solid
        assertFalse(entity._move(0, 1), "Deep forest interior must be fully impassable");
    }
}
