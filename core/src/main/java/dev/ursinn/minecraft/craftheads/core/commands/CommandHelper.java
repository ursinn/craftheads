package dev.ursinn.minecraft.craftheads.core.commands;

import co.aikar.commands.CommandIssuer;
import dev.ursinn.minecraft.craftheads.core.utils.Language;
import dev.ursinn.minecraft.craftheads.core.utils.MessageManager;

public interface CommandHelper {

    MessageManager getMessageManager();

    Language getLanguage();

    boolean hasEconomy();

    double getOtherHeadPrice();

    void openMainMenu(CommandIssuer issuer);

    double getBalance(CommandIssuer issuer);

    boolean isInventoryFull(CommandIssuer issuer);

    void withdrawPlayer(CommandIssuer issuer, double amount);

    void giveSkull(CommandIssuer issuer, Language language, String playerName);
}
