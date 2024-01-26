package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class GrassTile extends Tile {

    public GrassTile(int id) {
        super(id);
        walkable = true;
        connectsToGrass = true;
    }

    public void render(Screen screen, Level level, int x, int y) {

        boolean u = !level.getTile(x, y - 1).connectsToGrass;
        boolean d = !level.getTile(x, y + 1).connectsToGrass;
        boolean l = !level.getTile(x - 1, y).connectsToGrass;
        boolean r = !level.getTile(x + 1, y).connectsToGrass;

        if (!u && !l) {
            screen.render(x * 32, y * 32, Art.i.tileset[1][0], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 0, Art.i.tileset[l ? 0 : 1][u ? 3 : 4], 0);

        if (!u && !r) {
            screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[1][0], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[r ? 2 : 1][u ? 3 : 4], 0);

        if (!d && !l) {
            screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[1][0], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[l ? 0 : 1][d ? 5 : 4], 0);
        if (!d && !r) {
            screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[1][0], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[r ? 2 : 1][d ? 5 : 4], 0);
    }
}
