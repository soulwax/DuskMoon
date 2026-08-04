package de.cirrus.dusk.gdx;

import com.badlogic.gdx.Game;
import de.cirrus.dusk.gdx.input.GameInputState;
import de.cirrus.dusk.gdx.input.XboxControllerAdapter;
import de.cirrus.dusk.gdx.screens.PlayScreen;

public class GameApp extends Game {
    private final GameInputState inputState = new GameInputState();
    private XboxControllerAdapter controllerAdapter;

    @Override
    public void create() {
        controllerAdapter = new XboxControllerAdapter(inputState);
        setScreen(new PlayScreen(inputState, controllerAdapter));
    }

    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }
    }
}
