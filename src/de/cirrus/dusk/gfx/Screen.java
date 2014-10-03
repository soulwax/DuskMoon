package de.cirrus.dusk.gfx;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

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

public class Screen {
    public int xOffset;
    public int yOffset;
    public final int w, h;

    public static final int BIT_MIRROR_X = 0x01;
    public static final int BIT_MIRROR_Y = 0x02;

    public Color shadow = new Color(0,0,0,0.5f);

    public Screen(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void render(int xp, int yp, Image image, int bits) {
        xp -= xOffset;
        yp -= yOffset;

        Image target = image;
        if(bits > 0) target = getFlippedCopy(image, bits);

        target.draw(xp, yp, image.getWidth(), image.getHeight());
    }

    public void renderScaled(int xp, int yp, Image image, float scale, int bits) {
        xp -= xOffset;
        yp -= yOffset;

        Image target = image;
        if(bits > 0) target = getFlippedCopy(image, bits);

        target.draw(xp, yp, scale);
    }

    public void renderSubImage(int xp, int yp, Image image, int vwidth, int vheight, int bits) {
        xp -= xOffset;
        yp -= yOffset;

        Image target = image.getSubImage(0, 0, image.getWidth()-vwidth, image.getHeight()-vheight);
        if(bits > 0) target = getFlippedCopy(target, bits);

        target.draw(xp, yp);
    }

    public void renderShadow(int xp, int yp, Image image, int bits) {
        xp -= xOffset;
        yp -= yOffset;
        Image target = image;
        if(bits > 0) target = getFlippedCopy(image, bits);

        xp+=target.getWidth()-target.getWidth()/1.25F;
        yp+=target.getHeight()-target.getHeight()/1.5F;
        target.setCenterOfRotation((target.getWidth()/1.5F)/2, target.getHeight()/1.5F);
        target.rotate(25);
        target.drawFlash(xp, yp, target.getWidth()/1.5F, target.getHeight()/1.5F, shadow);
        target.rotate(-25);
    }

    private Image getFlippedCopy(Image image, int bits) {
        boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

        return image.getFlippedCopy(mirrorX, mirrorY);
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
