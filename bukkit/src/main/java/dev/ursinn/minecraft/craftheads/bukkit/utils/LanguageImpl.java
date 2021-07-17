package dev.ursinn.minecraft.craftheads.bukkit.utils;

import me.deejayarroba.craftheads.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Ursin Filli
 * @version 1.0
 */
public class LanguageImpl implements dev.ursinn.minecraft.craftheads.core.utils.Language {

    private final FileConfiguration languageConfig;
    private final File languageFile;
    private final FileHelper fileHelper;
    private final Main mainInstance;

    public LanguageImpl() {
        languageConfig = new YamlConfiguration();
        languageFile = new File(Main.getInstance().getDataFolder() + "/languages",
                Main.getInstance().getConfig().getString("language", "en") + ".yml");
        fileHelper = new FileHelper();
        mainInstance = Main.getInstance();
    }

    @Override
    public void loadFiles() {
        if (!languageFile.exists()) {
            fileHelper.extractFiles("languages/");
        }

        load();
    }

    @Override
    public String getMessage(String key, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&', languageConfig.getString(key, defaultMessage));
    }



    private void load() {
        try {
            languageConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            mainInstance.getLogger().warning(String.valueOf(e));
        }
    }
}
