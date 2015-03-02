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
package de.cirrus.dusk.gfx;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

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
        render(xp, yp, image, 1f, 1f, bits);
    }

    public void render(final int xp, final int yp, final Image image, final float scale, final int bits) {
        render(xp, yp, image, scale, 1f, bits);
    }

    /**
     * Render scaled image with alpha channel set.
     *
     * @param xp
     *            the x position
     * @param yp
     *            the y position
     * @param image
     *            the image to render
     * @param scale
     *            the image scale
     * @param alpha
     *            the alpha (transparency)
     * @param bits
     *            the mirror bits
     */
    public void render(int xp, int yp, final Image image, final float scale, final float alpha, final int bits) {
        xp -= xOffset;
        yp -= yOffset;

        Image target = image;
        if(bits > 0) {
            target = getFlippedCopy(image, bits);
        }

        target.draw(xp, yp, scale, new Color(1.0f,1.0f,1.0f,alpha));
    }

    public void renderSubImage(final int xp, final int yp, final Image image, final int x, final int y, final int vwidth, final int vheight, final float scale, final int bits) {
        renderSubImage(xp, yp, image, x, y, vwidth, vheight, scale, 1f, bits);
    }

    /**
     * Render sub image.
     *
     * @param xp
     *            the image x position
     * @param yp
     *            the image y position
     * @param image
     *            the image
     * @param x
     *            the x coordinate of sub-image
     * @param y
     *            the y coordinate of sub-image
     * @param vwidth
     *            the sub-image width difference
     * @param vheight
     *            the sub-image height difference
     * @param scale
     *            the image scale
     * @param alpha
     *            the alpha (transparency)
     * @param bits
     *            the mirror bits
     */
    public void renderSubImage(int xp, int yp, final Image image, final int x, final int y, final int vwidth, final int vheight, final float scale, final float alpha, final int bits) {
        xp -= xOffset;
        yp -= yOffset;

        Image target = image.getSubImage(x, y, image.getWidth()-vwidth, image.getHeight()-vheight);
        if(bits > 0) {
            target = getFlippedCopy(target, bits);
        }

        target.draw(xp, yp, scale, new Color(1.0f,1.0f,1.0f,alpha));
    }

    public void renderShadow(int xp, int yp, final Image image, final float scale, final int bits) {
        xp -= xOffset;
        yp -= yOffset;
        Image target = image;
        if(bits > 0) target = getFlippedCopy(image, bits);

        xp += target.getWidth() - target.getWidth() / 1.25F * scale;
        yp += target.getHeight() - target.getHeight() / 1.5F * scale;
        target.setCenterOfRotation(target.getWidth()/1.5F/2, target.getHeight()/1.5F);
        target.rotate(25);
        target.drawFlash(xp, yp, target.getWidth() / 1.5F * scale,
                target.getHeight() / 1.5F * scale, shadow);
        target.rotate(-25);
    }

    /**
     * Renders sub-image's shadow. Acts same way as
     * {@link #renderShadow(int, int, Image, float, int)}, but cuts the image by
     * <code>vwidth</code> and <code>vheight</code>. Rotates shadow using same
     * center as original image.
     *
     * TODO: get rid of code duplications.
     *
     * @param xp
     *            the x position
     * @param yp
     *            the y position
     * @param image
     *            the image to render
     * @param vwidth
     *            the sub-image width difference
     * @param vheight
     *            the sub-image height difference
     * @param scale
     *            the image scale
     * @param bits
     *            the mirror bits
     */
    public void renderSubImageShadow(int xp, int yp, final Image image, final int vwidth, final int vheight, final float scale, final int bits) {
        xp -= xOffset;
        yp -= yOffset;
        Image target = image;
        if (bits > 0) {
            target = getFlippedCopy(image, bits);
        }

        xp += target.getWidth() - target.getWidth() / 1.25F * scale;
        yp += target.getHeight() - target.getHeight() / 1.5F * scale;

        final int w = target.getWidth();
        final int h = target.getHeight();
        target = target.getSubImage(0, 0, target.getWidth() - vwidth, target.getHeight() - vheight);
        target.setCenterOfRotation(w / 1.5F / 2, h / 1.5F);
        target.rotate(25);
        target.drawFlash(xp, yp, target.getWidth() / 1.5F * scale, target.getHeight() / 1.5F * scale, shadow);
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
