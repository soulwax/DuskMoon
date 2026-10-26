package de.cirrus.dusk.entities;

import java.util.Random;

import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

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

        // origin tile
        int xto0 = ((int) (x) - xr) >> 5;
        int xto1 = ((int) (x) + xr) >> 5;
        int yto0 = ((int) (y) - yr) >> 5;
        int yto1 = ((int) (y) + yr) >> 5;

        // adjacent tiles
        int xt0 = ((int) (x + xa) - xr) >> 5;
        int xt1 = ((int) (x + xa) + xr) >> 5;
        int yt0 = ((int) (y + ya) - yr) >> 5;
        int yt1 = ((int) (y + ya) + yr) >> 5;

        // check all adjacent tiles for collisions
        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                // origin already inside adjacent tiles? ignore then
                if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1)
                    continue;
                level.getTile(xt, yt).bumpedInto(level, xt, yt, this); // TODO: process tile collision effect (damage
                                                                       // etc)
                if (!level.getTile(xt, yt).walkable) {
                    // collision, return without moving
                    return false;
                }
            }
        }

        // no collision, thus move
        x += xa;
        y += ya;

        return true;
    }

    public void update(int t) {
    }

    public void render(Screen screen) {
    }

}
