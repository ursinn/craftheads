package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.minecraft.craftheads.core.utils.MessageManager;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.utils.MessageManagerImpl;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryMenu extends Menu {

    private final JSONObject category;
    private final MessageManager msg;

    public CategoryMenu(JSONObject category) {
        this.category = Objects.requireNonNull(category);
        this.msg = MessageManagerImpl.getInstance();
        name = (String) category.get("Name");
        menuItems = new ArrayList<>();

        run();
    }

    private void run() {
        JSONArray heads = (JSONArray) category.get("Heads");

        for (Object o : heads) {
            final JSONObject head = (JSONObject) o;
            final float price;
            if ((long) head.get("Price") > 0) {
                price = (long) head.get("Price");
            } else {
                price = mainInstance.getDefaultHeadPrice();
            }

            ItemBuilderBukkit itemStackBuilder = new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) head.get("URL")))
                    .setName(ChatColor.AQUA + "" + ChatColor.BOLD + head.get("Name"));

            if (mainInstance.getEconomy() != null) {
                if (price > 0) {
                    itemStackBuilder.addLore(ChatColor.translateAlternateColorCodes('&',
                            mainInstance.getLanguage().getMessage("menu.buy.price",
                                    "&bPrice: &a%price%").replace("%price%", String.valueOf(price))));
                } else {
                    itemStackBuilder.addLore(ChatColor.translateAlternateColorCodes('&',
                            mainInstance.getLanguage().getMessage("menu.buy.free",
                                    "&bPrice: &aFREE")));
                }
            }

            final ItemStack itemStack = itemStackBuilder.build();

            menuItems.add(new MenuItem(itemStack, (Player p) -> {
                if (mainInstance.getEconomy() != null) {
                    double balance = mainInstance.getEconomy().getBalance(p);
                    if (balance < price) {
                        // Player can't afford the head
                        msg.bad(p, ChatColor.translateAlternateColorCodes('&',
                                mainInstance.getLanguage().getMessage("error.money.other",
                                        "You can't afford that head!")));
                        return;
                    }
                }
                // If the inventory is full
                if (p.getInventory().firstEmpty() == -1) {
                    msg.bad(p, ChatColor.translateAlternateColorCodes('&',
                            mainInstance.getLanguage().getMessage("error.inv",
                                    "Your inventory is full!")));
                } else {
                    if (mainInstance.getEconomy() != null && price > 0) {
                        // Player can afford the head
                        mainInstance.getEconomy().withdrawPlayer(p, price);
                        msg.good(p, ChatColor.translateAlternateColorCodes('&',
                                mainInstance.getLanguage().getMessage("give.item.buy",
                                        "You bought a head for &b%price%").replace("%price%", String.valueOf(price))));
                    }
                    ItemStack headItem = new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) head.get("URL")))
                            .setName(ChatColor.translateAlternateColorCodes('&',
                                    mainInstance.getLanguage().getMessage("item",
                                            "&6Head: &b%args0%").replace("%args0%", head.get("Name").toString())))
                            .build();

                    p.getInventory().addItem(headItem);
                    msg.good(p, ChatColor.translateAlternateColorCodes('&',
                            mainInstance.getLanguage().getMessage("give.item.give",
                                    "You now have: %item%")
                                    .replace("%item%", itemStack.getItemMeta().getDisplayName())));
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
