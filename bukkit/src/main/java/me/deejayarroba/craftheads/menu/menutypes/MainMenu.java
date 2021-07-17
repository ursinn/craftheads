package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.minecraft.craftheads.core.utils.LocalMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MainMenu extends Menu {

    private static final String CATEGORIES_ITEM_URL =
            "https://textures.minecraft.net/texture/3e8aad673157c92317a88b1f86f5271f1cd7397d7fc8ec3281f733f751634";
    private static final String OTHER_PLAYER_ITEM_URL =
            "https://textures.minecraft.net/texture/f937e1c45bb8da29b2c564dd9a7da780dd2fe54468a5dfb4113b4ff658f043e1";

    public MainMenu() {
        name = Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_MAIN_NAME));
        menuItems = new ArrayList<>();
        inventory = Bukkit.createInventory(null, 9, name);

        run();
    }

    private void run() {
        final ItemBuilderBukkit ownHeadBuilder = new ItemBuilderBukkit(SkullBukkit.getSkullMaterial())
                .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_MAIN_OWN_HEAD_NAME)))
                .setDurability((short) SkullType.PLAYER.ordinal());

        ItemBuilderBukkit otherHeadBuilder = new ItemBuilderBukkit(SkullBukkit.getCustomSkull(OTHER_PLAYER_ITEM_URL))
                .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_MAIN_OTHER_HEAD_NAME)))
                .addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_MAIN_OTHER_HEAD_LORE)));

        final float ownHeadPrice = mainInstance.getConfig().getInt("player-own-head-price");
        final float otherHeadPrice = mainInstance.getConfig().getInt("player-other-head-price");

        if (mainInstance.getEconomy() != null) {

            if (ownHeadPrice > 0) {
                otherHeadBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_VALUE)
                        .replace("{price}", String.valueOf(ownHeadPrice))));
            } else {
                ownHeadBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_FREE)));
            }

            if (otherHeadPrice > 0) {
                otherHeadBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_VALUE)
                        .replace("{price}", String.valueOf(otherHeadPrice))));
            } else {
                otherHeadBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_FREE)));
            }
        }

        menuItems.add(0, new MenuItem(ownHeadBuilder.build(),
                (Player p) -> {
                    if (mainInstance.getEconomy() != null) {
                        double balance = mainInstance.getEconomy().getBalance(p);
                        if (balance < ownHeadPrice) {
                            // Player can't afford the head
                            p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.NOT_ENOUGH_MONEY)));
                            return;
                        }
                    }

                    // If the inventory is full
                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.INVENTORY_FULL)));
                    } else {
                        ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(p.getName()))
                                .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_NAME)
                                        .replace("{headName}", p.getName())))
                                .build();
                        p.getInventory().addItem(head);

                        if (mainInstance.getEconomy() != null && ownHeadPrice > 0) {
                            // Player can afford the head
                            mainInstance.getEconomy().withdrawPlayer(p, ownHeadPrice);
                            p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_BUY)
                                    .replace("{playerName}", p.getName())
                                    .replace("{headPrice}", String.valueOf(ownHeadPrice))));
                        }

                        p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_GIVE)
                                .replace("{playerName}", p.getName())));
                        p.closeInventory();
                    }
                }));

        menuItems.add(1, new MenuItem(new ItemBuilderBukkit(SkullBukkit.getCustomSkull(CATEGORIES_ITEM_URL))
                .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_CATEGORIES_NAME)))
                .build(),
                (Player p) -> {
                    // Open categories menu here
                    p.openInventory(MenuManager.getCategoriesMenu().getInventory());
                }));

        menuItems.add(2, new MenuItem(otherHeadBuilder.build(),
                (Player p) -> {
                    // Give someone else's head here
                    p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.MENU_MAIN_OTHER_HEAD_COMMAND)));
                    p.closeInventory();
                }));

        placeItems();
    }

    @Override
    protected void placeItems() {
        inventory.setItem(2, menuItems.get(0).getItemStack());

        inventory.setItem(4, menuItems.get(1).getItemStack());

        inventory.setItem(6, menuItems.get(2).getItemStack());
    }
}
