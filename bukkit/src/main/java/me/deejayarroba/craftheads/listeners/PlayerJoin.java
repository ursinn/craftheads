package me.deejayarroba.craftheads.listeners;

import dev.ursinn.minecraft.craftheads.core.utils.LocalMessageKeys;
import me.deejayarroba.craftheads.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Main.getInstance().getConfig().getBoolean("update-check")) {
            return;
        }

        if (!player.hasPermission("craftheads.updater") && !event.getPlayer().isOp()) {
            return;
        }

        if (!Main.getInstance().getUpdateChecker().isUpdateAvailable()) {
            return;
        }

        player.sendMessage(
                Main.getInstance().messageFormatter(
                        Main.getInstance().getCommandManager().getLocales().getMessage(
                                null, LocalMessageKeys.UPDATE_AVAILABLE)));
    }
}
