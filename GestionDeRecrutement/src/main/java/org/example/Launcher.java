package org.example;

/**
 * Launcher wrapper — does NOT extend Application.
 * This avoids the "JavaFX runtime components are missing" error
 * when running a fat JAR, because the JVM only checks for JavaFX
 * modules when the main class directly extends Application.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
