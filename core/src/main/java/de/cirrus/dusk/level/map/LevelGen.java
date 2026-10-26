package de.cirrus.dusk.level.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import de.cirrus.dusk.level.tile.Tile;

public class LevelGen {

    private static final Random random = new Random();
    private final double[] values;
    private final double[] moistureValues;
    private final double[] temperatureValues;
    private final int w, h;

    public LevelGen(int w, int h, int featureSize) {
        this.w = w;
        this.h = h;

        values = new double[w * h];
        moistureValues = new double[w * h];
        temperatureValues = new double[w * h];

        // Initialize base terrain noise
        generateNoise(values, w, h, featureSize);

        // Initialize moisture and temperature maps
        generateNoise(moistureValues, w, h, featureSize * 2); // Larger feature size for broader moisture zones
        generateNoise(temperatureValues, w, h, featureSize * 2); // Larger feature size for broader temperature zones
    }

    private void generateNoise(double[] noiseArray, int width, int height, int featureSize) {
        for (int y = 0; y < height; y += featureSize) {
            for (int x = 0; x < width; x += featureSize) {
                setSample(noiseArray, x, y, random.nextFloat() * 2 - 1);
            }
        }

        int stepSize = featureSize;
        double scale = 1.0 / width;
        double scaleMod = 1;
        do {
            int halfStep = stepSize / 2;
            for (int y = 0; y < width; y += stepSize) {
                for (int x = 0; x < width; x += stepSize) {
                    double a = sample(noiseArray, x, y);
                    double b = sample(noiseArray, x + stepSize, y);
                    double c = sample(noiseArray, x, y + stepSize);
                    double d = sample(noiseArray, x + stepSize, y + stepSize);

                    double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
                    setSample(noiseArray, x + halfStep, y + halfStep, e);
                }
            }
            for (int y = 0; y < width; y += stepSize) {
                for (int x = 0; x < width; x += stepSize) {
                    double a = sample(noiseArray, x, y);
                    double b = sample(noiseArray, x + stepSize, y);
                    double c = sample(noiseArray, x, y + stepSize);
                    double d = sample(noiseArray, x + halfStep, y + halfStep);
                    double e = sample(noiseArray, x + halfStep, y - halfStep);
                    double f = sample(noiseArray, x - halfStep, y + halfStep);

                    double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    setSample(noiseArray, x + halfStep, y, H);
                    setSample(noiseArray, x, y + halfStep, g);
                }
            }
            stepSize /= 2;
            scale *= (scaleMod + 0.8);
            scaleMod *= 0.3;
        } while (stepSize > 1);
    }

    private double sample(double[] noiseArray, int x, int y) {
        return noiseArray[(x & (w - 1)) + (y & (h - 1)) * w];
    }

    private void setSample(double[] noiseArray, int x, int y, double value) {
        noiseArray[(x & (w - 1)) + (y & (h - 1)) * w] = value;
    }

    private static byte[][] createTopMap(int w, int h) {
        // TODO: Use the new moisture and temperature maps along with the original
        // terrain map
        // to create more detailed biomes

        LevelGen mnoise1 = new LevelGen(w, h, 16);
        LevelGen mnoise2 = new LevelGen(w, h, 16);
        LevelGen mnoise3 = new LevelGen(w, h, 16);

        LevelGen noise1 = new LevelGen(w, h, 32);
        LevelGen noise2 = new LevelGen(w, h, 32);

        byte[] map = new byte[w * h];
        byte[] data = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + y * w;

                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;
                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

                double xd = x / (w - 1.0) * 2 - 1;
                double yd = y / (h - 1.0) * 2 - 1;
                if (xd < 0) {
                    xd = -xd;
                }
                if (yd < 0) {
                    yd = -yd;
                }
                double dist = xd >= yd ? xd : yd;
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1 - dist * 20;

                if (val < -0.5) {
                    map[i] = Tile.water.id;
                } else if (val > 0.5 && mval < -1.5) {
                    map[i] = Tile.rock.id;
                } else {
                    map[i] = Tile.grass.id;
                }
            }
        }

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
                            if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                                if (map[xx + yy * w] == Tile.grass.id) {
                                    map[xx + yy * w] = Tile.sand.id;
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
         * for (int i = 0; i < w * h / 2800; i++) { int xs = random.nextInt(w); int ys =
         * random.nextInt(h); for (int k = 0; k < 10; k++) { int x = xs +
         * random.nextInt(21) - 10; int y = ys + random.nextInt(21) - 10; for (int j =
         * 0; j < 100; j++) { int xo = x + random.nextInt(5) - random.nextInt(5); int yo
         * = y + random.nextInt(5) - random.nextInt(5); for (int yy = yo - 1; yy <= yo +
         * 1; yy++) for (int xx = xo - 1; xx <= xo + 1; xx++) if (xx >= 0 && yy >= 0 &&
         * xx < w && yy < h) { if (map[xx + yy * w] == Tile.grass.id) { map[xx + yy * w]
         * = Tile.dirt.id; } } } } }
         */
        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            for (int j = 0; j < 200; j++) {
                int xx = x + random.nextInt(15) - random.nextInt(15);
                int yy = y + random.nextInt(15) - random.nextInt(15);
                if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                    if (map[xx + yy * w] == Tile.grass.id) {
                        map[xx + yy * w] = Tile.tree.id;
                    }
                }
            }
        }

        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int col = random.nextInt(4);
            for (int j = 0; j < 30; j++) {
                int xx = x + random.nextInt(5) - random.nextInt(5);
                int yy = y + random.nextInt(5) - random.nextInt(5);
                if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                    if (map[xx + yy * w] == Tile.grass.id) {
                        map[xx + yy * w] = Tile.flower.id;
                        data[xx + yy * w] = (byte) (col + random.nextInt(4) * 16);
                    }
                }
            }
        }

        for (int i = 0; i < w * h / 100; i++) {
            int xx = random.nextInt(w);
            int yy = random.nextInt(h);
            if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                if (map[xx + yy * w] == Tile.sand.id) {
                    map[xx + yy * w] = Tile.cactus.id;
                }
            }
        }

        int count = 0;
        stairsLoop:
        for (int i = 0; i < w * h / 100; i++) {
            int x = random.nextInt(w - 2) + 1;
            int y = random.nextInt(h - 2) + 1;

            for (int yy = y - 1; yy <= y + 1; yy++) {
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    if (map[xx + yy * w] != Tile.rock.id) {
                        continue stairsLoop;
                    }
                }
            }

            map[x + y * w] = Tile.stairsDown.id;
            count++;
            if (count == 4) {
                break;
            }
        }

        addDetails(map, w, h);
        return new byte[][]{map, data};
    }

    // TODO: Prototype for determining biome
    private static byte determineBiome(double elevation, double moisture, double temperature) {
        // Simple biome logic, needs refinement for more complex biomes
        if (elevation < -0.1) {
            return Tile.water.id; // Lower areas are water
        } else if (elevation > 0.8) {
            return Tile.rock.id; // High elevations are rocky
        } else if (moisture > 0.5) {
            if (temperature < 0.2) {
                // TODO: return Tile.snow.id; // Cold and moist areas are snowy
                return Tile.grass.id; // TODO: Remove this line
            } else {
                return Tile.grass.id; // Otherwise, they are lush with grass
            }
        } else {
            return Tile.sand.id; // Dry areas are sandy
        }
    }

    private static void addDetails(byte[] map, int w, int h) {
        // TODO: Add trees to grass tiles
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + y * w;
                if (map[i] == Tile.grass.id && random.nextFloat() < 0.1) { // 10% chance to add a tree on grass tiles
                    map[i] = Tile.tree.id;
                }
            }
        }
    }

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

    public static void main(String[] args) {
        int d = 0;
        while (true) {
            int w = 128;
            int h = 128;

            byte[] map = LevelGen.createAndValidateTopMap(w, h)[0];
            // byte[] map = LevelGen.createAndValidateUndergroundMap(w, h, (d++ % 3) + 1)[0];
            // byte[] map = LevelGen.createAndValidateSkyMap(w, h)[0];

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
            JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(w * 4, h * 4, Image.SCALE_AREA_AVERAGING)));
        }
    }
}
