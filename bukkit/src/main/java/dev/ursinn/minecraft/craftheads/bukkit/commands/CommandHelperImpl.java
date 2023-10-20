package dev.ursinn.minecraft.craftheads.bukkit.commands;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.Locales;
import dev.ursinn.minecraft.craftheads.bukkit.menu.MenuManager;
import dev.ursinn.minecraft.craftheads.core.commands.CommandHelper;
import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHelperImpl implements CommandHelper {

    private final Main mainInstance;

    public CommandHelperImpl() {
        this.mainInstance = Main.getInstance();
    }

    @Override
    public boolean hasEconomy() {
        return mainInstance.getEconomy() != null;
    }

    @Override
    public double getOtherHeadPrice() {
        return mainInstance.getOtherHeadPrice();
    }

    @Override
    public void openMainMenu(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        MenuManager.MAIN_MENU.open(p);
    }

    @Override
    public double getBalance(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        return mainInstance.getEconomy().getBalance(p);
    }

    @Override
    public boolean isInventoryFull(CommandIssuer issuer) {
        Player p = issuer.getIssuer();
        return p.getInventory().firstEmpty() == -1;
    }

    @Override
    public void withdrawPlayer(CommandIssuer issuer, double amount) {
        Player p = issuer.getIssuer();
        mainInstance.getEconomy().withdrawPlayer(p, amount);
    }

    @Override
    public void giveSkull(CommandIssuer issuer, Locales locales, String playerName) {
        Player p = issuer.getIssuer();
        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(playerName))
                .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_NAME).replace("{headName}", playerName))
                .build();
        p.getInventory().addItem(head);
    }
}
