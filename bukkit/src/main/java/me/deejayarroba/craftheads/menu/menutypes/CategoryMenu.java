package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.minecraft.craftheads.core.utils.LocalMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryMenu extends Menu {

    @Getter
    private final JSONObject category;

    public CategoryMenu(JSONObject category) {
        this.category = Objects.requireNonNull(category);
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
                    itemStackBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_VALUE)
                            .replace("{price}", String.valueOf(price))));
                } else {
                    itemStackBuilder.addLore(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_LORE_PRICE_FREE)));
                }
            }

            final ItemStack itemStack = itemStackBuilder.build();

            menuItems.add(new MenuItem(itemStack, (Player p) -> {
                if (mainInstance.getEconomy() != null) {
                    double balance = mainInstance.getEconomy().getBalance(p);
                    if (balance < price) {
                        // Player can't afford the head
                        p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.NOT_ENOUGH_MONEY)));
                        return;
                    }
                }
                // If the inventory is full
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.INVENTORY_FULL)));
                } else {
                    if (mainInstance.getEconomy() != null && price > 0) {
                        // Player can afford the head
                        mainInstance.getEconomy().withdrawPlayer(p, price);
                        p.sendMessage(Main.getInstance().messageFormatter(
                                locales.getMessage(null, LocalMessageKeys.ITEM_BUY)
                                        .replace("{headPrice}", String.valueOf(price))));
                    }
                    ItemStack headItem = new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) head.get("URL")))
                            .setName(Main.getInstance().messageFormatter(locales.getMessage(null, LocalMessageKeys.HEAD_NAME)
                                    .replace("{headName}", head.get("Name").toString())))
                            .build();

                    p.getInventory().addItem(headItem);
                    p.sendMessage(Main.getInstance().messageFormatter(
                            locales.getMessage(null, LocalMessageKeys.ITEM_GIVE)
                                    .replace("{item}", itemStack.getItemMeta().getDisplayName())));
                }
            }
            ));
        }

        placeItems();
    }
}
