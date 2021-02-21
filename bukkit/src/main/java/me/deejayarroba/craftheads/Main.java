package me.deejayarroba.craftheads;

import me.deejayarroba.craftheads.commands.CraftHeadsCommand;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.utils.AbstractCommand;
import me.deejayarroba.craftheads.utils.Language;
import me.deejayarroba.craftheads.utils.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JavaPlugin {

    private static final boolean DEV_BUILD = true;
    private static final int SPIGOT_PLUGIN_ID = 59481;
    private static final int METRICS_PLUGIN_ID = 3033;

    private static Main instance;
    private UpdateChecker updateChecker;
    private Language language;
    private Economy economy;
    private float defaultHeadPrice;
    private JSONArray headCategories;

    public static Main getInstance() {
        return instance;
    }

    public static boolean isDevBuild() {
        return DEV_BUILD;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this);

        language = new Language();
        language.createLanguageFile();

        economy = null;
        if (getConfig().getBoolean("economy")) {
            setupEconomy();
        }

        defaultHeadPrice = getConfig().getInt("default-price");

        headCategories = new JSONArray();
        loadCategories();

        MenuManager.setup();

        // Register the events
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        // Register the command
        AbstractCommand craftHeadsCommand =
                new CraftHeadsCommand("craftheads", "/<command>", "The main CraftHeads command.");
        craftHeadsCommand.register();

        // This takes care of auto-updating and metrics
        if (getConfig().getBoolean("metrics")) {
            Metrics metrics = new Metrics(this, METRICS_PLUGIN_ID);
            metrics.addCustomChart(
                    new Metrics.SimplePie("language", () -> getConfig().getString("language", "en")));
            metrics.addCustomChart(
                    new Metrics.SimplePie("economy", () -> getConfig().getString("economy", "false")));
        }

        if (getConfig().getBoolean("update-check")) {
            updateChecker.checkUpdate();
        }

        if (isDevBuild()) {
            getLogger().info("NMS Version: " + Skulls.getNmsVersion());
            if (Skulls.get18Versions().contains(Skulls.getNmsVersion())) {
                getLogger().info("Use 1.8 Heads");
            } else {
                getLogger().info("Use 1.13 Heads");
            }
        }

    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            if (inv != null) {
                if (MenuManager.getMenu(inv) == null) {
                    continue;
                }
                p.closeInventory();
            }
        }
    }

    public @Nonnull
    File getPluginFile() {
        return getFile();
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    private void loadCategories() {

        JSONParser parser = new JSONParser();

        if (getConfig().getBoolean("reset-categories")) {
            JarFile jarfile;
            try {
                jarfile = new JarFile(getPluginFile());

                Enumeration<JarEntry> entries = jarfile.entries();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith("categories/") && !"categories/".equals(name)) {
                        saveResource(name, true);
                    }
                }
                jarfile.close();
            } catch (IOException e) {
                getLogger().warning(String.valueOf(e));
            }

            getConfig().set("reset-categories", false);
            saveConfig();
        }

        for (File file : Objects.requireNonNull(getDataFolder().listFiles())) {
            if (file.isDirectory()) {
                if ("categories".equals(file.getName())) {
                    for (File categoryFile : Objects.requireNonNull(file.listFiles())) {
                        if (categoryFile.isFile()) {
                            try {
                                headCategories.add(parser.parse(
                                        new String(Files.readAllBytes(categoryFile.toPath()), StandardCharsets.UTF_8)));
                            } catch (IOException | ParseException e) {
                                getLogger().warning(String.valueOf(e));
                            }
                        }
                    }
                }
            }
        }
    }

    public @Nonnull UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public @Nonnull Language getLanguage() {
        return language;
    }

    public @Nonnull Economy getEconomy() {
        return economy;
    }

    public float getDefaultHeadPrice() {
        return defaultHeadPrice;
    }

    public @Nonnull JSONArray getHeadCategories() {
        return headCategories;
    }
}
