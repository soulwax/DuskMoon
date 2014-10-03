package de.cirrus.dusk.entities;

import de.cirrus.dusk.level.tile.Tile;

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

public class Mob extends Entity {

    protected int dir = 0;
    protected int walkDist = 0;

    public int hurtTime = 0;
    public int maxHealth = 100;
    public int health = maxHealth;
    public boolean swimming = false;

    public Mob() {
        x = y = 8;
        xr = 14;
        yr = 12;
    }

    public void update(int t) {
        if(health <= 0) die();

        if(hurtTime > 0) hurtTime--;

        if(level.getTile((int)x >> 5, (int)y >> 5) == Tile.water) swimming = true;
        else swimming = false;
    }

    public void die() {
        remove();
    }

    public boolean move(double xa, double ya) {

        if(xa != 0 || ya != 0) {
            walkDist++;
            if(ya < 0) dir = 1;
            if(ya > 0) dir = 0;
            if(xa < 0) dir = 2;
            if(xa > 0) dir = 3;
        }

        return super.move(xa, ya);
    }


}
