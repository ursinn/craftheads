package me.deejayarroba.craftheads.menu.menutypes;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.util.Items;
import me.deejayarroba.craftheads.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MainMenu extends Menu {

    private static final String CATEGORIES_ITEM_URL = "http://textures.minecraft.net/texture/3e8aad673157c92317a88b1f86f5271f1cd7397d7fc8ec3281f733f751634";
    private static final String OTHER_PLAYER_ITEM_URL = "http://textures.minecraft.net/texture/f937e1c45bb8da29b2c564dd9a7da780dd2fe54468a5dfb4113b4ff658f043e1";

    MessageManager msg = MessageManager.getInstance();

    public MainMenu() {

        name = "CraftHeads menu";
        menuItems = new ArrayList<>();

        inventory = Bukkit.createInventory(null, 9, name);

        final Items.ItemStackBuilder ownHeadBuilder = Items.builder()
                .setName(Main.getLanguage().getLanguageConfig().getString("menu.main.own", "§6Get your own head"))
                .setMaterial(Skulls.getPlayerSkullMaterial())
                .setData((short) SkullType.PLAYER.ordinal());

        Items.ItemStackBuilder otherHeadBuilder = Items.editor(Skulls.getCustomSkull(OTHER_PLAYER_ITEM_URL))
                .setName(Main.getLanguage().getLanguageConfig().getString("menu.main.other", "§6Get someone else's head"))
                .addLore(Main.getLanguage().getLanguageConfig().getString("menu.main.lore.other", "§a§oUse: §e§o/craftheads <player name>"));

        final float ownHeadPrice = Main.getInstance().getConfig().getInt("player-own-head-price");
        final float otherHeadPrice = Main.getInstance().getConfig().getInt("player-other-head-price");

        if (Main.economy != null) {

            if (ownHeadPrice > 0) {
                ownHeadBuilder.addLore(ChatColor.AQUA + "Price: " + ChatColor.GREEN + ownHeadPrice);
            } else {
                ownHeadBuilder.addLore(ChatColor.AQUA + "Price: " + ChatColor.GREEN + "FREE");
            }

            if (otherHeadPrice > 0) {
                otherHeadBuilder.addLore(ChatColor.AQUA + "Price: " + ChatColor.GREEN + otherHeadPrice);
            } else {
                otherHeadBuilder.addLore(ChatColor.AQUA + "Price: " + ChatColor.GREEN + "FREE");
            }
        }

        menuItems.add(0, new MenuItem(ownHeadBuilder.build(),
                p -> {
                    if (Main.economy != null) {
                        double balance = Main.economy.getBalance(p);
                        if (balance < ownHeadPrice) {
                            // Player can't afford the head
                            msg.bad(p, "You can't your afford your own head!");
                            return;
                        }
                    }

                    // If the inventory is full
                    if (p.getInventory().firstEmpty() == -1) {
                        msg.bad(p, Main.getLanguage().getLanguageConfig().getString("error.inv.full", "Your inventory is full!"));
                    } else {
                        ItemStack head = Items.editor(Skulls.getPlayerSkull(p.getName()))
                                .setName(ChatColor.GOLD + "Head: " + ChatColor.AQUA + p.getName())
                                .build();
                        p.getInventory().addItem(head);

                        if (Main.economy != null && ownHeadPrice > 0) {
                            // Player can afford the head
                            Main.economy.withdrawPlayer(p, ownHeadPrice);
                            msg.good(p, "You bought your own head for " + ChatColor.AQUA + ownHeadPrice);
                        }

                        msg.good(p, Main.getLanguage().getLanguageConfig().getString("give.own","You now have your own head!"));
                        p.closeInventory();
                    }
                }));

        menuItems.add(1, new MenuItem(Items.editor(Skulls.getCustomSkull(CATEGORIES_ITEM_URL))
                .setName(ChatColor.GOLD + Main.getLanguage().getLanguageConfig().getString("menu.main.categories", "Categories"))
                .build(),
                p -> {
                    // Open categories menu here
                    p.openInventory(MenuManager.categoriesMenu.getInventory());
                }));

        menuItems.add(2, new MenuItem(otherHeadBuilder.build(),
                p -> {
                    // Give someone else's head here
                    msg.info(p, Main.getLanguage().getLanguageConfig().getString("give.other.help", "§aUse: §e/craftheads <player name>"));
                    p.closeInventory();
                }));

        placeItems();

    }

    // x x X x X x X x x
    @Override
    protected void placeItems() {
        inventory.setItem(2, menuItems.get(0).getItemStack());

        inventory.setItem(4, menuItems.get(1).getItemStack());

        inventory.setItem(6, menuItems.get(2).getItemStack());
    }


}
