package me.deejayarroba.craftheads;

import me.deejayarroba.craftheads.commands.CraftHeadsCommand;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.util.AbstractCommand;
import me.deejayarroba.craftheads.util.UpdateChecker;
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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JavaPlugin {

    public static boolean devBuild = true;

    public static JSONArray HEAD_CATEGORIES = new JSONArray();
    public static Economy economy = null;
    public static float defaultHeadPrice;
    public static Main instance;
    public UpdateChecker updateChecker = new UpdateChecker(59481, this);

    @Override
    public void onEnable() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf('.') + 1);
        if (!Skulls.getVersions().containsKey(ver)) {
            System.err.println("unsupported Minecraft Server version! (" + ver + ")");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        instance = this;

        if (getConfig().getBoolean("economy"))
            setupEconomy();

        loadCategories();

        defaultHeadPrice = getConfig().getInt("default-price");

        MenuManager.setup();

        // Register the events
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        // Register the command
        AbstractCommand craftHeadsCommand = new CraftHeadsCommand("craftheads", "/<command>", "The main CraftHeads command.");
        craftHeadsCommand.register();

        // This takes care of auto-updating and metrics
        if (!devBuild) {
            if (getConfig().getBoolean("metrics"))
                new Metrics(this);

            if (getConfig().getBoolean("update-check")) {
                updateChecker.checkUpdates.start();
            }
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        economy = rsp.getProvider();
        return economy != null;
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

        for (File file : getDataFolder().listFiles())
            if (file.isDirectory())
                if (file.getName().equals("categories"))
                    for (File categoryFile : file.listFiles())
                        if (categoryFile.isFile())
                            try {
                                HEAD_CATEGORIES.add(parser.parse(new String(Files.readAllBytes(categoryFile.toPath()), StandardCharsets.UTF_8)));
                            } catch (IOException | ParseException e) {
                                e.printStackTrace();
                            }
    }


}
