package de.cirrus.dusk.level.map.feature;

import java.util.Random;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.TerrainContext;

/**
 * Interface for procedural world features and biome decorators.
 */
public interface WorldFeature {
    /**
     * Places the feature across the level map.
     *
     * @param levelMap the level map being decorated
     * @param context the climate and terrain elevation context
     * @param random the random number generator
     */
    void place(LevelMap levelMap, TerrainContext context, Random random);
}
