package me.deejayarroba.craftheads;

import dev.ursinn.utils.minecraft.checker.UpdateChecker;
import dev.ursinn.utils.minecraft.checker.UpdatePlatform;
import lombok.Getter;
import me.deejayarroba.craftheads.commands.CraftHeadsCommand;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.utils.AbstractCommand;
import me.deejayarroba.craftheads.utils.Language;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JavaPlugin {

    @Getter
    private static final boolean devBuild = true;
    private static final String SPIGOT_PLUGIN_ID = "59481";
    private static final int METRICS_PLUGIN_ID = 3033;

    @Getter
    private static Main instance;

    @Getter
    private static Language language;
    @Getter
    private UpdateChecker updateChecker;
    @Getter
    private Economy economy;

    public static JSONArray HEAD_CATEGORIES = new JSONArray();
    public static float defaultHeadPrice;


    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        language = new Language();
        language.createLanguageFile();

        this.updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this.getDescription().getName(), this.getDescription().getVersion(), UpdatePlatform.SPIGOT);

        this.economy = null;
        if (getConfig().getBoolean("economy")) {
            setupEconomy();
        }

        loadCategories();

        defaultHeadPrice = getConfig().getInt("default-price");

        MenuManager.setup();

        // Register the events
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        // Register the command
        AbstractCommand craftHeadsCommand = new CraftHeadsCommand("craftheads", "/<command>", "The main CraftHeads command.");
        craftHeadsCommand.register();


        if (!isDevBuild()) {
            // Metrics
            if (getConfig().getBoolean("metrics")) {
                Metrics metrics = new Metrics(this, METRICS_PLUGIN_ID);
                metrics.addCustomChart(
                        new Metrics.SimplePie("language", () -> getConfig().getString("language", "en")));
                metrics.addCustomChart(
                        new Metrics.SimplePie("economy", () -> getConfig().getString("economy", "false")));
            }

            // Update Check
            if (getConfig().getBoolean("update-check")) {
                updateChecker.checkUpdate();
            }
        }

        if (isDevBuild()) {
            System.out.println("NMS Version: " + Skulls.getNmsVersion());
            if (Skulls.get1_8Versions().contains(Skulls.getNmsVersion()))
                System.out.println("Use 1.8 Heads");
            else
                System.out.println("Use 1.13 Heads");
        }

    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            if (inv != null)
                if (MenuManager.getMenu(inv) != null)
                    p.closeInventory();
        }
    }

    public File getPluginFile() {
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
                    if (name.startsWith("categories/") && !name.equals("categories/")) {
                        saveResource(name, true);
                    }
                }
                jarfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getConfig().set("reset-categories", false);
            saveConfig();
        }

        for (File file : Objects.requireNonNull(getDataFolder().listFiles()))
            if (file.isDirectory())
                if (file.getName().equals("categories"))
                    for (File categoryFile : Objects.requireNonNull(file.listFiles()))
                        if (categoryFile.isFile())
                            try {
                                HEAD_CATEGORIES.add(parser.parse(new String(Files.readAllBytes(categoryFile.toPath()), StandardCharsets.UTF_8)));
                            } catch (IOException | ParseException e) {
                                e.printStackTrace();
                            }
    }
}
