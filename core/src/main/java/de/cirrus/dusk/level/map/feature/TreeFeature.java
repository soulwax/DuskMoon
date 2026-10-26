package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.biome.BiomeType;
import de.cirrus.dusk.level.map.noise.TerrainContext;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Places tree groves and forest clusters across the landscape.
 */
public class TreeFeature implements WorldFeature {

    @Override
    public void place(LevelMap levelMap, TerrainContext context, Random random) {
        int w = levelMap.w;
        int h = levelMap.h;

        // Clustered forest groves
        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            for (int j = 0; j < 200; j++) {
                int xx = x + random.nextInt(15) - random.nextInt(15);
                int yy = y + random.nextInt(15) - random.nextInt(15);
                if (levelMap.isInBounds(xx, yy)) {
                    if (levelMap.getTile(xx, yy) == Tile.grass) {
                        levelMap.setTile(xx, yy, Tile.tree);
                    }
                }
            }
        }

        // Additional density in designated Forest biomes
        if (context.biomes != null) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int idx = context.getIndex(x, y);
                    if (context.biomes[idx] != null && context.biomes[idx].getType() == BiomeType.FOREST) {
                        if (levelMap.getTile(x, y) == Tile.grass && random.nextFloat() < 0.25f) {
                            levelMap.setTile(x, y, Tile.tree);
                        }
                    }
                }
            }
        }
    }
}
