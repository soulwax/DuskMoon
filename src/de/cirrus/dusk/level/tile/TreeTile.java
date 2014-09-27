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

public class TreeTile extends Tile {
    public TreeTile(int id) {
        super(id);
        connectsToGrass = true;
    }

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
            screen.render(x * scale + 0, y * scale + 0, Art.i.tileset[10][1],  0);
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
}
