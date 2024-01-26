package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class CactusTile extends Tile {
    public CactusTile(int id) {
        super(id);
        connectsToSand = true;
    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[1][1], 0);
        screen.render(x * 32 + 16, y * 32, Art.i.tileset[2][1], 0);
        screen.render(x * 32, y * 32 + 16, Art.i.tileset[1][2], 0);
        screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[2][2], 0);
    }
}
