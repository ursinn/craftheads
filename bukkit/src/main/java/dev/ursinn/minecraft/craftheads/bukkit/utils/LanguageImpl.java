package dev.ursinn.minecraft.craftheads.bukkit.utils;

import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Ursin Filli
 * @version 1.0
 */
public class LanguageImpl implements dev.ursinn.minecraft.craftheads.core.utils.Language {

    @Getter
    private final
    FileConfiguration languageConfig;
    private final File languageFile;
    private final Main mainInstance;

    public LanguageImpl() {
        languageConfig = new YamlConfiguration();
        languageFile = new File(Main.getInstance().getDataFolder() + "/languages",
                Main.getInstance().getConfig().getString("language", "en") + ".yml");
        mainInstance = Main.getInstance();
    }

    @Override
    public void createLanguageFile() {
        if (!languageFile.exists()) {
            extractFiles();
        }

        load();
    }

    @Override
    public String getMessage(String key, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&', languageConfig.getString(key, defaultMessage));
    }

    private void extractFiles() {
        try {
            JarFile jarfile = new JarFile(mainInstance.getPluginFile());

            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("craftheads-languages/") && !"craftheads-languages/".equals(name)) {
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
}
