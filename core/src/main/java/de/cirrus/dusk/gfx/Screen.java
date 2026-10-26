package de.cirrus.dusk.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Screen {
    public int xOffset;
    public int yOffset;
    public final int w, h;

    public static final int BIT_MIRROR_X = 0x01;
    public static final int BIT_MIRROR_Y = 0x02;

    public final Color shadow = new Color(0f, 0f, 0f, 0.5f);

    private SpriteBatch batch;

    public Screen(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public Screen(SpriteBatch batch, int w, int h) {
        this.batch = batch;
        this.w = w;
        this.h = h;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void render(int xp, int yp, TextureRegion image, int bits) {
        render(xp, yp, image, 1f, 1f, bits);
    }

    public void render(final int xp, final int yp, final TextureRegion image, final float scale, final int bits) {
        render(xp, yp, image, scale, 1f, bits);
    }

    public void render(int xp, int yp, final TextureRegion image, final float scale, final float alpha, final int bits) {
        if (image == null || batch == null) return;

        xp -= xOffset;
        yp -= yOffset;

        boolean mirrorX = (bits & BIT_MIRROR_X) != 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) != 0;

        float width = image.getRegionWidth() * scale;
        float height = image.getRegionHeight() * scale;

        TextureRegion target = image;
        if (mirrorX || mirrorY) {
            target = new TextureRegion(image);
            target.flip(mirrorX, mirrorY);
        }

        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        batch.draw(target, xp, yp, width, height);
        batch.setColor(Color.WHITE);
    }

    public void renderSubImage(final int xp, final int yp, final TextureRegion image, final int x, final int y,
            final int vwidth, final int vheight, final float scale, final int bits) {
        renderSubImage(xp, yp, image, x, y, vwidth, vheight, scale, 1f, bits);
    }

    public void renderSubImage(int xp, int yp, final TextureRegion image, final int x, final int y, final int vwidth,
            final int vheight, final float scale, final float alpha, final int bits) {
        if (image == null || batch == null) return;

        int subW = image.getRegionWidth() - vwidth;
        int subH = image.getRegionHeight() - vheight;
        if (subW <= 0 || subH <= 0) return;

        xp -= xOffset;
        yp -= yOffset;

        boolean mirrorX = (bits & BIT_MIRROR_X) != 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) != 0;

        TextureRegion target = createSubRegion(image, x, y, subW, subH);
        if (mirrorX || mirrorY) {
            target.flip(mirrorX, mirrorY);
        }

        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        batch.draw(target, xp, yp, subW * scale, subH * scale);
        batch.setColor(Color.WHITE);
    }

    public void renderShadow(int xp, int yp, final TextureRegion image, final float scale, final int bits) {
        if (image == null || batch == null) return;

        xp -= xOffset;
        yp -= yOffset;

        boolean mirrorX = (bits & BIT_MIRROR_X) != 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) != 0;

        TextureRegion target = image;
        if (mirrorX || mirrorY) {
            target = new TextureRegion(image);
            target.flip(mirrorX, mirrorY);
        }

        float origW = target.getRegionWidth();
        float origH = target.getRegionHeight();

        float drawW = origW / 1.5f * scale;
        float drawH = origH / 1.5f * scale;

        xp += origW - origW / 1.25f * scale;
        yp += origH - origH / 1.5f * scale;

        float originX = drawW / 2f;
        float originY = drawH;

        batch.setColor(shadow);
        batch.draw(target, xp, yp, originX, originY, drawW, drawH, 1f, 1f, -25f);
        batch.setColor(Color.WHITE);
    }

    public void renderSubImageShadow(int xp, int yp, final TextureRegion image, final int vwidth, final int vheight,
            final float scale, final int bits) {
        if (image == null || batch == null) return;

        int subW = image.getRegionWidth() - vwidth;
        int subH = image.getRegionHeight() - vheight;
        if (subW <= 0 || subH <= 0) return;

        xp -= xOffset;
        yp -= yOffset;

        boolean mirrorX = (bits & BIT_MIRROR_X) != 0;
        boolean mirrorY = (bits & BIT_MIRROR_Y) != 0;

        float origW = image.getRegionWidth();
        float origH = image.getRegionHeight();

        xp += origW - origW / 1.25f * scale;
        yp += origH - origH / 1.5f * scale;

        TextureRegion target = createSubRegion(image, 0, 0, subW, subH);
        if (mirrorX || mirrorY) {
            target.flip(mirrorX, mirrorY);
        }

        float drawW = subW / 1.5f * scale;
        float drawH = subH / 1.5f * scale;

        float originX = (origW / 1.5f * scale) / 2f;
        float originY = origH / 1.5f * scale;

        batch.setColor(shadow);
        batch.draw(target, xp, yp, originX, originY, drawW, drawH, 1f, 1f, -25f);
        batch.setColor(Color.WHITE);
    }

    private TextureRegion createSubRegion(TextureRegion source, int x, int y, int width, int height) {
        int rx = source.getRegionX() + x;
        int ry = source.getRegionY() + y;
        TextureRegion sub = new TextureRegion(source.getTexture(), rx, ry, width, height);
        // Correct for Y-down camera
        sub.flip(false, true);
        return sub;
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
