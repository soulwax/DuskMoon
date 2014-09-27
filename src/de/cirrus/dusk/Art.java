package de.cirrus.dusk;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    public Image[][] tileset = loadAndCut("tileset.png", 16, 16, DuskMoon.SCALE);
    public Image[][] fem_player = loadAndCut("fem_blond.png", 64, 32, 1);

    private Image[][] loadAndCut(String path, int sw, int sh, int scale) {

        Image sheet;
        try {
            sheet = loadImage(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load art at " + path);
        }

        int xSlices = sheet.getWidth() / sw;
        int ySlices = sheet.getHeight() / sh;

        Image[][] result = new Image[xSlices][ySlices];

        for(int x = 0; x < xSlices; x++) {
            for(int y = 0; y < ySlices; y++) {
                Image tmp = sheet.getSubImage(x * sw, y * sh, sw, sh);
                if(scale > 1) result[x][y] = tmp.getScaledCopy(scale);
                else result[x][y] = tmp;

                result[x][y].setFilter(Image.FILTER_NEAREST);
            }
        }

        return result;
    }


    private Image loadImage(String path) throws IOException {
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
