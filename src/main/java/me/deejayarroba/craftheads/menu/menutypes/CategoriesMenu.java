package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CategoriesMenu extends Menu {

    public CategoriesMenu() {
        name = Main.getLanguage().getLanguageConfig().getString("menu.categories", "Categories");
        menuItems = new ArrayList<>();

        // Loop through all the categories
        for (Object o : Main.getCategories().getHeadCategories()) {
            final JSONObject category = (JSONObject) o;

            menuItems.add(new MenuItem(
                    new ItemBuilderBukkit(Skulls.getCustomSkull((String) category.get("URL")))
                            .setName(ChatColor.GOLD + (String) category.get("Name"))
                            .build(),
                    p -> {

                        for (CategoryMenu categoryMenu : MenuManager.categoryMenus) {
                            if (category.equals(categoryMenu.getCategory())) {
                                p.openInventory(categoryMenu.getInventory());
                            }
                        }

                    }));
        }

        placeItems();
    }
}
