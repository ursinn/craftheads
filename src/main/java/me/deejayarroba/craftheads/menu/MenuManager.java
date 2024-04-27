package me.deejayarroba.craftheads.menu;

import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.menutypes.CategoriesMenu;
import me.deejayarroba.craftheads.menu.menutypes.CategoryMenu;
import me.deejayarroba.craftheads.menu.menutypes.MainMenu;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    private MenuManager() {
        throw new IllegalStateException("Utility class");
    }

    public static final SmartInventory MAIN_MENU = getInventory(Main.getLanguage().getLanguageConfig().getString("menu.name", "CraftHeads menu"), new MainMenu(), 1);
    public static final SmartInventory CATEGORIES_MENU = getInventory(Main.getLanguage().getLanguageConfig().getString("menu.categories", "Categories"), new CategoriesMenu(), calcRows(Main.getInstance().getCategories().getHeadCategories().size()));

    public static SmartInventory getCategoryMenu(JSONObject category) {
        return getInventory((String) category.get("Name"), new CategoryMenu(category), calcRows(((JSONArray) category.get("Heads")).size()));
    }

    private static SmartInventory getInventory(String title, InventoryProvider inventoryProvider, int rows) {
        return SmartInventory.builder()
                .provider(inventoryProvider)
                .size(rows, 9)
                .title(title)
                .manager(Main.getInstance().getInventoryManager())
                .build();
    }

    private static int calcRows(int size) {
        if (size > 54) {
            return 6;
        }
        int rest = size % 9;
        if (rest == 0) {
            return size / 9;
        }
        return (size / 9) + 1;
    }

}