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
}
