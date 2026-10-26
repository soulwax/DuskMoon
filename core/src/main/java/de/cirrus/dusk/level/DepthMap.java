package de.cirrus.dusk.level;

import java.util.ArrayDeque;
import java.util.Queue;

import de.cirrus.dusk.level.tile.Tile;

/**
 * Calculates and stores a continuous distance-field water depth map.
 * Supports smooth bilinear interpolation at sub-pixel coordinates for realistic swimming immersion.
 */
public class DepthMap {
    public final int w;
    public final int h;
    public final int cw;
    public final int ch;
    public final float[] values;

    public DepthMap(int w, int h) {
        this.w = w;
        this.h = h;
        this.cw = w + 1;
        this.ch = h + 1;
        this.values = new float[cw * ch];
    }

    /**
     * Computes the shoreline distance field from the provided level map.
     * Uses multi-source BFS starting from water-land boundary corners.
     */
    public void compute(LevelMap levelMap) {
        float[] dist = new float[cw * ch];
        Queue<Integer> queue = new ArrayDeque<>();

        // Identify boundary corners and mark unvisited submerged corners
        for (int cy = 0; cy < ch; cy++) {
            for (int cx = 0; cx < cw; cx++) {
                int idx = cx + cy * cw;
                boolean touchesLand = false;
                boolean touchesWater = false;

                // Check the 4 adjacent tiles around corner (cx, cy)
                for (int dy = -1; dy <= 0; dy++) {
                    for (int dx = -1; dx <= 0; dx++) {
                        int tx = cx + dx;
                        int ty = cy + dy;
                        if (!levelMap.isInBounds(tx, ty)) {
                            touchesLand = true;
                        } else {
                            if (levelMap.getTile(tx, ty) == Tile.water) {
                                touchesWater = true;
                            } else {
                                touchesLand = true;
                            }
                        }
                    }
                }

                if (touchesWater && touchesLand) {
                    // Shoreline corner: right at the edge of water and land
                    dist[idx] = 0f;
                    queue.add(idx);
                } else if (touchesWater) {
                    // Completely submerged corner: distance to be calculated
                    dist[idx] = -1f;
                } else {
                    // Land corner
                    dist[idx] = 0f;
                }
            }
        }

        // Multi-source distance propagation (8-way connected grid)
        int[] dX = {-1, 1,  0, 0, -1,  1, -1, 1};
        int[] dY = { 0, 0, -1, 1, -1, -1,  1, 1};
        float[] dD = {1f, 1f, 1f, 1f, 1.4142f, 1.4142f, 1.4142f, 1.4142f};

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            int cx = curr % cw;
            int cy = curr / cw;
            float currentDist = dist[curr];

            for (int i = 0; i < 8; i++) {
                int nx = cx + dX[i];
                int ny = cy + dY[i];
                if (nx >= 0 && nx < cw && ny >= 0 && ny < ch) {
                    int nIdx = nx + ny * cw;
                    if (dist[nIdx] < 0f) {
                        dist[nIdx] = currentDist + dD[i];
                        queue.add(nIdx);
                    }
                }
            }
        }

        // Convert distance to normalized depth [0.0 .. 1.0]
        // Full swimming depth (1.0) is reached ~1.75 tiles out from shore.
        float maxDepthDist = 1.75f;
        for (int i = 0; i < values.length; i++) {
            float d = Math.max(0f, dist[i]);
            float norm = Math.min(1.0f, d / maxDepthDist);
            // Smoothstep curve for a realistic, natural underwater gradient
            values[i] = norm * norm * (3f - 2f * norm);
        }
    }

    /**
     * Samples the water depth at arbitrary pixel coordinates (px, py).
     * Returns a smoothly interpolated value between 0.0 (shore/land) and 1.0 (deep water).
     */
    public float getDepth(double px, double py, LevelMap levelMap) {
        float tx = (float) px / 32f;
        float ty = (float) py / 32f;
        if (tx < 0 || ty < 0 || tx >= w || ty >= h) {
            return 0f;
        }

        int x0 = (int) Math.floor(tx);
        int y0 = (int) Math.floor(ty);
        int x1 = Math.min(x0 + 1, w);
        int y1 = Math.min(y0 + 1, h);

        float fx = tx - x0;
        float fy = ty - y0;

        float d00 = values[x0 + y0 * cw];
        float d10 = values[x1 + y0 * cw];
        float d01 = values[x0 + y1 * cw];
        float d11 = values[x1 + y1 * cw];

        float d0 = d00 * (1f - fx) + d10 * fx;
        float d1 = d01 * (1f - fx) + d11 * fx;
        float depth = d0 * (1f - fy) + d1 * fy;

        if (levelMap != null && levelMap.getTile((int) px >> 5, (int) py >> 5) == Tile.water) {
            return Math.max(0.08f, Math.min(1.0f, depth));
        }
        return Math.max(0f, Math.min(1.0f, depth));
    }
}
