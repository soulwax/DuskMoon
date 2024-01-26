package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class WaterTile extends Tile {
    public WaterTile(int id) {
        super(id);
        walkable = true;
        connectsToSand = true;
        connectsToWater = true;
    }

    public void render(Screen screen, Level level, int x, int y) {

        boolean u = !level.getTile(x, y - 1).connectsToWater;
        boolean d = !level.getTile(x, y + 1).connectsToWater;
        boolean l = !level.getTile(x - 1, y).connectsToWater;
        boolean r = !level.getTile(x + 1, y).connectsToWater;

        boolean su = u && level.getTile(x, y - 1).connectsToSand;
        boolean sd = d && level.getTile(x, y + 1).connectsToSand;
        boolean sl = l && level.getTile(x - 1, y).connectsToSand;
        boolean sr = r && level.getTile(x + 1, y).connectsToSand;

        boolean ul = !level.getTile(x - 1, y - 1).connectsToWater;
        boolean dl = !level.getTile(x - 1, y + 1).connectsToWater;
        boolean ur = !level.getTile(x + 1, y - 1).connectsToWater;
        boolean dr = !level.getTile(x + 1, y + 1).connectsToWater;

        boolean sul = ul & level.getTile(x - 1, y - 1).connectsToSand;
        boolean sdl = dl & level.getTile(x - 1, y + 1).connectsToSand;
        boolean sur = ur & level.getTile(x + 1, y - 1).connectsToSand;
        boolean sdr = dr & level.getTile(x + 1, y + 1).connectsToSand;

        // 4|3 5|3 6|3
        // 4|4 5|4 6|4
        // 4|5 5|5 6|5
        // transit 1 = dirt
        // transit 2 = sand

        if (!u && !l) {
            if (!ul)
                screen.render(x * 32, y * 32, Art.i.tileset[5][0], 0);
            else
                screen.render(x * 32, y * 32, Art.i.tileset[(sul) ? 5 : 8][8], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 0, Art.i.tileset[(su || sl) ? (l ? 4 : 5) : (l ? 7 : 8)][u ? 4 : 5], 0);

        if (!u && !r) {
            if (!ur)
                screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[5][0], 0);
            else
                screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[(sur) ? 4 : 7][8], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[(su || sr) ? (r ? 6 : 5) : (r ? 9 : 8)][u ? 4 : 5], 0);

        if (!d && !l) {
            if (!dl)
                screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[5][0], 0);
            else
                screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[(sdl) ? 5 : 8][7], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[(sd || sl) ? (l ? 4 : 5) : (l ? 7 : 8)][d ? 6 : 5], 0);
        if (!d && !r) {
            if (!dr)
                screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[5][0], 0);
            else
                screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[(sdr) ? 4 : 7][7], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[(sd || sr) ? (r ? 6 : 5) : (r ? 9 : 8)][d ? 6 : 5],
                    0);
    }

    public void update(Level level, int xt, int yt) {

    }
}
