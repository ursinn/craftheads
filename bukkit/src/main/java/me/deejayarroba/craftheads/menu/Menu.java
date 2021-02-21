package me.deejayarroba.craftheads.menu;

import com.mojang.authlib.GameProfile;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    protected String name;
    protected Inventory inventory;
    protected ArrayList<MenuItem> menuItems;
    protected Main mainInstance;

    public Menu() {
        menuItems = new ArrayList<>();
        mainInstance = Main.getInstance();
    }

    // Places the MenuItems in the inventory
    // Must be called in the constructor
    protected void placeItems() {
        int slotCount;
        int itemCount = getMenuItems().size();
        int rest = itemCount % 9;
        if (rest == 0) {
            slotCount = itemCount;
        } else {
            slotCount = itemCount + (9 - rest);
        }

        inventory = Bukkit.createInventory(null, slotCount, name);

        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem menuItem = menuItems.get(i);
            inventory.setItem(i, menuItem.getItemStack());
        }
    }

    public @Nonnull
    List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public @Nonnull
    MenuItem getMenuItem(@Nonnull ItemStack itemStack) {
        for (MenuItem menuItem : menuItems) {
            ItemMeta itemStackItemMeta = itemStack.getItemMeta();
            Class<?> itemStackItemMetaClass = itemStackItemMeta.getClass();
            Object itemStackProfile = Reflections.getField(itemStackItemMetaClass, "profile", GameProfile.class)
                    .get(itemStackItemMeta);

            if (itemStackProfile == null) {
                return menuItem;
            }

            ItemMeta menuItemMeta = menuItem.getItemStack().getItemMeta();
            Class<?> menuItemMetaClass = menuItemMeta.getClass();
            Object menuItemProfile = Reflections.getField(menuItemMetaClass, "profile", GameProfile.class)
                    .get(menuItemMeta);

            if (itemStackProfile.equals(menuItemProfile)) {
                return menuItem;
            }
        }
        return null;
    }

    public @Nonnull
    Inventory getInventory() {
        return inventory;
    }

    public @Nonnull
    String getName() {
        return name;
    }
}