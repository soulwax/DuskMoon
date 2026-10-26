package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class TreeTile extends Tile {

    public TreeTile(int id) {
        super(id);
        connectsToGrass = true;
    }

    @Override
    public boolean blocks(Level level, int xt, int yt, Entity entity, double boxX0, double boxY0, double boxX1, double boxY1) {
        boolean u = level.getTile(xt, yt - 1) == this;
        boolean d = level.getTile(xt, yt + 1) == this;
        boolean l = level.getTile(xt - 1, yt) == this;
        boolean r = level.getTile(xt + 1, yt) == this;

        // Interior of deep forest grove: all 4 sides are trees, full tile is impassable
        if (u && d && l && r) {
            double tX0 = xt * 32.0;
            double tY0 = yt * 32.0;
            double tX1 = tX0 + 32.0;
            double tY1 = tY0 + 32.0;
            return boxX0 < tX1 && boxX1 > tX0 && boxY0 < tY1 && boxY1 > tY0;
        }

        // Tree edges & standalone trees: smaller collision box equivalent to the stem
        double stemX0 = xt * 32.0 + 10.0;
        double stemX1 = xt * 32.0 + 22.0;
        double stemY0 = yt * 32.0 + 14.0;
        double stemY1 = yt * 32.0 + 28.0;

        return boxX0 < stemX1 && boxX1 > stemX0 && boxY0 < stemY1 && boxY1 > stemY0;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        boolean u = level.getTile(x, y - 1) == this;
        boolean l = level.getTile(x - 1, y) == this;
        boolean r = level.getTile(x + 1, y) == this;
        boolean d = level.getTile(x, y + 1) == this;
        boolean ul = level.getTile(x - 1, y - 1) == this;
        boolean ur = level.getTile(x + 1, y - 1) == this;
        boolean dl = level.getTile(x - 1, y + 1) == this;
        boolean dr = level.getTile(x + 1, y + 1) == this;

        int scale = 32;
        int add = 16;
        if (u && ul && l) {
            screen.render(x * scale + 0, y * scale + 0, Art.i.tileset[10][1], 0);
        } else {
            screen.render(x * scale + 0, y * scale + 0, Art.i.tileset[9][0], 0);
        }
        if (u && ur && r) {
            screen.render(x * scale + add, y * scale + 0, Art.i.tileset[10][2], 0);
        } else {
            screen.render(x * scale + add, y * scale + 0, Art.i.tileset[10][0], 0);
        }
        if (d && dl && l) {
            screen.render(x * scale + 0, y * scale + add, Art.i.tileset[10][2], 0);
        } else {
            screen.render(x * scale + 0, y * scale + add, Art.i.tileset[9][1], 0);
        }
        if (d && dr && r) {
            screen.render(x * scale + add, y * scale + add, Art.i.tileset[10][1], 0);
        } else {
            screen.render(x * scale + add, y * scale + add, Art.i.tileset[10][3], 0);
        }
    }

    @Override
    public void renderForeground(Screen screen, Level level, int x, int y) {
        boolean u = level.getTile(x, y - 1) == this;
        boolean l = level.getTile(x - 1, y) == this;
        boolean r = level.getTile(x + 1, y) == this;
        boolean ul = level.getTile(x - 1, y - 1) == this;
        boolean ur = level.getTile(x + 1, y - 1) == this;

        int scale = 32;
        int add = 16;

        // Render top canopy on foreground layer so entities passing behind the tree are covered by foliage
        if (u && ul && l) {
            screen.render(x * scale + 0, y * scale + 0, Art.i.tileset[10][1], 0);
        } else {
            screen.render(x * scale + 0, y * scale + 0, Art.i.tileset[9][0], 0);
        }
        if (u && ur && r) {
            screen.render(x * scale + add, y * scale + 0, Art.i.tileset[10][2], 0);
        } else {
            screen.render(x * scale + add, y * scale + 0, Art.i.tileset[10][0], 0);
        }
    }
}
