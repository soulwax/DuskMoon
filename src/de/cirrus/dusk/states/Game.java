package de.cirrus.dusk.states;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.DuskMoon;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.entities.Player;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
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
    public InputHandler input;
    public Player player;

    public Game() {
        input = new InputHandler();
        screen = new Screen(DuskMoon.WIDTH/DuskMoon.SCALE, DuskMoon.HEIGHT/DuskMoon.SCALE);
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
        player = new Player(this,input);
        player.findStartPos(level);
        level.add(player);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.scale(DuskMoon.SCALE, DuskMoon.SCALE);

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
}
