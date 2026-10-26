package de.cirrus.dusk.level.map.noise;

import java.util.Random;

/**
 * 2D fractal noise generator based on midpoint displacement.
 */
public class NoiseMap {
    public final int w;
    public final int h;
    public final double[] values;

    public NoiseMap(int w, int h, int featureSize, Random random) {
        this.w = w;
        this.h = h;
        this.values = new double[w * h];
        generateNoise(values, w, h, featureSize, random);
    }

    private void generateNoise(double[] noiseArray, int width, int height, int featureSize, Random random) {
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

    public double sample(int x, int y) {
        return values[(x & (w - 1)) + (y & (h - 1)) * w];
    }

    private double sample(double[] noiseArray, int x, int y) {
        return noiseArray[(x & (w - 1)) + (y & (h - 1)) * w];
    }

    private void setSample(double[] noiseArray, int x, int y, double value) {
        noiseArray[(x & (w - 1)) + (y & (h - 1)) * w] = value;
    }
}
