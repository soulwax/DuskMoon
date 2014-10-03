package de.cirrus.dusk;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

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



public class InputHandler implements MouseListener, KeyListener {

    public float x = 0;
    public float y = 0;
    public float xStart = 0;
    public float yStart = 0;
    public float xEnd = 0;
    public float yEnd = 0;

    public boolean mbl = false;
    public boolean mbr = false;
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;
    public boolean space = false;
    public boolean lshift = false;
    public boolean plus = false;
    public boolean minus = false;

    public boolean dragged = false;
    public boolean justPressed = false;

    public InputHandler() {
    }

    public void toggle(int keyCode, boolean isPressed) {
        if (keyCode == 0) {
            mbl = isPressed;
        }
        if (keyCode == 1) {
            mbr = isPressed;
        }
        if (keyCode == Input.KEY_UP) {
            up = isPressed;
        }
        if (keyCode == Input.KEY_DOWN) {
            down = isPressed;
        }
        if (keyCode == Input.KEY_LEFT) {
            left = isPressed;
        }
        if (keyCode == Input.KEY_RIGHT) {
            right = isPressed;
        }

        if (keyCode == Input.KEY_SPACE) {
            space = isPressed;
        }

        if (keyCode == Input.KEY_LSHIFT) {
            lshift = isPressed;
        }

        if(keyCode == Input.KEY_ADD) {
            plus = isPressed;
        }

        if(keyCode == Input.KEY_SUBTRACT) {
            minus = isPressed;
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        toggle(button, true);
        dragged = false;
        this.justPressed = true;
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        toggle(button, true);
        this.xStart = x;
        this.yStart = y;
        this.justPressed = true;
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        toggle(button, false);
        this.xEnd = x;
        this.yEnd = y;
        this.justPressed = false;
        dragged = false;
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.x = newx;
        this.y = newy;
        dragged = false;
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

        //if(Math.abs(xStart - newx) < 1 && Math.abs(yStart - newy) < 1) dragged = false;

        this.x = newx;
        this.y = newy;
        this.justPressed = false;
        dragged = true;
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }

    @Override
    public void keyPressed(int key, char c) {
        toggle(key, true);
    }

    @Override
    public void keyReleased(int key, char c) {
        toggle(key, false);
    }
}