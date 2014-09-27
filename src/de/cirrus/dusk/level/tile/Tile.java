package de.cirrus.dusk.level.tile;

import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

import java.util.Random;

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

public class Tile {
    protected Random random = new Random();

    public static Tile[] tiles = new Tile[256];
    public static Tile metalPlate = new MetalPlate(0);
    public static Tile grass = new GrassTile(1);
    public static Tile sand = new SandTile(2);
    public static Tile water = new WaterTile(3);
    public static Tile lava = new LavaTile(4);
    public static Tile tree = new TreeTile(5);
    public static Tile rock = new RockTile(6);
    public static Tile flower = new FlowerTile(7);
    public static Tile cactus = new CactusTile(8);
    public static Tile stairsDown = new StairsDown(9);

    public final byte id;

    public boolean walkable = false;
    public boolean connectsToGrass = false;
    public boolean connectsToSand = false;
    public boolean connectsToLava = false;
    public boolean connectsToWater = false;

    public Tile(int id) {
        this.id = (byte)id;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
        tiles[id] = this;
    }

    public void bumpedInto(Level level, int xt, int yt, Entity entity) {
    }

    public void steppedOn(Level level, int xt, int yt, Entity entity) {
    }

    public void update(Level level, int xt, int yt) {
    }

    public void render(Screen screen, Level level, int x, int y) {
    }
}
