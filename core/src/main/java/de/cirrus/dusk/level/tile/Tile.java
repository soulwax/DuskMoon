package de.cirrus.dusk.level.tile;

import java.util.Random;

import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

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
    public static Tile dirt = new DirtTile(10);

    public final byte id;

    public boolean walkable = false;
    public boolean connectsToGrass = false;
    public boolean connectsToSand = false;
    public boolean connectsToLava = false;
    public boolean connectsToWater = false;

    public Tile(int id) {
        this.id = (byte) id;
        if (tiles[id] != null)
            throw new RuntimeException("Duplicate tile ids!");
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

    /**
     * Determines whether this tile at (xt, yt) blocks an entity from moving into the specified bounding box.
     *
     * @param level the level
     * @param xt tile x
     * @param yt tile y
     * @param entity the entity moving
     * @param boxX0 entity min x
     * @param boxY0 entity min y
     * @param boxX1 entity max x
     * @param boxY1 entity max y
     * @return true if movement is blocked
     */
    public boolean blocks(Level level, int xt, int yt, Entity entity, double boxX0, double boxY0, double boxX1, double boxY1) {
        if (walkable) {
            return false;
        }
        double tX0 = xt * 32.0;
        double tY0 = yt * 32.0;
        double tX1 = tX0 + 32.0;
        double tY1 = tY0 + 32.0;

        return boxX0 < tX1 && boxX1 > tX0 && boxY0 < tY1 && boxY1 > tY0;
    }

    /**
     * Renders any elevated foreground elements (e.g. tree canopies) that should draw on top of entities.
     */
    public void renderForeground(Screen screen, Level level, int x, int y) {
    }
}
