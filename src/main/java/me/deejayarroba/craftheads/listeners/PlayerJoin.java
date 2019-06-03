package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.util.MessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!Main.devBuild) {
            if (Main.instance.getConfig().getBoolean("update-check")) {
                if (e.getPlayer().hasPermission("craftheads.updater") || e.getPlayer().isOp()) {
                    if (Main.instance.updateChecker.isUpdate())
                        MessageManager.getInstance().info(e.getPlayer(), "An update is available for CraftHeads.");
                }
            }
        }
    }

}
