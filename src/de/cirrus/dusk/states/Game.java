package de.cirrus.dusk.states;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.DuskMoon;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.entities.Player;
import de.cirrus.dusk.gfx.Loader;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * DuskMoon
 * Copyright (C) 2014 by Cirrus
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * -
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * -
 * Contact: cirrus.contact@t-online.de
 */

public class Game extends BasicGameState {
    public Level level;
    public Screen screen;
    public Loader loader;
    public InputHandler input;
    public Player player;

    public boolean loaded;

    public Game() {
        input = new InputHandler();
        screen = new Screen(DuskMoon.WIDTH, DuskMoon.HEIGHT);
        loader = new Loader(this);
        level = new Level(128, 128);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gc.getInput().addMouseListener(input);
        gc.getInput().addKeyListener(input);
        loaded = false;
    }

    public void loadState() {


    }

    public void resetGame() {
        player = new Player(this,input);
        player.findStartPos(level);
        level.add(player);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if(!loaded) {
            long lastTime = System.currentTimeMillis();
            loader.progress = 10;
            renderLoadingScreen(g);
            Art.init();
            loader.progress = 40;
            renderLoadingScreen(g);
            level.init();
            loader.progress = 75;
            renderLoadingScreen(g);
            resetGame();
            loader.progress = 100;
            renderLoadingScreen(g);
            long passedTime = System.currentTimeMillis() - lastTime;
            System.out.println("loading took " + ((double)passedTime)/1000D + " seconds.");
            loaded = true;
        }


        int xScroll = player.x - screen.w / 2;
        int yScroll = player.y - (screen.h - 16) / 2;
        if (xScroll < 32) xScroll = 32;
        if (yScroll < 32) yScroll = 32;
        if (xScroll > level.w * 32 - screen.w - 32) xScroll = level.w * 32 - screen.w - 32;
        if (yScroll > level.h * 32 - screen.h - 32) yScroll = level.h * 32 - screen.h - 32;

        level.renderBackground(screen, xScroll, yScroll);
        level.renderEntities(screen, xScroll, yScroll);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int t) throws SlickException {
        level.update(t >> 3);
    }

    public int getID() {
        return 1;
    }

    public void renderLoadingScreen(Graphics g) {
        int w = screen.w;
        int h = screen.h;
        System.out.println("render loading screen");
        g.setColor(Color.white);
        g.fillRect(30, h / 2 - 15, (w - 2 * 30) * loader.progress / 100, 30);
    }
}
