package me.deejayarroba.craftheads.commands;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.utils.AbstractCommand;
import me.deejayarroba.craftheads.utils.ItemBuilder;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class CraftHeadsCommand extends AbstractCommand {

    private static final String BASE_COMMAND = "craftheads";
    private static final String PERMISSION = "craftheads.use";

    private final MessageManager msg;
    private final Main mainInstance;
    private final FileConfiguration languageConfig;

    public CraftHeadsCommand(@Nonnull String command, @Nonnull String usage, @Nonnull String description) {
        super(command, usage, description);
        msg = MessageManager.getInstance();
        mainInstance = Main.getInstance();
        languageConfig = mainInstance.getLanguage().getLanguageConfig();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase(BASE_COMMAND)) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    languageConfig.getString("error.console", "You can only run this command as a player.")));
            return true;
        }

        if (!sender.hasPermission(PERMISSION)) {
            msg.bad(sender, ChatColor.translateAlternateColorCodes('&',
                    languageConfig.getString(
                            "error.permission", "You don't have permission to use this command.")));
            return false;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            // Open the menu
            Inventory inv = MenuManager.getMainMenu().getInventory();
            p.openInventory(inv);
            return true;
        }

        // Here the command would be: /craftheads <playername>
        float otherHeadPrice = mainInstance.getConfig().getInt("player-other-head-price");

        if (mainInstance.getEconomy() != null) {
            double balance = mainInstance.getEconomy().getBalance(p);
            if (balance < otherHeadPrice && otherHeadPrice > 0) {
                msg.bad(p, ChatColor.translateAlternateColorCodes('&',
                        languageConfig.getString("error.money.player"
                                , "You can't your afford this player's head!")));
                return true;
            }
        }

        // Check if the inventory is full
        if (p.getInventory().firstEmpty() == -1) {
            msg.bad(p, ChatColor.translateAlternateColorCodes('&',
                    languageConfig.getString("error.inv", "Your inventory is full!")));
            return true;
        }

        String playerName = args[0];
        ItemStack head = new ItemBuilder(Skulls.getPlayerSkull(playerName))
                .setName(ChatColor.translateAlternateColorCodes('&',
                        languageConfig.getString("item", "&6Head: &b%args0%")
                                .replace("%args0%", args[0])))
                .build();

        if (mainInstance.getEconomy() != null && otherHeadPrice > 0) {
            mainInstance.getEconomy().withdrawPlayer(p, otherHeadPrice);
            msg.good(p, ChatColor.translateAlternateColorCodes('&',
                    languageConfig.getString("give.buy",
                            "You bought &b%playerName%&a's head for &b %otherHeadPrice%"
                                    .replace("%playerName%", playerName)
                                    .replace("%otherHeadPrice%", String.valueOf(otherHeadPrice)))));
        }

        p.getInventory().addItem(head);
        msg.good(p, ChatColor.translateAlternateColorCodes('&',
                languageConfig.getString("give.give", "You now have %args0%'s head!")
                        .replace("%args0%", args[0])));

        return true;
    }
}
