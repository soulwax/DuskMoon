package de.cirrus.dusk.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import de.cirrus.dusk.gdx.input.GameInputState;
import de.cirrus.dusk.gdx.input.XboxControllerAdapter;

public class PlayScreen implements Screen {
    private final GameInputState inputState;
    private final XboxControllerAdapter controllerAdapter;

    public PlayScreen(GameInputState inputState, XboxControllerAdapter controllerAdapter) {
        this.inputState = inputState;
        this.controllerAdapter = controllerAdapter;
    }

    @Override
    public void render(float delta) {
        inputState.beginFrame();

        int keyX = 0;
        int keyY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) keyX -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) keyX += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) keyY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) keyY -= 1;

        boolean keyboardAttack = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        controllerAdapter.poll();

        int mergedX = keyX != 0 ? Integer.signum(keyX) : inputState.moveX;
        int mergedY = keyY != 0 ? Integer.signum(keyY) : inputState.moveY;
        inputState.setMove(mergedX, mergedY);
        inputState.setAttackHeld(inputState.attackHeld || keyboardAttack);

        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
