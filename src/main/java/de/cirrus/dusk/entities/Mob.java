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
package de.cirrus.dusk.entities;

import de.cirrus.dusk.level.tile.Tile;

public class Mob extends Entity {

    /**
     * Movement direction.
     *
     * @author Maksim Chizhov &lt;maksim.chizhov@gmail.com&gt;
     */
    enum Direction {
        DOWN, UP, LEFT, RIGHT;

        public int dx() {
            return ordinal() > 1 ? 2 * ordinal() - 5 : 0;
        }

        public int dy() {
            return ordinal() < 2 ? 2 * (ordinal() & 1) - 1 : 0;
        }

        /**
         * Get direction by coordinates increment.
         * FIXME: give a proper name to this function.
         *
         * @param xa increment by x
         * @param ya increment by y
         * @return direction
         */
        public static Direction fromInc(final double xa, final double ya) {
            if (ya > 0) {
                return Direction.DOWN;
            }
            if (xa < 0) {
                return Direction.LEFT;
            }
            if (xa > 0) {
                return Direction.RIGHT;
            }
            return Direction.UP;
        }
    }

    /** The movement direction. */
    protected Direction dir = Direction.DOWN;
    protected int walkDist = 0;

    public int hurtTime = 0;
    public int maxHealth = 100;
    public int health = maxHealth;
    public boolean swimming = false;
    /** The immersion depth. */
    public float depth = 0.0f;

    public Mob() {
        x = y = 8;
        xr = 14;
        yr = 12;
    }

    @Override
    public void update(final int t) {
        if(health <= 0) {
            die();
        }

        if(hurtTime > 0) {
            hurtTime--;
        }

    }

    public void die() {
        remove();
    }

    @Override
    public boolean move(final double xa, final double ya) {

        if (xa != 0 || ya != 0) {
            walkDist++;
            dir = Direction.fromInc(xa, ya);
            if (level.getTile((int)x >> 5, (int)y >> 5) == Tile.water) {
                swimming = true;
                // The immersion mechanics. Helps smoothly walk in and out of
                // the water. 1.0f -- max depth, 0.0f -- we're on the land
                // (swimming == false).
                // TODO: implement depth map instead
                depth += (float) (getNextTile() != Tile.water && depth > 0f ? -Math.sqrt(depth) * 0.05f : getPreviousTile() != Tile.water && depth < 1f ? Math
                                        .sqrt(1 - depth) * 0.05f : 0f);
            } else {
                swimming = false;
                depth = 0f;
            }
        }
        return super.move(xa, ya);
    }

    /**
     * Get previous tile according to current movement direction.
     *
     * @return tile
     */
    protected Tile getPreviousTile() {
        return level.getTile(((int) x >> 5) - dir.dx(),
                ((int) y >> 5) + dir.dy());
    }

    /**
     * Get next tile according to current movement direction.
     *
     * @return tile
     */
    protected Tile getNextTile() {
        return level.getTile(((int) x >> 5) + dir.dx(),
                ((int) y >> 5) - dir.dy());
    }
}
