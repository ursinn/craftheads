package me.deejayarroba.craftheads.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public final class MessageManager {

    private static final MessageManager instance = new MessageManager();
    private static final String PREFIX =
            ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "CraftHeads" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    public static MessageManager getInstance() {
        return instance;
    }

    public void info(@Nonnull CommandSender sender, @Nonnull String msg) {
        msg(sender, ChatColor.YELLOW, msg);
    }

    public void good(@Nonnull CommandSender sender, @Nonnull String msg) {
        msg(sender, ChatColor.GREEN, msg);
    }

    public void bad(@Nonnull CommandSender sender, @Nonnull String msg) {
        msg(sender, ChatColor.RED, msg);
    }

    public void msg(@Nonnull CommandSender sender, @Nonnull ChatColor color, @Nonnull String msg) {
        sender.sendMessage(PREFIX + color + msg);
    }
}
