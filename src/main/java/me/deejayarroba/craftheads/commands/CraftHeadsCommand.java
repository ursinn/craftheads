package me.deejayarroba.craftheads.commands;

import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.utils.AbstractCommand;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftHeadsCommand extends AbstractCommand {

    MessageManager msg = MessageManager.getInstance();

    public CraftHeadsCommand(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("craftheads")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (!sender.hasPermission("craftheads.use")) {
                    msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.permission", "You don't have permission to use this command.")));
                    return false;
                }

                if (args.length > 0) {

                    // Here the command would be: /craftheads <playername>

                    // TODO: implement buying other people's heads

                    float otherHeadPrice = Main.getInstance().getConfig().getInt("player-other-head-price");

                    if (Main.getInstance().getEconomy() != null) {
                        double balance = Main.getInstance().getEconomy().getBalance(p);
                        if (balance < otherHeadPrice && otherHeadPrice > 0) {
                            msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.money.player", "You can't your afford this player's head!")));
                            return true;
                        }
                    }

                    // Check if the inventory is full
                    if (p.getInventory().firstEmpty() == -1) {
                        msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.inv", "Your inventory is full!")));
                        return true;
                    } else {
                        String playerName = args[0];
                        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(playerName))
                                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("item", "&6Head: &b%args0%").replaceAll("%args0%", args[0])))
                                .build();

                        if (Main.getInstance().getEconomy() != null && otherHeadPrice > 0) {
                            Main.getInstance().getEconomy().withdrawPlayer(p, otherHeadPrice);
                            msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.buy", "You bought &b%playerName%&a's head for &b %otherHeadPrice%".replaceAll("%playerName%", playerName).replaceAll("%otherHeadPrice%", String.valueOf(otherHeadPrice)))));
                        }

                        p.getInventory().addItem(head);
                        msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.give", "You now have %args0%'s head!").replaceAll("%args0%", args[0])));
                        return true;
                    }
                } else {
                    // Open the menu

                    Inventory inv = MenuManager.MAIN_MENU.open(p);
                    p.openInventory(inv);

                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.console", "You can only run this command as a player.")));
                return true;
            }
        }
        return false;
    }

}
