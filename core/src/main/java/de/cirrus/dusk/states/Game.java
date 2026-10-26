package de.cirrus.dusk.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.cirrus.dusk.DuskMoon;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.entities.Player;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class Game implements com.badlogic.gdx.Screen {
    public Level level;
    public Screen screen;
    public InputHandler input;
    public Player player;
    public static int gameTempo = 3;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float accumulator = 0f;

    public Game() {
        input = new InputHandler();
        int viewW = DuskMoon.WIDTH / DuskMoon.SCALE;
        int viewH = DuskMoon.HEIGHT / DuskMoon.SCALE;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, viewW, viewH);
        viewport = new FitViewport(viewW, viewH, camera);

        screen = new Screen(batch, viewW, viewH);
        level = new Level(128, 128);
        level.init();
        resetGame();
    }

    public void resetGame() {
        player = new Player(this, input);
        player.findStartPos(level);
        level.add(player);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        input.update();

        if (input.plus)
            gameTempo++;
        if (input.minus)
            gameTempo--;
        if (gameTempo < 1)
            gameTempo = 1;
        if (gameTempo > 10)
            gameTempo = 10;

        // Fixed timestep update (60 UPS)
        accumulator += Math.min(delta, 0.25f);
        float step = 1f / DuskMoon.UPS;
        while (accumulator >= step) {
            int t = 16;
            level.update(t >> (6 / gameTempo));
            accumulator -= step;
        }

        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        int xScroll = (int) player.x - screen.w / 2;
        int yScroll = (int) player.y - (screen.h - 16) / 2;
        if (xScroll < 32)
            xScroll = 32;
        if (yScroll < 32)
            yScroll = 32;
        if (xScroll > level.w * 32 - screen.w - 32)
            xScroll = level.w * 32 - screen.w - 32;
        if (yScroll > level.h * 32 - screen.h - 32)
            yScroll = level.h * 32 - screen.h - 32;

        batch.begin();
        level.renderBackground(screen, xScroll, yScroll);
        level.renderEntities(screen, xScroll, yScroll);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
