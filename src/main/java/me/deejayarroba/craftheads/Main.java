package me.deejayarroba.craftheads;

import dev.ursinn.utils.bukkit.reflections.ReflectionsBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import dev.ursinn.utils.bukkit.utils.UtilsBukkit;
import dev.ursinn.utils.minecraft.checker.UpdateChecker;
import dev.ursinn.utils.minecraft.checker.UpdatePlatform;
import lombok.Getter;
import me.deejayarroba.craftheads.commands.CraftHeadsCommand;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.utils.AbstractCommand;
import me.deejayarroba.craftheads.utils.Categories;
import me.deejayarroba.craftheads.utils.CategoriesImpl;
import me.deejayarroba.craftheads.utils.Language;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
    private static Categories categories;
    @Getter
    private UpdateChecker updateChecker;
    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        language = new Language();
        language.createLanguageFile();

        this.updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this.getDescription().getName(), this.getDescription().getVersion(), UpdatePlatform.SPIGOT, null);

        this.economy = null;
        if (getConfig().getBoolean("economy")) {
            setupEconomy();
        }

        loadCategories();

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

        // Dev Build NMS Debug Info
        if (isDevBuild()) {
            getLogger().info("NMS Version: " + ReflectionsBukkit.getNmsVersion());
            if (SkullBukkit.get18Versions().contains(ReflectionsBukkit.getNmsVersion())) {
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
        categories = new CategoriesImpl();
        categories.loadFiles();
    }

    public float getDefaultHeadPrice() {
        return getConfig().getInt("default-price");
    }
}
