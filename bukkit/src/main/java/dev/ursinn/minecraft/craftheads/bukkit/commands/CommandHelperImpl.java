package dev.ursinn.minecraft.craftheads.bukkit.commands;

import co.aikar.commands.CommandIssuer;
import dev.ursinn.minecraft.craftheads.core.commands.CommandHelper;
import dev.ursinn.minecraft.craftheads.core.utils.Language;
import dev.ursinn.minecraft.craftheads.core.utils.MessageManager;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.utils.MessageManagerImpl;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHelperImpl implements CommandHelper {

    @Override
    public MessageManager getMessageManager() {
        return MessageManagerImpl.getInstance();
    }

    @Override
    public Language getLanguage() {
        return Main.getInstance().getLanguage();
    }

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
    public void giveSkull(CommandIssuer issuer, Language language, String playerName) {
        Player p = issuer.getIssuer();
        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(playerName))
                .setName(ChatColor.translateAlternateColorCodes('&',
                        language.getMessage("item", "&6Head: &b%args0%"))
                        .replace("%args0%", playerName))
                .build();
        p.getInventory().addItem(head);
    }
}
