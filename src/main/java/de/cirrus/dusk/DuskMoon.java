package de.cirrus.dusk;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.cirrus.dusk.states.Game;

public class DuskMoon extends StateBasedGame {

    // Settings
    public static final String TITLE = "Codename Nightfall";
    public static final int HEIGHT = 720;
    public static final int WIDTH = HEIGHT * 16 / 9;
    public static final int SCALE = 2;
    public static final int UPS = 60;
    public static final int FPS = 60;
    public static boolean fullScreen = false;

    // Game states
    public Game game;

    public DuskMoon(String title) {
        super(title);
        this.game = new Game();
        this.addState(game);
        // TODO: add more states
    }

    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.enterState(game.getID());
    }

    public static void main(String[] args) {
        Natives.setupNativeEnv();

        AppGameContainer container;
        try {
            container = new AppGameContainer(new DuskMoon(TITLE));
            container.setDisplayMode(WIDTH, HEIGHT, fullScreen);
            container.setVerbose(false);
            container.setMinimumLogicUpdateInterval(1000 / UPS);
            container.setMaximumLogicUpdateInterval(1000 / UPS);
            container.setTargetFrameRate(FPS);
            container.setAlwaysRender(true);
            container.start();
        } catch (SlickException e) {
            throw new RuntimeException("Failed to initialize the game container."
                    + " Reason: " + e.getMessage());
        }
    }
}
