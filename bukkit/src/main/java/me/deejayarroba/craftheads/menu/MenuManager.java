package me.deejayarroba.craftheads.menu;

import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.menutypes.CategoriesMenu;
import me.deejayarroba.craftheads.menu.menutypes.CategoryMenu;
import me.deejayarroba.craftheads.menu.menutypes.MainMenu;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuManager {

    private static final List<Menu> menus = new ArrayList<>();
    private static final ArrayList<CategoryMenu> categoryMenus = new ArrayList<>();

    @Getter
    private static CategoriesMenu categoriesMenu;

    @Getter
    private static MainMenu mainMenu;

    private MenuManager() {
        throw new IllegalStateException("Utility class");
    }

    // Shortcut to add a menu
    private static void add(Menu menu) {
        menus.add(menu);
    }

    // Initialization: create all the menus here
    public static void setup() {
        mainMenu = new MainMenu();
        add(mainMenu);
        categoriesMenu = new CategoriesMenu();
        add(categoriesMenu);

        for (int i = 0; i < Main.getInstance().getCategories().getHeadCategories().size(); i++) {
            JSONObject category = (JSONObject) Main.getInstance().getCategories().getHeadCategories().get(i);
            CategoryMenu categoryMenu = new CategoryMenu(category);
            categoryMenus.add(categoryMenu);
            add(categoryMenu);
        }
    }

    // Get all the menus
    public static List<Menu> getMenus() {
        return Collections.unmodifiableList(menus);
    }

    // Get a menu from its name
    public static Menu getMenu(String name) {
        for (Menu menu : getMenus()) {
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        return null;
    }

    // Get a menu from its inventory
    public static Menu getMenu(Inventory inv) {
        for (Menu menu : getMenus()) {
            if (menu.getInventory().equals(inv)) {
                return menu;
            }
        }
        return null;
    }

    public static List<CategoryMenu> getCategoryMenus() {
        return Collections.unmodifiableList(categoryMenus);
    }
}
