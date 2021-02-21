package me.deejayarroba.craftheads.utils;

import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Ursin Filli
 * @version 1.0
 */
public class UpdateChecker {

    private final int id;
    private final Plugin plugin;
    private boolean updateAvailable;
    private String updateNotifyText;

    /**
     * Constructor.
     *
     * @param id     Spigot Plugin Id
     * @param plugin Instance of {@link Plugin}
     */
    public UpdateChecker(int id, @Nonnull Plugin plugin) {
        this.id = id;
        this.plugin = Objects.requireNonNull(plugin);
        this.updateAvailable = false;
        this.updateNotifyText = "An update for %PLUGIN_NAME% is available";
    }

    /**
     * @return result of UpdateCheck
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Checks for Update on Spigot
     */
    public void checkUpdate() {
        new Thread(() -> {
            try {
                URLConnection connection = new URL(
                        "https://api.spigotmc.org/legacy/update.php?resource=" + id).openConnection();
                checkVersion(connection);
            } catch (IOException e) {
                plugin.getLogger().warning(String.valueOf(e));
            }
        }).start();
    }

    private void checkVersion(@Nonnull URLConnection connection) throws IOException {
        InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        String oldVersion = plugin.getDescription().getVersion();
        String newVersion = br.readLine();
        if (!newVersion.equals(oldVersion)) {
            updateAvailable = true;
            plugin.getLogger().info(getFormattedUpdateNotifyText());
        }
    }

    /**
     * Placeholders:
     * %PLUGIN_NAME% - Plugin Name
     *
     * @param updateNotifyText UpdateNotifyText
     */
    public void setUpdateNotifyText(@Nonnull String updateNotifyText) {
        this.updateNotifyText = updateNotifyText;
    }

    /**
     * @return Formatted UpdateNotifyText
     */
    public @Nonnull
    String getFormattedUpdateNotifyText() {
        return updateNotifyText.replace("%PLUGIN_NAME%", plugin.getDescription().getName());
    }
}
