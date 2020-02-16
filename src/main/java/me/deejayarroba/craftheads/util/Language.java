package me.deejayarroba.craftheads.util;

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

    private FileConfiguration languageConfig;

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public void createLanguageFile() {
        File languageFile = new File(Main.getInstance().getDataFolder() + "/lang", Main.getInstance().getConfig().getString("language", "en") + ".yml");
        if (!languageFile.exists()) {
            JarFile jarfile;
            try {
                jarfile = new JarFile(Main.getInstance().getPluginFile());

                Enumeration<JarEntry> entries = jarfile.entries();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith("lang/") && !name.equals("lang/")) {
                        Main.getInstance().saveResource(name, false);
                    }
                }
                jarfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        languageConfig = new YamlConfiguration();
        try {
            languageConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
