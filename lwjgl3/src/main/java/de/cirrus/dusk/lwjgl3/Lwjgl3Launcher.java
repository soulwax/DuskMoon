package de.cirrus.dusk.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.cirrus.dusk.DuskMoon;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new DuskMoon(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle(DuskMoon.TITLE);
        configuration.useVsync(true);
        configuration.setForegroundFPS(DuskMoon.FPS);
        configuration.setWindowedMode(DuskMoon.WIDTH, DuskMoon.HEIGHT);
        configuration.setWindowIcon("logo.png");
        return configuration;
    }
}
