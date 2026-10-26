package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class CactusTile extends Tile {
    public CactusTile(int id) {
        super(id);
        connectsToSand = true;
    }

    @Override
    public boolean blocks(Level level, int xt, int yt, Entity entity, double boxX0, double boxY0, double boxX1, double boxY1) {
        // Smaller collision box centered on the cactus stem
        double stemX0 = xt * 32.0 + 10.0;
        double stemX1 = xt * 32.0 + 22.0;
        double stemY0 = yt * 32.0 + 12.0;
        double stemY1 = yt * 32.0 + 28.0;

        return boxX0 < stemX1 && boxX1 > stemX0 && boxY0 < stemY1 && boxY1 > stemY0;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[1][1], 0);
        screen.render(x * 32 + 16, y * 32, Art.i.tileset[2][1], 0);
        screen.render(x * 32, y * 32 + 16, Art.i.tileset[1][2], 0);
        screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[2][2], 0);
    }
}
