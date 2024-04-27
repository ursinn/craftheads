package me.deejayarroba.craftheads.menu;

import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.menutypes.CategoriesMenu;
import me.deejayarroba.craftheads.menu.menutypes.CategoryMenu;
import me.deejayarroba.craftheads.menu.menutypes.MainMenu;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    public static MainMenu mainMenu;
    public static CategoriesMenu categoriesMenu;
    public static ArrayList<CategoryMenu> categoryMenus = new ArrayList<>();
    @Getter
    private static List<Menu> menus = new ArrayList<>();

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

        for (int i = 0; i < Main.HEAD_CATEGORIES.size(); i++) {
            JSONObject category = (JSONObject) Main.HEAD_CATEGORIES.get(i);
            CategoryMenu categoryMenu = new CategoryMenu(category);
            categoryMenus.add(categoryMenu);
            add(categoryMenu);
        }
    }

    // Get a menu from its name
    public static Menu getMenu(String name) {
        for (Menu menu : getMenus()) {
            if (menu.getName().equals(name))
                return menu;
        }
        return null;
    }

    // Get a menu from its inventory
    public static Menu getMenu(Inventory inv) {
        for (Menu menu : getMenus()) {
            if (menu.getInventory().equals(inv))
                return menu;
        }
        return null;
    }

}