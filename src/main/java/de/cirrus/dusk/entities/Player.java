package de.cirrus.dusk.entities;

import de.cirrus.dusk.Art;
import de.cirrus.dusk.InputHandler;
import de.cirrus.dusk.gfx.Screen;
import de.cirrus.dusk.level.Level;
import de.cirrus.dusk.level.tile.Tile;
import de.cirrus.dusk.states.Game;

public class Player extends Mob {

    public static final int ATTACK_CD = 20;
    /** The player texture scale. */
    public static final float SCALE = .5f;

    public static float speed = .25f;

    /** The Constant ripples frequency. */
    private static final float RIPPLES_FREQUENCY = 2f;

    private float xt, yt;
    private int attackTime;
    private Direction attackDir;
    private double tickCount = 0;

    public Game game;
    public InputHandler input;

    public Player(Game game, InputHandler input) {
        this.game = game;
        this.input = input;
        x = 48;
        y = 48;
        xr = 5;
        yr = 4;
    }

    @Override
    public void update(int t) {
        super.update(t);
        xa = 0;
        ya = 0;
        if (input.up)
            ya--;
        if (input.down)
            ya++;
        if (input.left)
            xa--;
        if (input.right)
            xa++;

        if (input.space && attackTime == 0 && !swimming) {
            attack();
        }

        if (attackTime > 0) {
            attackTime--;
        }
        if (attackTime <= ATTACK_CD / 2)
            move(xa * t * speed, ya * t * speed);

        tickCount += t * 0.01d;
        // Loop it
        if (tickCount > RIPPLES_FREQUENCY) {
            tickCount = 0;
        }
    }

    private void attack() {
        attackTime = ATTACK_CD;
        attackDir = dir;
        xt = 0;
    }

    @Override
    public void render(Screen screen) {
        renderRipples(screen);
        renderShadow(screen);
        if (attackTime > 0) {
            if (attackTime == ATTACK_CD)
                xt = 0;
            if (attackDir == Direction.UP) {
                yt = 0;
            }
            if (attackDir == Direction.DOWN) {
                yt = 2;
            }
            if (attackDir == Direction.LEFT) {
                yt = 1;
            }
            if (attackDir == Direction.RIGHT) {
                yt = 3;
            }

            if (attackTime * attackTime % (ATTACK_CD / 5) == 0)
                xt += 1;
            if (xt > 5)
                xt = 5;
            screen.render((int) x - 48, (int) y - 24 - 32 - yr, Art.i.fem_player_attack[(int) xt][(int) yt],
                    Player.SCALE, 0);
            yt = attackDir == Direction.DOWN ? 2 : attackDir == Direction.UP ? 0 : attackDir == Direction.LEFT ? 1 : 3;
        } else {
            yt = (xa > 0 ? 11f : (xa < 0 ? 9f : yt));
            yt = (ya > 0 ? 10f : (ya < 0 ? 8f : yt));

            if (xt >= 8)
                xt = 0;
            xt = ((xa != 0 || ya != 0) ? xt + speed : 0);

            if (swimming) {
                // Body top
                screen.renderSubImage((int) x - 16, (int) y - 20 - (int) (4 * (1 - depth)) - yr,
                        Art.i.fem_player[(int) xt][(int) yt], 0, 0, 0, (int) (12 * depth), Player.SCALE, 0);
                // Body bottom
                screen.renderSubImage((int) x - 16, (int) y + 5 - (int) (4 * (1 - depth)) - yr,
                        Art.i.fem_player[(int) xt][(int) yt], 0, 50, 0, 50, Player.SCALE, 0.3f, 0);
            } else {
                screen.render((int) x - 16, (int) y - 24 - yr, Art.i.fem_player[(int) xt][(int) yt], Player.SCALE, 0);
            }
        }
    }

    /**
     * Render shadow.
     *
     * @param screen the screen
     */
    private void renderShadow(final Screen screen) {
        if (!swimming) {
            screen.renderShadow((int) x - 58, (int) y - 54 - yr, Art.i.fem_player[(int) xt][(int) yt], Player.SCALE, 0);
        } else {
            screen.renderSubImageShadow((int) x - 58, (int) y - 50 - (int) (4 * (1 - depth)) - yr,
                    Art.i.fem_player[(int) xt][(int) yt], 0, (int) (26 * depth),
                    Player.SCALE, 0);
        }
    }

    /**
     * Render water ripples.
     *
     * @param screen the screen
     */
    private void renderRipples(final Screen screen) {
        // Do not render ripples until we get deep enough.
        if (!swimming || depth < 0.4f) {
            return;
        }
        float scale = (float) tickCount;
        float alpha = (float) (((RIPPLES_FREQUENCY - tickCount) / RIPPLES_FREQUENCY + 0.75f) / 1.5f);
        screen.render((int) (x - 16 * scale + 0.1 * scale), (int) (y - 28 * scale + 6 - yr), Art.i.fem_player[7][0],
                scale * Player.SCALE, alpha, 0);
        scale += RIPPLES_FREQUENCY;
        alpha = (float) ((RIPPLES_FREQUENCY - tickCount) / RIPPLES_FREQUENCY) * 0.5f;
        screen.render((int) (x - 16 * scale + 0.1 * scale), (int) (y - 28 * scale + 6 - yr), Art.i.fem_player[7][0],
                scale * Player.SCALE, alpha, 0);
    }

    public boolean findStartPos(Level level) {
        while (true) {
            int x = random.nextInt(level.w);
            int y = random.nextInt(level.h);
            if (level.getTile(x, y) == Tile.grass) {
                this.x = (x << 5) + 16;
                this.y = (y << 5) + 16;
                return true;
            }
        }
    }
}
