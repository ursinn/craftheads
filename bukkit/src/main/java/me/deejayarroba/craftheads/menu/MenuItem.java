package me.deejayarroba.craftheads.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MenuItem {

    private final ItemStack itemStack;
    private final MenuItemAction action;

    public MenuItem(ItemStack itemStack, MenuItemAction action) {
        this.itemStack = Objects.requireNonNull(itemStack);
        this.action = Objects.requireNonNull(action);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return itemStack.getItemMeta().getDisplayName();
    }

    public void executeAction(Player p) {
        action.execute(p);
    }
}
