package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class CategoriesMenu extends Menu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        int size = Main.getInstance().getCategories().getHeadCategories().size();
        ClickableItem[] items = new ClickableItem[size];

        for (int i = 0; i < size; i++) {
            JSONObject category = (JSONObject) Main.getInstance().getCategories().getHeadCategories().get(i);
            items[i] = ClickableItem.of(new ItemBuilderBukkit(SkullBukkit.getCustomSkull((String) category.get("URL")))
                    .setName(ChatColor.GOLD + (String) category.get("Name"))
                    .build(), (InventoryClickEvent inventoryClickEvent) -> MenuManager.getCategoryMenu(category).open(player));
        }

        pagination.setItems(items);
        if (size <= 54) {
            pagination.setItemsPerPage(size);
        } else {
            pagination.setItemsPerPage(45);

            contents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> MenuManager.CATEGORIES_MENU.open(player, pagination.previous().getPage())));
            contents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> MenuManager.CATEGORIES_MENU.open(player, pagination.next().getPage())));
        }

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        //
    }
}
