package com.botaimers.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/** Starts the graphical game window. */
public final class DesktopLauncher {
    private DesktopLauncher() {}

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Bot-Aimers | Tactical Arena");
        config.setWindowedMode(960, 640);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new TacticalGame(), config);
    }
}
