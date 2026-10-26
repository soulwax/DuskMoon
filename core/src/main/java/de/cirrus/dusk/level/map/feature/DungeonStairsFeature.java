package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Places dungeon descent staircases encased within solid rock formations.
 */
public class DungeonStairsFeature implements WorldFeature {

    private final int targetCount;

    public DungeonStairsFeature() {
        this(4);
    }

    public DungeonStairsFeature(int targetCount) {
        this.targetCount = targetCount;
    }

    @Override
    public void place(LevelMap levelMap, TerrainContext context, Random random) {
        int w = levelMap.w;
        int h = levelMap.h;
        int count = 0;

        int maxAttempts = Math.max(w * h / 20, 1000);
        stairsLoop:
        for (int i = 0; i < maxAttempts; i++) {
            int x = random.nextInt(w - 2) + 1;
            int y = random.nextInt(h - 2) + 1;

            for (int yy = y - 1; yy <= y + 1; yy++) {
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    if (levelMap.getTile(xx, yy) != Tile.rock) {
                        continue stairsLoop;
                    }
                }
            }

            levelMap.setTile(x, y, Tile.stairsDown);
            count++;
            if (count >= targetCount) {
                break;
            }
        }
    }
}
