package de.cirrus.dusk.gfx;

import de.cirrus.dusk.states.Game;

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

public class Loader implements Runnable {
    private Game game;

    public byte progress = 0;
    public boolean loading;
    public Thread thread;

    public Loader(Game game) {
        this.game = game;
    }

    public synchronized void start() {
        loading = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        loading = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        game.loadState();

    }
}
