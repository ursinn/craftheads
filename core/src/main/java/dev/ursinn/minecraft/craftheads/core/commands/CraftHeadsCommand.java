package dev.ursinn.minecraft.craftheads.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;

/**
 * @author Ursin Filli
 * @version 1.0
 * @since 1.0
 */
@CommandAlias("craftheads")
@Description("CraftHeads Command")
@CommandPermission("craftheads.use")
public class CraftHeadsCommand extends BaseCommand {

    private static final String PREFIX = "{prefix}";

    private final CommandHelper commandHelper;

    public CraftHeadsCommand(CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Default
    @CatchUnknown
    public void onCommand(CommandIssuer sender, String[] args) {
        Locales locales = getCurrentCommandManager().getLocales();
        if (!sender.isPlayer()) {
            sender.sendMessage(locales.getMessage(sender, MessageKeys.NOT_ALLOWED_ON_CONSOLE));
            return;
        }

        if (args.length == 0) {
            // Open the menu
            commandHelper.openMainMenu(sender);
            return;
        }

        // Here the command would be: /craftheads <playername>
        double otherHeadPrice = commandHelper.getOtherHeadPrice();

        if (commandHelper.hasEconomy()) {
            double balance = commandHelper.getBalance(sender);
            if (balance < otherHeadPrice && otherHeadPrice > 0) {
                sender.sendMessage(locales.getMessage(sender, CraftHeadsMessageKeys.NOT_ENOUGH_MONEY)
                        .replace(PREFIX, locales.getMessage(sender, CraftHeadsMessageKeys.PREFIX))
                );
                return;
            }
        }

        // Check if the inventory is full
        if (commandHelper.isInventoryFull(sender)) {
            sender.sendMessage(locales.getMessage(sender, CraftHeadsMessageKeys.INVENTORY_FULL)
                    .replace(PREFIX, locales.getMessage(sender, CraftHeadsMessageKeys.PREFIX))
            );
            return;
        }

        String playerName = args[0];

        if (commandHelper.hasEconomy() && otherHeadPrice > 0) {
            commandHelper.withdrawPlayer(sender, otherHeadPrice);
            sender.sendMessage(locales.getMessage(sender, CraftHeadsMessageKeys.HEAD_BUY)
                    .replace("{playerName}", playerName)
                    .replace("{headPrice}", String.valueOf(otherHeadPrice))
                    .replace(PREFIX, locales.getMessage(sender, CraftHeadsMessageKeys.PREFIX))
            );
        }

        commandHelper.giveSkull(sender, locales, playerName);
        sender.sendMessage(locales.getMessage(sender, CraftHeadsMessageKeys.HEAD_GIVE)
                .replace("{playerName}", playerName)
                .replace(PREFIX, locales.getMessage(sender, CraftHeadsMessageKeys.PREFIX))
        );
    }
}
