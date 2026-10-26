package de.cirrus.dusk;

import com.badlogic.gdx.Game;

public class DuskMoon extends com.badlogic.gdx.Game {

    // Settings
    public static final String TITLE = "DuskMoon";
    public static final int HEIGHT = 720;
    public static final int WIDTH = HEIGHT * 16 / 9; // 1280
    public static final int SCALE = 2;
    public static final int UPS = 60;
    public static final int FPS = 60;
    public static boolean fullScreen = false;

    // Game state
    public de.cirrus.dusk.states.Game gameState;

    public DuskMoon() {
    }

    @Override
    public void create() {
        Art.init();
        gameState = new de.cirrus.dusk.states.Game();
        setScreen(gameState);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (Art.i != null) {
            Art.i.dispose();
        }
    }
}
