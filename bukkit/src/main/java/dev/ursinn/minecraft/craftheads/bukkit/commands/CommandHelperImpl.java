package dev.ursinn.minecraft.craftheads.bukkit.commands;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;
import dev.ursinn.minecraft.craftheads.core.commands.CommandHelper;
import dev.ursinn.minecraft.craftheads.core.utils.LocalMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHelperImpl implements CommandHelper {

    @Override
    public boolean hasEconomy() {
        return Main.getInstance().getEconomy() != null;
    }

    @Override
    public double getOtherHeadPrice() {
        return Main.getInstance().getOtherHeadPrice();
    }

    @Override
    public void openMainMenu(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        p.openInventory(MenuManager.getMainMenu().getInventory());
    }

    @Override
    public double getBalance(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        return Main.getInstance().getEconomy().getBalance(p);
    }

    @Override
    public boolean isInventoryFull(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        return p.getInventory().firstEmpty() == -1;
    }

    @Override
    public void withdrawPlayer(CommandIssuer issuer, double amount) {
        Player p = issuer.getIssuer();
        Main.getInstance().getEconomy().withdrawPlayer(p, amount);
    }

    @Override
    public void giveSkull(CommandIssuer issuer, Locales locales, String playerName) {
        Player p = issuer.getIssuer();
        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(playerName))
                .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_NAME).replace("{headName}", playerName)))
                .build();
        p.getInventory().addItem(head);
    }
}
