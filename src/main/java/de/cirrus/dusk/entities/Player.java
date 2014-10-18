package de.cirrus.dusk.entities;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
import de.cirrus.dusk.level.tile.Tile;
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

public class Player extends Mob {
    private float xt, yt;
    private int attackTime, attackDir;
    private double tickCount = 0;


    public static final int ATTACK_CD = 20;

    public Game game;
    public InputHandler input;
    public static float speed = .25f;

    public Player(Game game, InputHandler input) {
        this.game = game;
        this.input = input;
        x = 48;
        y = 48;
        xr = 5;
        yr = 4;
    }

    public void update(int t) {
        super.update(t);
        xa = 0;
        ya = 0;
        if(input.up) ya--;
        if(input.down) ya++;
        if(input.left) xa--;
        if(input.right) xa++;

        if(input.space && attackTime == 0 && !swimming) {
            attack();
        }


        if(attackTime > 0) {
            attackTime--;
        }
        if(attackTime <= ATTACK_CD/2)
            move(xa * t * speed, ya * t * speed);

        tickCount+=t*0.01d;
    }

    private void attack() {
        attackTime = ATTACK_CD;
        attackDir = dir;
        xt = 0;
    }

    public void render(Screen screen) {
        if(!swimming) screen.renderShadow((int)x - 16, (int)y - 24 - yr, Art.i.fem_player[(int) xt][(int) yt], 0);
        if(attackTime > 0) {
            if(attackTime == ATTACK_CD) xt = 0;
            if(attackDir == 0) yt = 2;
            if(attackDir == 1) yt = 0;
            if(attackDir == 2) yt = 1;
            if(attackDir == 3) yt = 3;

            if(attackTime*attackTime % (ATTACK_CD/5) == 0) xt+=1;
            if(xt > 5) xt = 5;
            screen.render((int)x-48, (int)y-24-32-yr, Art.i.fem_player_attack[(int)xt][(int)yt], 0);
            yt = (attackDir == 0 ? 2 : attackDir == 1 ? 0 : attackDir == 2 ? 1 : 3);
        } else {
            yt = (xa > 0 ? 11f : (xa < 0 ? 9f : yt));
            yt = (ya > 0 ? 10f : (ya < 0 ? 8f : yt));

            if (xt >= 8) xt = 0;
            xt = ((xa != 0 || ya != 0) ? xt + speed : 0);

            if(swimming) {
                float scale = (float)Math.abs(Math.sin(tickCount)) + 1f;
                screen.renderScaled((int) (x - 16 * scale + 0.1 * scale), (int) (y - 32 * scale + 6 - yr), Art.i.fem_player[7][0], scale, 0);
                screen.renderSubImage((int) x - 16, (int) y - 20 - yr, Art.i.fem_player[(int) xt][(int) yt], 0, 12, 0);
            }
            else screen.render((int)x-16, (int)y-24-yr, Art.i.fem_player[(int)xt][(int)yt], 0);
        }
    }

    public boolean findStartPos(Level level) {
        while(true) {
            int x = random.nextInt(level.w);
            int y = random.nextInt(level.h);
            if(level.getTile(x, y) == Tile.grass) {
                this.x = (x << 5) + 16;
                this.y = (y << 5) + 16;
                return true;
            }
        }
    }
}
