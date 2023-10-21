package me.deejayarroba.craftheads.menu;

import com.mojang.authlib.GameProfile;
import dev.ursinn.utils.bukkit.reflections.ReflectionsBukkit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@Getter
public abstract class Menu {

    protected String name;
    protected Inventory inventory;
    protected ArrayList<MenuItem> menuItems;

    public Menu() {
        menuItems = new ArrayList<>();
    }

    // Places the MenuItems in the inventory
    // Must be called in the constructor
    protected void placeItems() {
        int slotCount;
        int itemCount = getMenuItems().size();
        int rest = itemCount % 9;
        if (rest == 0)
            slotCount = itemCount;
        else
            slotCount = itemCount + (9 - rest);

        inventory = Bukkit.createInventory(null, slotCount, name);

        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem menuItem = menuItems.get(i);
            inventory.setItem(i, menuItem.getItemStack());
        }
    }

    public MenuItem getMenuItem(ItemStack itemStack) {
        for (MenuItem menuItem : menuItems) {
            ItemMeta itemStackItemMeta = itemStack.getItemMeta();
            Class<?> itemStackItemMetaClass = itemStackItemMeta.getClass();
            Object itemStackProfile = ReflectionsBukkit.getField(itemStackItemMetaClass, "profile", GameProfile.class)
                    .get(itemStackItemMeta);

            if (itemStackProfile == null) {
                return menuItem;
            }

            ItemMeta menuItemMeta = menuItem.getItemStack().getItemMeta();
            Class<?> menuItemMetaClass = menuItemMeta.getClass();
            Object menuItemProfile = ReflectionsBukkit.getField(menuItemMetaClass, "profile", GameProfile.class)
                    .get(menuItemMeta);

            if (itemStackProfile.equals(menuItemProfile)) {
                return menuItem;
            }
        }
        return null;
    }

}