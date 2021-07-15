package me.deejayarroba.craftheads.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageManagerImpl implements dev.ursinn.minecraft.craftheads.core.utils.MessageManager {

    private static final dev.ursinn.minecraft.craftheads.core.utils.MessageManager instance = new MessageManagerImpl();
    private static final String PREFIX =
            ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "CraftHeads" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    public static dev.ursinn.minecraft.craftheads.core.utils.MessageManager getInstance() {
        return instance;
    }

    @Override
    public void info(Object senderObj, String msg) {
        CommandSender sender = (CommandSender) senderObj;
        msg(sender, ChatColor.YELLOW, msg);
    }

    @Override
    public void good(Object senderObj, String msg) {
        CommandSender sender = (CommandSender) senderObj;
        msg(sender, ChatColor.GREEN, msg);
    }

    @Override
    public void bad(Object senderObj, String msg) {
        CommandSender sender = (CommandSender) senderObj;
        msg(sender, ChatColor.RED, msg);
    }

    @Override
    public void msg(Object senderObj, Object colorObj, String msg) {
        CommandSender sender = (CommandSender) senderObj;
        ChatColor color = (ChatColor) colorObj;
        sender.sendMessage(PREFIX + color + msg);
    }
}
