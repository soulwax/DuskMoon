package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Procedurally generates sand patches and sandy shores.
 */
public class SandPatchFeature implements WorldFeature {

    @Override
    public void place(LevelMap levelMap, TerrainContext context, Random random) {
        int w = levelMap.w;
        int h = levelMap.h;

        for (int i = 0; i < w * h / 2800; i++) {
            int xs = random.nextInt(w);
            int ys = random.nextInt(h);
            for (int k = 0; k < 10; k++) {
                int x = xs + random.nextInt(21) - 10;
                int y = ys + random.nextInt(21) - 10;
                for (int j = 0; j < 100; j++) {
                    int xo = x + random.nextInt(5) - random.nextInt(5);
                    int yo = y + random.nextInt(5) - random.nextInt(5);
                    for (int yy = yo - 1; yy <= yo + 1; yy++) {
                        for (int xx = xo - 1; xx <= xo + 1; xx++) {
                            if (levelMap.isInBounds(xx, yy)) {
                                if (levelMap.getTile(xx, yy) == Tile.grass) {
                                    levelMap.setTile(xx, yy, Tile.sand);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
