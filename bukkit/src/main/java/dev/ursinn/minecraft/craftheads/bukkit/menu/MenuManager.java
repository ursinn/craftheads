package dev.ursinn.minecraft.craftheads.bukkit.menu;

import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import me.deejayarroba.craftheads.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MenuManager {

    private MenuManager() {
        throw new IllegalStateException("Utility class");
    }

    public static final SmartInventory MAIN_MENU = getInventory(CraftHeadsMessageKeys.MENU_MAIN_NAME, new MainMenu(), 1);
    public static final SmartInventory CATEGORIES_MENU = getInventory(CraftHeadsMessageKeys.MENU_CATEGORIES_NAME, new CategoriesMenu(), calcRows(Main.getInstance().getCategories().getHeadCategories().size()));

    public static SmartInventory getCategoryMenu(JSONObject category) {
        return getInventory((String) category.get("Name"), new CategoryMenu(category), calcRows(((JSONArray) category.get("Heads")).size()));
    }

    private static SmartInventory getInventory(CraftHeadsMessageKeys localMessageKeys, InventoryProvider inventoryProvider, int rows) {
        return getInventory(Main.getInstance().messageFormatter(localMessageKeys), inventoryProvider, rows);
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
