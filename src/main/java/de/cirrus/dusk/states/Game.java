package de.cirrus.dusk.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.DuskMoon;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.entities.Player;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;

public class Game extends BasicGameState {
    public Level level;
    public Screen screen;
    public InputHandler input;
    public Player player;
    public static int gameTempo = 3;

    public Game() {
        input = new InputHandler();
        screen = new Screen(DuskMoon.WIDTH / DuskMoon.SCALE, DuskMoon.HEIGHT / DuskMoon.SCALE);
        level = new Level(128, 128);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gc.getInput().addMouseListener(input);
        gc.getInput().addKeyListener(input);

        Art.init();
        level.init();
        resetGame();
    }

    public void resetGame() {
        player = new Player(this, input);
        player.findStartPos(level);
        level.add(player);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.scale(DuskMoon.SCALE, DuskMoon.SCALE);

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
        level.renderBackground(screen, xScroll, yScroll);
        level.renderEntities(screen, xScroll, yScroll);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int t) throws SlickException {
        if (input.plus)
            gameTempo++;
        if (input.minus)
            gameTempo--;
        if (gameTempo < 1)
            gameTempo = 1;
        if (gameTempo > 10)
            gameTempo = 10;
        level.update(t >> (6 / gameTempo));
    }

    public int getID() {
        return 1;
    }
}
