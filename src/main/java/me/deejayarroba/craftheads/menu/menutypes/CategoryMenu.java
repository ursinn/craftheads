package me.deejayarroba.craftheads.menu.menutypes;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.util.Items;
import me.deejayarroba.craftheads.util.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CategoryMenu extends Menu {

    JSONObject category;
    MessageManager msg = MessageManager.getInstance();

    public CategoryMenu(JSONObject category) {
        this.category = category;
        name = (String) category.get("Name");
        menuItems = new ArrayList<>();

        JSONArray heads = (JSONArray) category.get("Heads");

        for (Object o : heads) {
            final JSONObject head = (JSONObject) o;
            final float price = (long) head.get("Price") > 0 ? (long) head.get("Price") : Main.defaultHeadPrice;

            Items.ItemStackBuilder itemStackBuilder = Items.editor(Skulls.getCustomSkull((String) head.get("URL")))
                    .setName(ChatColor.AQUA + "" + ChatColor.BOLD + head.get("Name"));

            if (Main.economy != null) {
                if (price > 0) {
                    itemStackBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.price", "&bPrice: &a%price%").replaceAll("%price%", String.valueOf(price))));
                } else {
                    itemStackBuilder.addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.buy.free", "&bPrice: &aFREE")));
                }
            }

            final ItemStack itemStack = itemStackBuilder.build();

            menuItems.add(new MenuItem(itemStack, p -> {
                ItemStack headItem = Items.editor(Skulls.getCustomSkull((String) head.get("URL")))
                        .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("item", "&6Head: &b%args0%").replaceAll("%args0%", head.get("Name").toString())))
                        .build();

                if (Main.economy != null) {
                    double balance = Main.economy.getBalance(p);
                    if (balance < price) {
                        // Player can't afford the head
                        msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.money.other", "You can't afford that head!")));
                        return;
                    }
                }
                // If the inventory is full
                if (p.getInventory().firstEmpty() == -1) {
                    msg.bad(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.inv", "Your inventory is full!")));
                } else {
                    if (Main.economy != null && price > 0) {
                        // Player can afford the head
                        Main.economy.withdrawPlayer(p, price);
                        msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.item.buy", "You bought a head for &b%price%").replaceAll("%price%", String.valueOf(price))));
                    }
                    p.getInventory().addItem(headItem);
                    msg.good(p, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.item.give", "You now have: %item%").replaceAll("%item%", itemStack.getItemMeta().getDisplayName())));
                }
            }
            ));
        }

        placeItems();

    }

    public JSONObject getCategory() {
        return category;
    }

}
