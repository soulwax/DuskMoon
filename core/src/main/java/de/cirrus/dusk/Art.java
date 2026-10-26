package de.cirrus.dusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class Art implements Disposable {
    public static Art i;

    public static void init() {
        if (i != null) {
            i.dispose();
        }
        i = new Art();
    }

    private final List<Texture> textures = new ArrayList<>();

    public TextureRegion[][] tileset;
    public TextureRegion[][] fem_player;
    public TextureRegion[][] fem_player_attack;

    public Art() {
        tileset = load("tileset.png", 16, 16);
        fem_player = load("fem_blond_general.png", 64, 64);
        fem_player_attack = load("fem_blond_attack.png", 192, 192);
    }

    private TextureRegion[][] load(String path, int sw, int sh) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textures.add(texture);
        return cutImage(texture, sw, sh);
    }

    private TextureRegion[][] cutImage(Texture texture, int sw, int sh) {
        int xSlices = texture.getWidth() / sw;
        int ySlices = texture.getHeight() / sh;

        TextureRegion[][] result = new TextureRegion[xSlices][ySlices];

        for (int x = 0; x < xSlices; x++) {
            for (int y = 0; y < ySlices; y++) {
                TextureRegion region = new TextureRegion(texture, x * sw, y * sh, sw, sh);
                region.flip(false, true);
                result[x][y] = region;
            }
        }

        return result;
    }

    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }
}
