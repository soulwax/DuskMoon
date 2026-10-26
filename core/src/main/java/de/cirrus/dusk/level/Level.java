package de.cirrus.dusk.level;

import java.util.ArrayList;
import java.util.List;

import de.cirrus.dusk.entities.Entity;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.map.LevelGen;
import de.cirrus.dusk.level.tile.Tile;

public class Level {
    public int w, h;
    public LevelMap levelMap;
    public DepthMap depthMap;

    // Direct array references for backward compatibility
    public byte[] map;
    public byte[] data;

    public List<Entity> entities = new ArrayList<>();

    public Level(int w, int h) {
        this.w = w;
        this.h = h;
        this.levelMap = new LevelMap(w, h);
        this.depthMap = new DepthMap(w, h);
        this.map = levelMap.tiles;
        this.data = levelMap.data;
    }

    public void init() {
        byte[][] levelData = LevelGen.createAndValidateTopMap(w, h);
        this.map = levelData[0];
        this.data = levelData[1];
        this.levelMap = new LevelMap(w, h, map, data);
        initDepthMap();
    }

    public void initDepthMap() {
        if (levelMap == null || levelMap.tiles != map || levelMap.data != data) {
            levelMap = new LevelMap(w, h, map, data);
        }
        if (depthMap == null || depthMap.w != w || depthMap.h != h) {
            depthMap = new DepthMap(w, h);
        }
        depthMap.compute(levelMap);
    }

    public float getWaterDepth(double px, double py) {
        if (levelMap == null || levelMap.tiles != map) {
            levelMap = new LevelMap(w, h, map, data);
        }
        if (depthMap == null) return 0f;
        return depthMap.getDepth(px, py, levelMap);
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 5;
        int yo = yScroll >> 5;
        int ww = (screen.w + 30) >> 5;
        int hh = (screen.h + 30) >> 5;

        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= hh + yo; y++) {
            for (int x = xo; x <= ww + xo; x++) {
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
        if (x < 0 || y < 0 || x >= w || y >= h)
            return Tile.metalPlate;
        if (map != null) {
            return Tile.tiles[map[x + y * w]];
        }
        return levelMap.getTile(x, y);
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && y >= 0 && x < w && y < h) {
            if (map != null) {
                map[x + y * w] = tile.id;
            }
            if (levelMap != null && levelMap.tiles != map) {
                levelMap.setTile(x, y, tile);
            }
        }
    }

    public void update(int t) {
        entities.parallelStream().filter(e -> !e.removed).forEach(e -> e.update(t));
    }

    public void add(Entity e) {
        e.init(this);
        entities.add(e);
    }
}
