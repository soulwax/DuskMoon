package de.cirrus.dusk.level;

import de.cirrus.dusk.level.tile.Tile;

/**
 * Manages the 2D grid of terrain tiles and associated metadata for a level.
 */
public class LevelMap {
    public final int w;
    public final int h;
    public final byte[] tiles;
    public final byte[] data;

    public LevelMap(int w, int h) {
        this.w = w;
        this.h = h;
        this.tiles = new byte[w * h];
        this.data = new byte[w * h];
    }

    public LevelMap(int w, int h, byte[] tiles, byte[] data) {
        this.w = w;
        this.h = h;
        this.tiles = tiles != null ? tiles : new byte[w * h];
        this.data = data != null ? data : new byte[w * h];
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < w && y < h;
    }

    public Tile getTile(int x, int y) {
        if (!isInBounds(x, y)) {
            return Tile.metalPlate;
        }
        return Tile.tiles[tiles[x + y * w]];
    }

    public byte getTileId(int x, int y) {
        if (!isInBounds(x, y)) {
            return Tile.metalPlate.id;
        }
        return tiles[x + y * w];
    }

    public void setTile(int x, int y, Tile tile) {
        if (isInBounds(x, y)) {
            tiles[x + y * w] = tile.id;
        }
    }

    public void setTileId(int x, int y, byte tileId) {
        if (isInBounds(x, y)) {
            tiles[x + y * w] = tileId;
        }
    }

    public byte getData(int x, int y) {
        if (!isInBounds(x, y)) {
            return 0;
        }
        return data[x + y * w];
    }

    public void setData(int x, int y, byte val) {
        if (isInBounds(x, y)) {
            data[x + y * w] = val;
        }
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
