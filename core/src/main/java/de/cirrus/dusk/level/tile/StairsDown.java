package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class StairsDown extends Tile {
    public StairsDown(int id) {
        super(id);
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[11][0], 0);
        screen.render(x * 32 + 16, y * 32, Art.i.tileset[12][0], 0);
        screen.render(x * 32, y * 32 + 16, Art.i.tileset[11][1], 0);
        screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[12][1], 0);
    }
}
