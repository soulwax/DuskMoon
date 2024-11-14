package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class MetalPlate extends Tile {

    public MetalPlate(int id) {
        super(id);
        walkable = false;
    }
    
    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 32, y * 32, Art.i.tileset[0][0], 0);
    }
}
