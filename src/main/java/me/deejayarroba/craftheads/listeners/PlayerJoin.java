package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!Main.devBuild) {
            return;
        }

        if (!Main.getInstance().getConfig().getBoolean("update-check")) {
            return;
        }

        if (!player.hasPermission("craftheads.updater") && !player.isOp()) {
            return;
        }

        if (!Main.getInstance().getUpdateChecker().isUpdateAvailable()) {
            return;
        }

        MessageManager.getInstance().info(player, Main.getLanguage().getLanguageConfig().getString("update.notify", "An update for CraftHeads is available"));
    }

}
