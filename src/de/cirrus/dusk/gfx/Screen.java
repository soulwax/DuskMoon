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
        boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

        if(mirrorX || mirrorY) {
            image.getFlippedCopy(mirrorX, mirrorY).draw(xp, yp);
            return;
        }

        image.draw(xp, yp);
    }

    public void renderShadow(int xp, int yp, Image image, int bits) {
        xp -= xOffset;
        yp -= yOffset;
        boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

        if(mirrorX || mirrorY) {
            image.getFlippedCopy(mirrorX, mirrorY).draw(xp, yp);
            return;
        }
        xp+=image.getWidth()-image.getWidth()/1.25F;
        yp+=image.getHeight()-image.getHeight()/1.5F;
        image.setCenterOfRotation((image.getWidth()/1.5F)/2, image.getHeight()/1.5F);
        image.rotate(25);
        image.drawFlash(xp, yp, image.getWidth()/1.5F, image.getHeight()/1.5F, shadow);
        image.rotate(-25);
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
