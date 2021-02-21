package me.deejayarroba.craftheads.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageManager {

    private static final MessageManager instance = new MessageManager();
    private static final String PREFIX =
            ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "CraftHeads" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    public static MessageManager getInstance() {
        return instance;
    }

    public void info(CommandSender sender, String msg) {
        msg(sender, ChatColor.YELLOW, msg);
    }

    public void good(CommandSender sender, String msg) {
        msg(sender, ChatColor.GREEN, msg);
    }

    public void bad(CommandSender sender, String msg) {
        msg(sender, ChatColor.RED, msg);
    }

    public void msg(CommandSender sender, ChatColor color, String msg) {
        sender.sendMessage(PREFIX + color + msg);
    }
}
