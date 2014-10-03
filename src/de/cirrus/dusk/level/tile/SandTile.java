package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

/**
 * DuskMoon
 * Copyright (C) 2014 by Cirrus
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * -
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * -
 * Contact: cirrus.contact@t-online.de
 */

public class SandTile extends Tile {
    public SandTile(int id) {
        super(id);
        walkable = true;
        connectsToSand = true;
    }

    public void render(Screen screen, Level level, int x, int y) {

        boolean u = !level.getTile(x, y - 1).connectsToSand;
        boolean d = !level.getTile(x, y + 1).connectsToSand;
        boolean l = !level.getTile(x - 1, y).connectsToSand;
        boolean r = !level.getTile(x + 1, y).connectsToSand;


        if (!u && !l) {
            screen.render(x * 32, y * 32, Art.i.tileset[4][0], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 0, Art.i.tileset[l ? 0 : 1][u ? 6 : 7], 0);

        if (!u && !r) {
            screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[4][0], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 0, Art.i.tileset[r ? 2 : 1][u ? 6 : 7], 0);


        if (!d && !l) {
            screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[4][0], 0);
        } else
            screen.render(x * 32 + 0, y * 32 + 16, Art.i.tileset[l ? 0 : 1][d ? 8 : 7], 0);
        if (!d && !r) {
            screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[4][0], 0);
        } else
            screen.render(x * 32 + 16, y * 32 + 16, Art.i.tileset[r ? 2 : 1][d ? 8 : 7], 0);
    }
}
