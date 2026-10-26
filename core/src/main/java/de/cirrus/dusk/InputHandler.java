package de.cirrus.dusk;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputHandler implements InputProcessor {

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

    // Internal keyboard states
    private boolean kLeft, kRight, kUp, kDown, kSpace, kShift, kPlus, kMinus;

    public InputHandler() {
    }

    public void update() {
        pollController();
    }

    private void pollController() {
        boolean cLeft = false;
        boolean cRight = false;
        boolean cUp = false;
        boolean cDown = false;
        boolean cSpace = false;

        Controller controller = Controllers.getCurrent();
        if (controller != null) {
            float axisX = controller.getAxis(0);
            float axisY = controller.getAxis(1);
            float deadzone = 0.25f;

            if (axisX < -deadzone) cLeft = true;
            if (axisX > deadzone) cRight = true;
            if (axisY < -deadzone) cUp = true;
            if (axisY > deadzone) cDown = true;

            // Button A (usually button index 0 on standard controllers)
            if (controller.getButton(0)) {
                cSpace = true;
            }
        }

        left = kLeft || cLeft;
        right = kRight || cRight;
        up = kUp || cUp;
        down = kDown || cDown;
        space = kSpace || cSpace;
        lshift = kShift;
        plus = kPlus;
        minus = kMinus;
    }

    @Override
    public boolean keyDown(int keycode) {
        setKey(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        setKey(keycode, false);
        return true;
    }

    private void setKey(int keycode, boolean isPressed) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.W:
                kUp = isPressed;
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                kDown = isPressed;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                kLeft = isPressed;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                kRight = isPressed;
                break;
            case Input.Keys.SPACE:
                kSpace = isPressed;
                break;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                kShift = isPressed;
                break;
            case Input.Keys.PLUS:
            case Input.Keys.NUMPAD_ADD:
            case Input.Keys.EQUALS:
                kPlus = isPressed;
                break;
            case Input.Keys.MINUS:
            case Input.Keys.NUMPAD_SUBTRACT:
                kMinus = isPressed;
                break;
            default:
                break;
        }
        pollController();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) mbl = true;
        if (button == Input.Buttons.RIGHT) mbr = true;
        xStart = screenX;
        yStart = screenY;
        justPressed = true;
        dragged = false;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) mbl = false;
        if (button == Input.Buttons.RIGHT) mbr = false;
        xEnd = screenX;
        yEnd = screenY;
        justPressed = false;
        dragged = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        x = screenX;
        y = screenY;
        dragged = true;
        justPressed = false;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        x = screenX;
        y = screenY;
        dragged = false;
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return touchUp(screenX, screenY, pointer, button);
    }
}
