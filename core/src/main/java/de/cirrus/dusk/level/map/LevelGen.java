package de.cirrus.dusk.level.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import de.cirrus.dusk.level.LevelMap;
import de.cirrus.dusk.level.map.noise.NoiseMap;
import de.cirrus.dusk.level.tile.Tile;

/**
 * Procedural level generation façade and map validation utility.
 * Delegates actual procedural synthesis and feature decoration to {@link WorldGenerator}.
 */
public class LevelGen {

    private static final Random random = new Random();
    private static final WorldGenerator worldGenerator = new WorldGenerator(random);

    public final double[] values;
    public final double[] moistureValues;
    public final double[] temperatureValues;
    public final int w, h;

    public LevelGen(int w, int h, int featureSize) {
        this.w = w;
        this.h = h;

        NoiseMap baseNoise = new NoiseMap(w, h, featureSize, random);
        NoiseMap moistureNoise = new NoiseMap(w, h, featureSize * 2, random);
        NoiseMap tempNoise = new NoiseMap(w, h, featureSize * 2, random);

        this.values = baseNoise.values;
        this.moistureValues = moistureNoise.values;
        this.temperatureValues = tempNoise.values;
    }

    /**
     * Generates a raw top-level map array pair [map, data] using the modular WorldGenerator.
     *
     * @param w width in tiles
     * @param h height in tiles
     * @return 2D byte array containing [tiles, data]
     */
    public static byte[][] createTopMap(int w, int h) {
        return worldGenerator.generateRaw(w, h);
    }

    /**
     * Generates and validates a top-level map, ensuring critical gameplay features
     * (rock formations, sand, trees, stairs, flowers, and cacti) meet minimum distribution thresholds.
     *
     * @param w width in tiles
     * @param h height in tiles
     * @return validated 2D byte array containing [tiles, data]
     */
    public static byte[][] createAndValidateTopMap(int w, int h) {
        int attempt = 0;
        do {
            attempt++;
            byte[][] result = createTopMap(w, h);
            int[] count = new int[256];

            for (int i = 0; i < w * h; i++) {
                count[result[0][i] & 0xff]++;
            }
            if (count[Tile.rock.id & 0xff] < 100) {
                continue;
            }
            if (count[Tile.sand.id & 0xff] < 100) {
                continue;
            }
            if (count[Tile.grass.id & 0xff] < 100) {
                continue;
            }
            if (count[Tile.tree.id & 0xff] < 100) {
                continue;
            }
            if (count[Tile.stairsDown.id & 0xff] < 2) {
                continue;
            }
            if (count[Tile.flower.id & 0xff] < 10) {
                continue;
            }
            if (count[Tile.cactus.id & 0xff] < 10) {
                continue;
            }
            if (count[Tile.stairsDown.id & 0xff] >= 200) {
                continue;
            }

            return result;

        } while (true);
    }

    /**
     * Generates and validates a top-level map, returning a structured {@link LevelMap}.
     */
    public static LevelMap createAndValidateLevelMap(int w, int h) {
        byte[][] raw = createAndValidateTopMap(w, h);
        return new LevelMap(w, h, raw[0], raw[1]);
    }

    /**
     * Standalone interactive Swing previewer for procedural map generation.
     */
    public static void main(String[] args) {
        int d = 0;
        while (true) {
            int w = 256;
            int h = 256;

            byte[] map = LevelGen.createAndValidateTopMap(w, h)[0];

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;

                    if (map[i] == Tile.water.id) {
                        pixels[i] = 0x000080;
                    }
                    if (map[i] == Tile.grass.id) {
                        pixels[i] = 0x208020;
                    }
                    if (map[i] == Tile.rock.id) {
                        pixels[i] = 0xa0a0a0;
                    }
                    if (map[i] == Tile.dirt.id) {
                        pixels[i] = 0x604040;
                    }
                    if (map[i] == Tile.sand.id) {
                        pixels[i] = 0xa0a040;
                    }
                    if (map[i] == Tile.tree.id) {
                        pixels[i] = 0x003000;
                    }
                    if (map[i] == Tile.lava.id) {
                        pixels[i] = 0xff2020;
                    }
                    if (map[i] == Tile.stairsDown.id) {
                        pixels[i] = 0xffffff;
                    }
                }
            }
            img.setRGB(0, 0, w, h, pixels, 0, w);
            int response = JOptionPane.showConfirmDialog(
                    null,
                    new ImageIcon(img.getScaledInstance(w * 2, h * 2, Image.SCALE_AREA_AVERAGING)),
                    "Procedural Level Preview (256x256)",
                    JOptionPane.YES_NO_OPTION
            );
            if (response != JOptionPane.YES_OPTION) {
                break;
            }
        }
    }
}
