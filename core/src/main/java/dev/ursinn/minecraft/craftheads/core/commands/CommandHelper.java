package dev.ursinn.minecraft.craftheads.core.commands;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;

public interface CommandHelper {

    boolean hasEconomy();

    double getOtherHeadPrice();

    void openMainMenu(CommandIssuer issuer);

    double getBalance(CommandIssuer issuer);

    boolean isInventoryFull(CommandIssuer issuer);

    void withdrawPlayer(CommandIssuer issuer, double amount);

    void giveSkull(CommandIssuer issuer, Locales locales, String playerName);
}
