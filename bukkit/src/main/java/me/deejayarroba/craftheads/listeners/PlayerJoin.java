package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.utils.MessageManagerImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("update-check")) {
            return;
        }

        if (!event.getPlayer().hasPermission("craftheads.updater") && !event.getPlayer().isOp()) {
            return;
        }

        if (!Main.getInstance().getUpdateChecker().isUpdateAvailable()) {
            return;
        }

        MessageManagerImpl.getInstance().info(event.getPlayer(),
                Main.getInstance().getLanguage().getMessage("update.notify",
                        "An update for CraftHeads is available"));
    }
}
