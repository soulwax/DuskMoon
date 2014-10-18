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

public class RockTile extends Tile {

    public RockTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {

        boolean u = level.getTile(x, y - 1) != this;
        boolean d = level.getTile(x, y + 1) != this;
        boolean l = level.getTile(x - 1, y) != this;
        boolean r = level.getTile(x + 1, y) != this;

        boolean ul = level.getTile(x - 1, y - 1) != this;
        boolean dl = level.getTile(x - 1, y + 1) != this;
        boolean ur = level.getTile(x + 1, y - 1) != this;
        boolean dr = level.getTile(x + 1, y + 1) != this;
        int xs = x * 32;
        int ys = y * 32;
        if (!u && !l) {
            if (!ul)
                screen.render(xs + 0, ys + 0, Art.i.tileset[1][0], 0);
            else
                screen.render(xs + 0, ys + 0, Art.i.tileset[7][1], 3);
        } else
            screen.render(xs + 0, ys + 0, Art.i.tileset[l ? 6 : 5][u ? 3 : 2], 3);

        if (!u && !r) {
            if (!ur)
                screen.render(xs + 16, ys + 0, Art.i.tileset[1][0], 0);
            else
                screen.render(xs + 16, ys + 0, Art.i.tileset[8][1], 3);
        } else
            screen.render(xs + 16, ys + 0, Art.i.tileset[r ? 4 : 5][u ? 3 : 2], 3);

        if (!d && !l) {
            if (!dl)
                screen.render(xs + 0, ys + 16, Art.i.tileset[1][0], 0);
            else
                screen.render(xs + 0, ys + 16, Art.i.tileset[7][2], 3);
        } else
            screen.render(xs + 0, ys + 16, Art.i.tileset[l ? 6 : 5][d ? 1 : 2], 3);
        if (!d && !r) {
            if (!dr)
                screen.render(xs + 16, ys + 16, Art.i.tileset[1][0], 0);
            else
                screen.render(xs + 16, ys + 16, Art.i.tileset[8][2], 3);
        } else
            screen.render(xs + 16, ys + 16, Art.i.tileset[r ? 4 : 5][d ? 1 : 2], 3);
    }

}
