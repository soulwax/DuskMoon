package de.cirrus.dusk.level.map;

import java.util.Random;

import de.cirrus.dusk.level.tile.Tile;

/**
 * DuskMoon
 * Copyright (C) 2014 by Cirrus
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * -
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * -
 * Contact: cirrus.contact@t-online.de
 */

public class LevelGen {

    private static final Random random = new Random();
    public double[] values;
    private int w, h;

    public LevelGen(int w, int h, int featureSize) {
        this.w = w;
        this.h = h;

        values = new double[w * h];

        for (int y = 0; y < w; y += featureSize) {
            for (int x = 0; x < w; x += featureSize) {
                setSample(x, y, random.nextFloat() * 2 - 1);
            }
        }

        int stepSize = featureSize;
        double scale = 1.0 / w;
        double scaleMod = 1;
        do {
            int halfStep = stepSize / 2;
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = sample(x, y);
                    double b = sample(x + stepSize, y);
                    double c = sample(x, y + stepSize);
                    double d = sample(x + stepSize, y + stepSize);

                    double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
                    setSample(x + halfStep, y + halfStep, e);
                }
            }
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = sample(x, y);
                    double b = sample(x + stepSize, y);
                    double c = sample(x, y + stepSize);
                    double d = sample(x + halfStep, y + halfStep);
                    double e = sample(x + halfStep, y - halfStep);
                    double f = sample(x - halfStep, y + halfStep);

                    double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    setSample(x + halfStep, y, H);
                    setSample(x, y + halfStep, g);
                }
            }
            stepSize /= 2;
            scale *= (scaleMod + 0.8);
            scaleMod *= 0.3;
        } while (stepSize > 1);
    }

    private double sample(int x, int y) {
        return values[(x & (w - 1)) + (y & (h - 1)) * w];
    }

    private void setSample(int x, int y, double value) {
        values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
    }

    private static byte[][] createTopMap(int w, int h) {
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
                if (xd < 0)
                    xd = -xd;
                if (yd < 0)
                    yd = -yd;
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
                    for (int yy = yo - 1; yy <= yo + 1; yy++)
                        for (int xx = xo - 1; xx <= xo + 1; xx++)
                            if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                                if (map[xx + yy * w] == Tile.grass.id) {
                                    map[xx + yy * w] = Tile.sand.id;
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
        stairsLoop: for (int i = 0; i < w * h / 100; i++) {
            int x = random.nextInt(w - 2) + 1;
            int y = random.nextInt(h - 2) + 1;

            for (int yy = y - 1; yy <= y + 1; yy++)
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    if (map[xx + yy * w] != Tile.rock.id)
                        continue stairsLoop;
                }

            map[x + y * w] = Tile.stairsDown.id;
            count++;
            if (count == 4)
                break;
        }

        return new byte[][] { map, data };
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
            if (count[Tile.rock.id & 0xff] < 100)
                continue;
            if (count[Tile.sand.id & 0xff] < 100)
                continue;
            if (count[Tile.grass.id & 0xff] < 100)
                continue;
            if (count[Tile.tree.id & 0xff] < 100)
                continue;
            if (count[Tile.stairsDown.id & 0xff] < 2)
                continue;
            if (count[Tile.flower.id & 0xff] < 10)
                continue;
            if (count[Tile.cactus.id & 0xff] < 10)
                continue;
            if (count[Tile.stairsDown.id & 0xff] >= 200)
                continue;

            return result;

        } while (true);
    }
}
