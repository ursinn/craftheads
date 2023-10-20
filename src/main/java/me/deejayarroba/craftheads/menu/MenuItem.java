package me.deejayarroba.craftheads.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuItem {

    @Getter
    private ItemStack itemStack;
    private MenuItemAction action;

    public MenuItem(ItemStack itemStack, MenuItemAction action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public String getName() {
        return itemStack.getItemMeta().getDisplayName();
    }

    public void executeAction(Player p) {
        action.execute(p);
    }

}