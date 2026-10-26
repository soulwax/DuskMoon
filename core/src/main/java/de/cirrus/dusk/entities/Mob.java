package de.cirrus.dusk.entities;

import de.cirrus.dusk.level.tile.Tile;

public class Mob extends Entity {

    /**
     * Movement direction.
     *
     * @author Maksim Chizhov &lt;maksim.chizhov@gmail.com&gt;
     */
    public enum Direction {
        DOWN, UP, LEFT, RIGHT;

        public int dx() {
            return ordinal() > 1 ? 2 * ordinal() - 5 : 0;
        }

        public int dy() {
            return ordinal() < 2 ? 2 * (ordinal() & 1) - 1 : 0;
        }

        /**
         * Get direction by coordinates increment.
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
    /** The immersion depth [0.0 = shore/land, 1.0 = deep water]. */
    public float depth = 0.0f;

    public Mob() {
        x = y = 8;
        xr = 14;
        yr = 12;
    }

    @Override
    public void update(final int t) {
        if (health <= 0) {
            die();
        }

        if (hurtTime > 0) {
            hurtTime--;
        }

        updateSwimming();
    }

    /**
     * Evaluates spatial water immersion depth based on the level's continuous depth map.
     */
    public void updateSwimming() {
        if (level != null && level.getTile((int) x >> 5, (int) y >> 5) == Tile.water) {
            swimming = true;
            depth = level.getWaterDepth(x, y);
        } else {
            swimming = false;
            depth = 0f;
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
        }
        boolean moved = super.move(xa, ya);
        updateSwimming();
        return moved;
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
