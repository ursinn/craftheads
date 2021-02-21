package me.deejayarroba.craftheads.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MenuItem {

    private final ItemStack itemStack;
    private final MenuItemAction action;

    public MenuItem(@Nonnull ItemStack itemStack, @Nonnull MenuItemAction action) {
        this.itemStack = Objects.requireNonNull(itemStack);
        this.action = Objects.requireNonNull(action);
    }

    public @Nonnull
    ItemStack getItemStack() {
        return itemStack;
    }

    public @Nonnull
    String getName() {
        return itemStack.getItemMeta().getDisplayName();
    }

    public void executeAction(Player p) {
        action.execute(p);
    }
}
