package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.utils.MessageManager;
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

        name = Main.getLanguage().getLanguageConfig().getString("menu.name", "CraftHeads menu");
        menuItems = new ArrayList<>();

        inventory = Bukkit.createInventory(null, 9, name);

        final ItemBuilderBukkit ownHeadBuilder = new ItemBuilderBukkit(SkullBukkit.getSkullMaterial())
                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.own", "§6Get your own head")))
            .setDurability((short) SkullType.PLAYER.ordinal());

        ItemBuilderBukkit otherHeadBuilder = new ItemBuilderBukkit(SkullBukkit.getCustomSkull(OTHER_PLAYER_ITEM_URL))
                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.other", "§6Get someone else's head")))
                .addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.lore.other", "§a§oUse: §e§o/craftheads <player name>")));

        final float ownHeadPrice = Main.getInstance().getConfig().getInt("player-own-head-price");
        final float otherHeadPrice = Main.getInstance().getConfig().getInt("player-other-head-price");

        if (Main.getInstance().getEconomy() != null) {

            if (ownHeadPrice > 0) {
                ownHeadBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.price", "&bPrice: &a%price%").replaceAll("%price%", String.valueOf(ownHeadPrice))));
            } else {
                ownHeadBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.free", "&bPrice: &aFREE")));
            }

            if (otherHeadPrice > 0) {
                otherHeadBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.price", "&bPrice: &a%price%").replaceAll("%price%", String.valueOf(otherHeadPrice))));
            } else {
                otherHeadBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.free", "&bPrice: &aFREE")));
            }
        }

        menuItems.add(0, new MenuItem(ownHeadBuilder.build(),
                p -> {
                    if (Main.getInstance().getEconomy() != null) {
                        double balance = Main.getInstance().getEconomy().getBalance(p);
                        if (balance < ownHeadPrice) {
                            // Player can't afford the head
                            msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.money.own", "You can't your afford your own head!")));
                            return;
                        }
                    }

                    // If the inventory is full
                    if (p.getInventory().firstEmpty() == -1) {
                        msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.inv", "Your inventory is full!")));
                    } else {
                        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(p.getName()))
                                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("item", "&6Head: &b%args0%").replaceAll("%args0%", p.getName())))
                                .build();
                        p.getInventory().addItem(head);

                        if (Main.getInstance().getEconomy() != null && ownHeadPrice > 0) {
                            // Player can afford the head
                            Main.getInstance().getEconomy().withdrawPlayer(p, ownHeadPrice);
                            msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("buy.own", "You bought your own head for &b%ownHeadPrice%").replaceAll("%ownHeadPrice%", String.valueOf(ownHeadPrice))));
                        }

                        msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.own", "You now have your own head!")));
                        p.closeInventory();
                    }
                }));

        menuItems.add(1, new MenuItem(new ItemBuilderBukkit(Skulls.getCustomSkull(CATEGORIES_ITEM_URL))
                .setName(ChatColor.GOLD + Main.getLanguage().getLanguageConfig().getString("menu.categories", "Categories"))
                .build(),
                p -> {
                    // Open categories menu here
                    p.openInventory(MenuManager.categoriesMenu.getInventory());
                }));

        menuItems.add(2, new MenuItem(otherHeadBuilder.build(),
                p -> {
                    // Give someone else's head here
                    msg.info(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.other.helper", "§aUse: §e/craftheads <player name>")));
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
