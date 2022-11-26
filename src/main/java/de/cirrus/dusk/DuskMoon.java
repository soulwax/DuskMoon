package de.cirrus.dusk;

import de.cirrus.dusk.states.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
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

public class DuskMoon extends StateBasedGame {

    //Settings
    public static final String TITLE = "Dusk Moon - by Cirrus";
    public static final int HEIGHT = 720;
    public static final int WIDTH = HEIGHT * 16 / 9;
    public static final int SCALE = 2;
    public static final int UPS = 60;
    public static final int FPS = 60;
    public static boolean fullScreen = false;

    //Game states
    public Game game;

    public DuskMoon(String title) {
        super(title);
        this.game = new Game();
        this.addState(game);
        //TODO: add more states
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
            container.setMinimumLogicUpdateInterval(1000/UPS);
            container.setMaximumLogicUpdateInterval(1000/UPS);
            container.setTargetFrameRate(FPS);
            container.setAlwaysRender(true);
            container.start();
        } catch(SlickException e) {
            throw new RuntimeException("Failed to initialize the game container."
                    + " Reason: " + e.getMessage());
        }
    }
}
