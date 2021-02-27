package dev.ursinn.minecraft.craftheads.bukkit.utils;

import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import org.apiguardian.api.API;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Ursin Filli
 * @version 1.0
 */
@API(status = API.Status.MAINTAINED, since = "1.0")
public class Language {

    @Getter
    private final @Nonnull
    FileConfiguration languageConfig;
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
        try {
            JarFile jarfile = new JarFile(mainInstance.getPluginFile());

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
}
