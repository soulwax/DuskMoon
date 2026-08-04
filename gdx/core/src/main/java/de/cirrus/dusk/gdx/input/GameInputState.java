package de.cirrus.dusk.gdx.input;

public class GameInputState {
    public int moveX;
    public int moveY;
    public boolean attackPressedThisFrame;
    public boolean attackHeld;

    public void beginFrame() {
        attackPressedThisFrame = false;
    }

    public void setMove(int x, int y) {
        moveX = clampToSign(x);
        moveY = clampToSign(y);
    }

    public void setAttackHeld(boolean held) {
        if (held && !attackHeld) {
            attackPressedThisFrame = true;
        }
        attackHeld = held;
    }

    private int clampToSign(int v) {
        if (v > 0) return 1;
        if (v < 0) return -1;
        return 0;
    }
}
