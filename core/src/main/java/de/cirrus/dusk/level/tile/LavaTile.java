package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class LavaTile extends Tile {
    public LavaTile(int id) {
        super(id);
        connectsToSand = true;
        connectsToLava = true;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[7][0], 0);
    }
    
    @Override
    public void update(Level level, int xt, int yt) {
    }
}
