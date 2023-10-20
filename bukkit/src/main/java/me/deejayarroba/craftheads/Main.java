package me.deejayarroba.craftheads;

import co.aikar.commands.BukkitMessageFormatter;
import co.aikar.commands.Locales;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import dev.ursinn.minecraft.craftheads.bukkit.commands.CommandHelperImpl;
import dev.ursinn.minecraft.craftheads.bukkit.utils.CategoriesImpl;
import dev.ursinn.minecraft.craftheads.core.MainInterface;
import dev.ursinn.minecraft.craftheads.core.commands.CraftHeadsCommand;
import dev.ursinn.minecraft.craftheads.core.utils.Categories;
import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import dev.ursinn.utils.bukkit.utils.UtilsBukkit;
import dev.ursinn.utils.minecraft.checker.UpdateChecker;
import dev.ursinn.utils.minecraft.checker.UpdatePlatform;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements MainInterface {

    private static final boolean DEV_BUILD = true;
    private static final String SPIGOT_PLUGIN_ID = "59481";
    private static final int METRICS_PLUGIN_ID = 3033;

    @Getter
    private static Main instance;

    @Getter
    private UpdateChecker updateChecker;
    @Getter
    private Categories categories;
    @Getter
    private Economy economy;
    @Getter
    private InventoryManager inventoryManager;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        registerCommands();

        this.updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this.getDescription().getName(), this.getDescription().getVersion(), UpdatePlatform.SPIGOT);

        this.economy = null;
        if (getConfig().getBoolean("economy")) {
            setupEconomy();
        }

        this.categories = new CategoriesImpl();
        getCategories().loadFiles();

//        UtilsBukkit.registerListener("me.deejayarroba.craftheads.listeners", this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

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

        if (DEV_BUILD) {
            getLogger().info("NMS Version: " + UtilsBukkit.getNmsVersion());
            if (SkullBukkit.get18Versions().contains(UtilsBukkit.getNmsVersion())) {
                getLogger().info("Use 1.8 Heads");
            } else {
                getLogger().info("Use 1.13 Heads");
            }
        }

        this.inventoryManager = new InventoryManager(this);
        getInventoryManager().init();

    }

    @Override
    public void onDisable() {
        //
    }

    private void registerCommands() {
        commandManager = new PaperCommandManager(instance);
        commandManager.setFormat(MessageType.INFO, new BukkitMessageFormatter(
                ChatColor.BLUE,
                ChatColor.DARK_GREEN,
                ChatColor.GREEN,
                ChatColor.DARK_GRAY,
                ChatColor.RED,
                ChatColor.YELLOW,
                ChatColor.AQUA,
                ChatColor.GOLD
        ));

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

    @Override
    public float getDefaultHeadPrice() {
        return getConfig().getInt("default-price");
    }

    @Override
    public float getOwnHeadPrice() {
        return getConfig().getInt("player-own-head-price");
    }

    @Override
    public float getOtherHeadPrice() {
        return getConfig().getInt("player-other-head-price");
    }

    @Override
    public String messageFormatter(CraftHeadsMessageKeys localMessageKeys) {
        Locales locales = commandManager.getLocales();
        return ChatColor.translateAlternateColorCodes('&', locales.getMessage(null, localMessageKeys))
                .replace("{prefix}", locales.getMessage(null, CraftHeadsMessageKeys.PREFIX))
                .replace("<c1>", "§9").replace("</c1>", "§f")
                .replace("<c2>", "§2").replace("</c2>", "§f")
                .replace("<c3>", "§a").replace("</c3>", "§f")
                .replace("<c4>", "§8").replace("</c4>", "§f")
                .replace("<c5>", "§c").replace("</c5>", "§f")
                .replace("<c6>", "§e").replace("</c6>", "§f")
                .replace("<c7>", "§b").replace("</c7>", "§f")
                .replace("<c8>", "§6").replace("</c8>", "§f")
                ;
    }
}
