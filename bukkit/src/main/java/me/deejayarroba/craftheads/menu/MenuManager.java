package me.deejayarroba.craftheads.menu;

import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.menutypes.CategoriesMenu;
import me.deejayarroba.craftheads.menu.menutypes.CategoryMenu;
import me.deejayarroba.craftheads.menu.menutypes.MainMenu;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuManager {

    private static final List<Menu> menus = new ArrayList<>();
    private static final ArrayList<CategoryMenu> categoryMenus = new ArrayList<>();
    private static CategoriesMenu categoriesMenu;
    private static MainMenu mainMenu;

    private MenuManager() {
        throw new IllegalStateException("Utility class");
    }

    // Shortcut to add a menu
    private static void add(@Nonnull Menu menu) {
        menus.add(menu);
    }

    // Initialization: create all the menus here
    public static void setup() {
        mainMenu = new MainMenu();
        add(mainMenu);
        categoriesMenu = new CategoriesMenu();
        add(categoriesMenu);

        for (int i = 0; i < Main.getInstance().getHeadCategories().size(); i++) {
            JSONObject category = (JSONObject) Main.getInstance().getHeadCategories().get(i);
            CategoryMenu categoryMenu = new CategoryMenu(category);
            categoryMenus.add(categoryMenu);
            add(categoryMenu);
        }
    }

    // Get all the menus
    public static @Nonnull List<Menu> getMenus() {
        return Collections.unmodifiableList(menus);
    }

    // Get a menu from its name
    public static @Nullable
    Menu getMenu(@Nonnull String name) {
        for (Menu menu : getMenus()) {
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        return null;
    }

    // Get a menu from its inventory
    public static @Nullable Menu getMenu(@Nonnull Inventory inv) {
        for (Menu menu : getMenus()) {
            if (menu.getInventory().equals(inv)) {
                return menu;
            }
        }
        return null;
    }


    public static @Nonnull List<CategoryMenu> getCategoryMenus() {
        return Collections.unmodifiableList(categoryMenus);
    }

    public static @Nonnull CategoriesMenu getCategoriesMenu() {
        return categoriesMenu;
    }

    public static @Nonnull MainMenu getMainMenu() {
        return mainMenu;
    }
}
