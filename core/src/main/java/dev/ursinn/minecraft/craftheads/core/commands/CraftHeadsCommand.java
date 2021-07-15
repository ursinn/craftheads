package dev.ursinn.minecraft.craftheads.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.ursinn.minecraft.craftheads.core.utils.Language;
import dev.ursinn.minecraft.craftheads.core.utils.MessageManager;

/**
 * @author Ursin Filli
 * @version 1.0
 * @since 1.0
 */
@CommandAlias("craftheads")
@Description("CraftHeads Command")
public class CraftHeadsCommand extends BaseCommand {

    private final CommandHelper commandHelper;
    private final MessageManager msg;
    private final Language language;

    public CraftHeadsCommand(CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
        this.msg = commandHelper.getMessageManager();
        this.language = commandHelper.getLanguage();
    }

    @Default
    @CatchUnknown
    public void onCommand(CommandIssuer sender, String[] args) {
        if (!sender.isPlayer()) {
            sender.sendMessage(language.getMessage("error.console", "You can only run this command as a player."));
            return;
        }

        Object player = sender.getIssuer();

        String permission = "craftheads.use";
        if (!sender.hasPermission(permission)) {
            msg.bad(player, language.getMessage("error.permission", "You don't have permission to use this command."));
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
                msg.bad(player, language.getMessage("error.money.player", "You can't your afford this player's head!"));
                return;
            }
        }

        // Check if the inventory is full
        if (commandHelper.isInventoryFull(sender)) {
            msg.bad(player, language.getMessage("error.inv", "Your inventory is full!"));
            return;
        }

        String playerName = args[0];

        if (commandHelper.hasEconomy() && otherHeadPrice > 0) {
            commandHelper.withdrawPlayer(sender, otherHeadPrice);
            msg.good(player, language.getMessage("give.buy",
                    "You bought &b%playerName%&a's head for &b %otherHeadPrice%"
                            .replace("%playerName%", playerName)
                            .replace("%otherHeadPrice%", String.valueOf(otherHeadPrice))));
        }

        commandHelper.giveSkull(sender, language, playerName);
        msg.good(player, language.getMessage("give.give", "You now have %args0%'s head!")
                .replace("%args0%", args[0]));
    }
}
