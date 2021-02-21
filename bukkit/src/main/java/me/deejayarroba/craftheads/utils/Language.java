package me.deejayarroba.craftheads.utils;

import me.deejayarroba.craftheads.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Language {

    private final FileConfiguration languageConfig;
    private final File languageFile;
    private final Main mainInstance;

    public Language() {
        languageConfig = new YamlConfiguration();
        languageFile = new File(Main.getInstance().getDataFolder() + "/lang",
                Main.getInstance().getConfig().getString("language", "en") + ".yml");
        mainInstance = Main.getInstance();
    }

    public void createLanguageFile() {
        if (!languageFile.exists()) {
            extractFiles();
        }

        load();
    }

    private void extractFiles() {
        JarFile jarfile;
        try {
            jarfile = new JarFile(mainInstance.getPluginFile());

            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("lang/") && !"lang/".equals(name)) {
                    mainInstance.saveResource(name, false);
                }
            }
            jarfile.close();
        } catch (IOException e) {
            mainInstance.getLogger().warning(String.valueOf(e));
        }
    }

    private void load() {
        try {
            languageConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            mainInstance.getLogger().warning(String.valueOf(e));
        }
    }

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }
}
