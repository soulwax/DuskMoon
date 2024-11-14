package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class FlowerTile extends Tile {
    public FlowerTile(int id) {
        super(id);
        walkable = true;
        connectsToGrass = true;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[3][1], 0);
        screen.render(x * 32 + 16, y * 32, Art.i.tileset[3][1], 0);
        screen.render(x * 32, y * 32 + 16, Art.i.tileset[3][1], 0);
        screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[3][1], 0);
    }
}
