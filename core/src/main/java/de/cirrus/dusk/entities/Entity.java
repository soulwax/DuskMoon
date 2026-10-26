package de.cirrus.dusk.entities;

import java.util.Random;

import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
import de.cirrus.dusk.level.tile.Tile;

public class Entity {
    protected final Random random = new Random();
    public double x, y, xa, ya;
    public int xr = 6;
    public int yr = 6;
    public boolean removed;
    public Level level;

    public final void init(Level level) {
        this.level = level;
    }

    public void remove() {
        removed = true;
    }

    public boolean move(double xa, double ya) {
        if (xa != 0 || ya != 0) {
            boolean stopped = true;
            if (xa != 0 && _move(xa, 0))
                stopped = false;
            if (ya != 0 && _move(0, ya))
                stopped = false;
            if (!stopped) {
                int xt = (int) x >> 5;
                int yt = (int) y >> 5;
                level.getTile(xt, yt).steppedOn(level, xt, yt, this);
            }
            return !stopped;
        }
        return true;
    }

    public boolean _move(double xa, double ya) {
        if (xa != 0 && ya != 0)
            throw new IllegalArgumentException("_move can only move along one axis at a time");

        double targetX = x + xa;
        double targetY = y + ya;

        double boxX0 = targetX - xr;
        double boxX1 = targetX + xr;
        double boxY0 = targetY - yr;
        double boxY1 = targetY + yr;

        int xt0 = (int) Math.floor(boxX0 / 32.0);
        int xt1 = (int) Math.floor(boxX1 / 32.0);
        int yt0 = (int) Math.floor(boxY0 / 32.0);
        int yt1 = (int) Math.floor(boxY1 / 32.0);

        // Check all overlapping tiles for solid collisions
        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                Tile tile = level.getTile(xt, yt);
                if (tile.blocks(level, xt, yt, this, boxX0, boxY0, boxX1, boxY1)) {
                    tile.bumpedInto(level, xt, yt, this);
                    return false;
                }
            }
        }

        // No collision, proceed with movement
        x = targetX;
        y = targetY;

        return true;
    }

    public void update(int t) {
    }

    public void render(Screen screen) {
    }

}
