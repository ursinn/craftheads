package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("update-check")) {
            return;
        }

        if (!event.getPlayer().hasPermission("craftheads.updater") && !event.getPlayer().isOp()) {
            return;
        }

        if (!Main.getInstance().getUpdateChecker().isUpdateAvailable()) {
            return;
        }

        MessageManager.getInstance().info(event.getPlayer(),
                Main.getInstance().getLanguage().getLanguageConfig().getString("update.notify",
                        "An update for CraftHeads is available"));
    }
}
