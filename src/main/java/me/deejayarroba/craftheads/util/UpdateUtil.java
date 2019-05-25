package me.deejayarroba.craftheads.util;

import me.deejayarroba.craftheads.Main;
import org.bukkit.entity.Player;

public class UpdateUtil {

    MessageManager msg = MessageManager.getInstance();
    private static UpdateUtil instance = new UpdateUtil();

    // Alerts the administrator of a new update for the plugin
    public void updateNotice(Player p) {
        if (!Main.devBuild)
            if (Main.instance.getConfig().getBoolean("update-check"))
                if (p.hasPermission("craftheads.updater") || p.isOp()) {
                    UpdateChecker updateChecker = new UpdateChecker(59484);
                    if (updateChecker.check())
                        msg.info(p, "An update is available for CraftHeads.");
                }
    }

    public static UpdateUtil getInstance() {
        return instance;
    }

}