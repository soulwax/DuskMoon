package de.cirrus.dusk.entities;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
import de.cirrus.dusk.level.tile.Tile;
import de.cirrus.dusk.states.Game;

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

public class Player extends Mob {
    private float xt, yt;
    public Game game;
    public InputHandler input;
    public static float speed = .25f;

    public Player(Game game, InputHandler input) {
        this.game = game;
        this.input = input;
        x = 48;
        y = 48;
        xr = 5;
        yr = 4;
    }

    public void update(int t) {
        int xa = 0;
        int ya = 0;
        if(input.up) ya--;
        if(input.down) ya++;
        if(input.left) xa--;
        if(input.right) xa++;

        yt = (xa > 0 ? 11f*2 : (xa < 0 ? 9f*2 : yt));
        yt = (ya > 0 ? 10f*2 : (ya < 0 ? 8f*2 : yt));

        if (xt >= 8) xt = 0;
        xt = ((xa != 0 || ya != 0) ? xt + (t >> 1) * speed : 0);

        move(xa * t, ya * t);
    }

    public void render(Screen screen) {
        screen.render(x-16, y-24-yr*2/2, Art.i.fem_player[(int)xt][(int)yt], 0);
        screen.render(x-16, y-8-yr*2/2, Art.i.fem_player[(int)xt][(int)yt+1], 0);
    }

    public boolean findStartPos(Level level) {
        while(true) {
            int x = random.nextInt(level.w);
            int y = random.nextInt(level.h);
            if(level.getTile(x, y) == Tile.grass) {
                this.x = (x << 5) + 16;
                this.y = (y << 5) + 16;
                return true;
            }
        }
    }
}
