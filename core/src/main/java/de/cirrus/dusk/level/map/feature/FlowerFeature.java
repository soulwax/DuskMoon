package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Places colorful flower patches across grassy areas.
 */
public class FlowerFeature implements WorldFeature {

    @Override
    public void place(LevelMap levelMap, TerrainContext context, Random random) {
        int w = levelMap.w;
        int h = levelMap.h;

        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int col = random.nextInt(4);
            for (int j = 0; j < 30; j++) {
                int xx = x + random.nextInt(5) - random.nextInt(5);
                int yy = y + random.nextInt(5) - random.nextInt(5);
                if (levelMap.isInBounds(xx, yy)) {
                    if (levelMap.getTile(xx, yy) == Tile.grass) {
                        levelMap.setTile(xx, yy, Tile.flower);
                        levelMap.setData(xx, yy, (byte) (col + random.nextInt(4) * 16));
                    }
                }
            }
        }
    }
}
