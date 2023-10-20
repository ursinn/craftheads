package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!Main.devBuild) {
            if (Main.getInstance().getConfig().getBoolean("update-check")) {
                if (e.getPlayer().hasPermission("craftheads.updater") || e.getPlayer().isOp()) {
                    if (Main.getInstance().updateChecker.isUpdate())
                        MessageManager.getInstance().info(e.getPlayer(), Main.getLanguage().getLanguageConfig().getString("update.notify", "An update for CraftHeads is available"));
                }
            }
        }
    }

}