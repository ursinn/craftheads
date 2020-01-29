package me.deejayarroba.craftheads.util;

import me.deejayarroba.craftheads.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Language {

    private FileConfiguration languageConfig;

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public void createLanguageFile() {
        File languageFile = new File(Main.getInstance().getDataFolder() + "/lang", Main.getInstance().getConfig().getString("language", "en") + ".yml");
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("lang/en.yml", false);
        }

        languageConfig= new YamlConfiguration();
        try {
            languageConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
