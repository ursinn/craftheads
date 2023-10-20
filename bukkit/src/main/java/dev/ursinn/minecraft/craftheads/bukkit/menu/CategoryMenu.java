package dev.ursinn.minecraft.craftheads.bukkit.menu;

import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;

public class CategoryMenu extends Menu implements InventoryProvider {

    private final JSONObject category;

    public CategoryMenu(JSONObject category) {
        this.category = Objects.requireNonNull(category);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        JSONArray heads = (JSONArray) category.get("Heads");

        int size = heads.size();
        ClickableItem[] items = new ClickableItem[size];

        for (int i = 0; i < size; i++) {
            final JSONObject head = (JSONObject) heads.get(i);
            final float price;
            if ((long) head.get("Price") > 0) {
                price = (long) head.get("Price");
            } else {
                price = mainInstance.getDefaultHeadPrice();
            }

            ItemBuilderBukkit item = new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) head.get("URL")))
                    .setName(ChatColor.AQUA + "" + ChatColor.BOLD + head.get("Name"));

            items[i] = ClickableItem.of(addPriceLore(item, price).build(), inventoryClickEvent -> {
                if (mainInstance.getEconomy() != null) {
                    double balance = mainInstance.getEconomy().getBalance(player);
                    if (balance < price) {
                        // Player can't afford the head
                        player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.NOT_ENOUGH_MONEY));
                        return;
                    }
                }

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.INVENTORY_FULL));
                    return;
                }

                if (mainInstance.getEconomy() != null && price > 0) {
                    // Player can afford the head
                    mainInstance.getEconomy().withdrawPlayer(player, price);
                    player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.ITEM_BUY)
                            .replace("{headPrice}", String.valueOf(price)));
                }
                ItemStack headItem = new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) head.get("URL")))
                        .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_NAME)
                                .replace("{headName}", head.get("Name").toString()))
                        .build();

                player.getInventory().addItem(headItem);
                player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.ITEM_GIVE)
                        .replace("{item}", item.getItemMeta().getDisplayName()));
            });
        }

        pagination.setItems(items);
        if (size <= 54) {
            pagination.setItemsPerPage(size);
        } else {
            pagination.setItemsPerPage(45);

            contents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> MenuManager.getCategoryMenu(category).open(player, pagination.previous().getPage())));
            contents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> MenuManager.getCategoryMenu(category).open(player, pagination.next().getPage())));
        }

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        //
    }
}
