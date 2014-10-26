package de.cirrus.dusk;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * -
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * -
 * Contact: cirrus.contact@t-online.de
 */

public class Art {
    public static Art i;
    public static void init() {
        i = new Art();
    }

    public Image[][] tileset = load("tileset.png", 16, 16);
    public Image[][] fem_player = load("fem_blond_general.png", 64, 64);
    public Image[][] fem_player_attack = load("fem_blond_attack.png", 192, 192);

    private Image[][] load(String path, int sw, int sh) {
        Image sheet;
        try {
            sheet = convertImage(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load art at " + path);
        }
        return cutImage(sheet, sw, sh);
    }

    @SuppressWarnings("unused")
    private Image[][] loadScaled(String path, int sw, int sh, float scale) {
        Image sheet;
        try {
            sheet = convertImage(path);
            // FIXME: according to documentation, this function do no scale the
            // image, but convas instead. As the result image itself will be
            // clipped. That's most likely not what we want.
            sheet = sheet.getScaledCopy(scale);
            sw*=scale;
            sh*=scale;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load art at " + path);
        }
        return cutImage(sheet, sw, sh);

    }

    private Image[][] cutImage(Image image, int sw, int sh) {

        int xSlices = image.getWidth() / sw;
        int ySlices = image.getHeight() / sh;

        Image[][] result = new Image[xSlices][ySlices];

        for(int x = 0; x < xSlices; x++) {
            for(int y = 0; y < ySlices; y++) {
                result[x][y] = image.getSubImage(x * sw, y * sh, sw, sh);
                result[x][y].setFilter(Image.FILTER_NEAREST);
            }
        }

        return result;
    }


    private Image convertImage(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(DuskMoon.class.getResourceAsStream("/"+path));
        Texture texture = BufferedImageUtil.getTexture("", bufferedImage);
        Image image = null;
        try {
            image = new Image(texture.getImageWidth(), texture.getImageHeight());
            image.setTexture(texture);
        } catch (SlickException e) {
            e.printStackTrace();
        }

        return image;
    }
}
