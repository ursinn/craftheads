package dev.ursinn.minecraft.craftheads.bukkit.utils;

import me.deejayarroba.craftheads.Main;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileHelper {

    private final Main mainInstance;

    public FileHelper() {
        mainInstance = Main.getInstance();
    }

    public void extractFiles(String dir) {
        try {
            JarFile jarfile = new JarFile(mainInstance.getPluginFile());

            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(dir) && !dir.equals(name)) {
                    mainInstance.saveResource(name, true);
                }
            }
            jarfile.close();
        } catch (IOException e) {
            mainInstance.getLogger().warning(String.valueOf(e));
        }
    }
}
