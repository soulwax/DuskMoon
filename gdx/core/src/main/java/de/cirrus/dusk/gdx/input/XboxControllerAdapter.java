package de.cirrus.dusk.gdx.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class XboxControllerAdapter {
    private static final int BUTTON_A = 0;
    private static final int AXIS_LEFT_X = 0;
    private static final int AXIS_LEFT_Y = 1;
    private static final float DEADZONE = 0.2f;

    private final GameInputState inputState;

    public XboxControllerAdapter(GameInputState inputState) {
        this.inputState = inputState;
    }

    public void poll() {
        Controller controller = Controllers.getCurrent();
        if (controller == null) {
            inputState.setMove(0, 0);
            inputState.setAttackHeld(false);
            return;
        }

        float rawX = controller.getAxis(AXIS_LEFT_X);
        float rawY = controller.getAxis(AXIS_LEFT_Y);

        int moveX = quantizeAxis(rawX);
        int moveY = quantizeAxis(-rawY); // invert for screen-space consistency

        inputState.setMove(moveX, moveY);
        inputState.setAttackHeld(controller.getButton(BUTTON_A));
    }

    private int quantizeAxis(float v) {
        if (Math.abs(v) < DEADZONE) {
            return 0;
        }
        return v > 0f ? 1 : -1;
    }
}
