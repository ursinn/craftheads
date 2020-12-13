package me.deejayarroba.craftheads.listeners;

import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuItem;
import me.deejayarroba.craftheads.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvClickEvent implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        // Check if the inventory is the menu
        if (MenuManager.getMenu(inventory) == null) {
            return;
        }

        // Cancel the event to prevent the user from placing an item in the menu
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        if (clickedItem.getType() == Material.AIR) {
            return;
        }

        // Check if the menu contains the clicked item
        Menu menu = MenuManager.getMenu(inventory);
        if (menu == null) {
            return;
        }

        if (!menu.getInventory().contains(clickedItem)) {
            return;
        }

        MenuItem menuItem = menu.getMenuItem(clickedItem);
        // Execute the menu item's action
        Player p = (Player) event.getWhoClicked();
        menuItem.executeAction(p);
    }
}
