package me.deejayarroba.craftheads;

import co.aikar.commands.BukkitMessageFormatter;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import dev.ursinn.minecraft.craftheads.bukkit.commands.CommandHelperImpl;
import dev.ursinn.minecraft.craftheads.bukkit.utils.CategoriesImpl;
import dev.ursinn.minecraft.craftheads.core.commands.CraftHeadsCommand;
import dev.ursinn.minecraft.craftheads.core.utils.Categories;
import dev.ursinn.minecraft.craftheads.core.utils.LocalMessageKeys;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import dev.ursinn.utils.bukkit.utils.UtilsBukkit;
import dev.ursinn.utils.minecraft.checker.UpdateChecker;
import dev.ursinn.utils.minecraft.checker.UpdatePlatform;
import lombok.Getter;
import me.deejayarroba.craftheads.listeners.InvClickEvent;
import me.deejayarroba.craftheads.listeners.PlayerJoin;
import me.deejayarroba.craftheads.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static final boolean DEV_BUILD = true;
    private static final String SPIGOT_PLUGIN_ID = "59481";
    private static final int METRICS_PLUGIN_ID = 3033;

    @Getter
    private static Main instance;

    @Getter
    private UpdateChecker updateChecker;
    @Getter
    private Economy economy;
    @Getter
    private Categories categories;
    @Getter
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        registerCommands();

        updateChecker = new UpdateChecker(SPIGOT_PLUGIN_ID, this.getDescription().getName(), this.getDescription().getVersion(), UpdatePlatform.SPIGOT);

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

    public float getDefaultHeadPrice() {
        return getConfig().getInt("default-price");
    }

    public float getOtherHeadPrice() {
        return getConfig().getInt("player-other-head-price");
    }

    public boolean isDevBuild() {
        return DEV_BUILD;
    }

    public String messageFormatter(String message) {
        return ChatColor.translateAlternateColorCodes('&', message)
                .replace("{prefix}", commandManager.getLocales().getMessage(null, LocalMessageKeys.PREFIX))
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
