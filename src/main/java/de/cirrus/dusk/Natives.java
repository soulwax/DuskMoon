package de.cirrus.dusk;

import java.io.File;

public class Natives {
    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("OS: " + os);
        return os;
    }

    public static void setupNativeEnv() {
        String os = getOS();
        switch (os) {
            case "linux":
                System.setProperty("org.lwjgl.librarypath", new File("lib/natives/linux").getAbsolutePath());
                System.setProperty("net.java.games.input.librarypath", new File("lib/natives/linux").getAbsolutePath());
                break;
            case "windows":
                System.setProperty("org.lwjgl.librarypath", new File("lib/natives/windows").getAbsolutePath());
                System.setProperty("net.java.games.input.librarypath", new File("lib/natives/windows").getAbsolutePath());
                break;
            case "mac":
                System.setProperty("org.lwjgl.librarypath", new File("lib/natives/macosx").getAbsolutePath());
                System.setProperty("net.java.games.input.librarypath", new File("lib/natives/macosx").getAbsolutePath());
                break;
            case "solaris":
                System.setProperty("org.lwjgl.librarypath", new File("lib/natives/solaris").getAbsolutePath());
                System.setProperty("net.java.games.input.librarypath", new File("lib/natives/solaris").getAbsolutePath());
                break;
            case "freebsd":
                System.setProperty("org.lwjgl.librarypath", new File("lib/natives/freebsd").getAbsolutePath());
                System.setProperty("net.java.games.input.librarypath", new File("lib/natives/freebsd").getAbsolutePath());
                break;
            default:
                System.out.println("OS not supported!");
                System.exit(1);
        }
    }
}
