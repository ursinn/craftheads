package me.deejayarroba.craftheads.listeners;

import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import me.deejayarroba.craftheads.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Main mainInstance = Main.getInstance();

        if (!mainInstance.getConfig().getBoolean("update-check")) {
            return;
        }

        if (!player.hasPermission("craftheads.updater") && !event.getPlayer().isOp()) {
            return;
        }

        if (!mainInstance.getUpdateChecker().isUpdateAvailable()) {
            return;
        }

        player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.UPDATE_AVAILABLE));
    }
}
