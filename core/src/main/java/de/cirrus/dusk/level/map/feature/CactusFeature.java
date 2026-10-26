package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Places cacti across arid sand and desert terrain.
 */
public class CactusFeature implements WorldFeature {

    @Override
    public void place(LevelMap levelMap, TerrainContext context, Random random) {
        int w = levelMap.w;
        int h = levelMap.h;

        for (int i = 0; i < w * h / 100; i++) {
            int xx = random.nextInt(w);
            int yy = random.nextInt(h);
            if (levelMap.isInBounds(xx, yy)) {
                if (levelMap.getTile(xx, yy) == Tile.sand) {
                    levelMap.setTile(xx, yy, Tile.cactus);
                }
            }
        }
    }
}
