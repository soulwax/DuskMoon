package de.cirrus.dusk.level.map;

import java.util.Random;

public class Map {
    private static Random random = new Random();

    public static byte[] getMap(int w, int h) {
        byte[] result = new byte[w * h];

        for(int x = 0; x < w; x++) {
            for(int y = 0; y < h; y++) {
                result[x+y*w] = (byte)random.nextInt(3);
            }
        }

        return result;
    }
}