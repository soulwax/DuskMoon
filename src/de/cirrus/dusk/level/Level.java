package de.cirrus.dusk.level;

import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.map.LevelGen;
import de.cirrus.dusk.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

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

public class Level {
    public int w, h;
    public byte[] map;

    public List<Entity> entities = new ArrayList<>();

    public Level(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void init() {
        byte[][] levelData = LevelGen.createAndValidateTopMap(w, h);
        map = levelData[0];
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 5;
        int yo = yScroll >> 5;
        int w = (screen.w + 30) >> 5;
        int h = (screen.h + 30) >> 5;
        screen.setOffset(xScroll, yScroll);
        for(int y = yo; y <= h + yo; y++) {
            for(int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }

    public void renderEntities(Screen screen, int xScroll, int yScroll) {
        screen.setOffset(xScroll, yScroll);
        entities.stream().filter(e -> !e.removed).forEach(e -> e.render(screen));
        screen.setOffset(0, 0);
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= w || y >= h) return Tile.metalPlate;
        return Tile.tiles[map[x + y * w]];
    }

    public void update(int t) {
        entities.parallelStream().filter(e -> !e.removed).forEach(e -> e.update(t));
    }

    public void add(Entity e) {
        e.init(this);
        entities.add(e);
    }
}
