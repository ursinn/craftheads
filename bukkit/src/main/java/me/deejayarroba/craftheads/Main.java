package me.deejayarroba.craftheads;

import co.aikar.commands.PaperCommandManager;
import dev.ursinn.minecraft.craftheads.bukkit.commands.CommandHelperImpl;
import dev.ursinn.minecraft.craftheads.bukkit.utils.CategoriesImpl;
import dev.ursinn.minecraft.craftheads.bukkit.utils.LanguageImpl;
import dev.ursinn.minecraft.craftheads.core.commands.CraftHeadsCommand;
import dev.ursinn.minecraft.craftheads.core.utils.Categories;
import dev.ursinn.minecraft.craftheads.core.utils.Language;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import dev.ursinn.utils.bukkit.utils.UtilsBukkit;
import dev.ursinn.utils.minecraft.checker.UpdateChecker;
import dev.ursinn.utils.minecraft.checker.UpdatePlatform;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static final boolean DEV_BUILD = true;
    private static final String SPIGOT_PLUGIN_ID = "59481";
    private static final int METRICS_PLUGIN_ID = 3033;

    private static Main instance;
    private UpdateChecker updateChecker;
    private LanguageImpl language;
    private Economy economy;
    private Categories categories;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this.getDescription().getName(), this.getDescription().getVersion(), UpdatePlatform.SPIGOT);

        language = new LanguageImpl();
        language.loadFiles();

        economy = null;
        if (getConfig().getBoolean("economy")) {
            setupEconomy();
        }

        categories = new CategoriesImpl();
        categories.loadFiles();

        MenuManager.setup();

//        UtilsBukkit.registerListener("me.deejayarroba.craftheads.listeners", this);
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        registerCommands();

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
            getLogger().info("NMS Version: " + UtilsBukkit.getNmsVersion());
            if (SkullBukkit.get18Versions().contains(UtilsBukkit.getNmsVersion())) {
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

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(instance);

        commandManager.registerCommand(new CraftHeadsCommand(new CommandHelperImpl()));
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

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public Language getLanguage() {
        return language;
    }

    public Economy getEconomy() {
        return economy;
    }

    public float getDefaultHeadPrice() {
        return getConfig().getInt("default-price");
    }

    public float getOtherHeadPrice() {
        return getConfig().getInt("player-other-head-price");
    }

    public Categories getCategories() {
        return categories;
    }

    public boolean isDevBuild() {
        return DEV_BUILD;
    }
}
