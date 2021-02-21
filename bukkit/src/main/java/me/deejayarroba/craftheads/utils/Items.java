package me.deejayarroba.craftheads.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Useful methods for items.
 *
 * @author TigerHix
 */
public final class Items {

    private Items() {
        throw new IllegalStateException("Utility class");
    }

    public static ItemStackBuilder builder() {
        return new ItemStackBuilder();
    }

    public static ItemStackBuilder editor(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack);
    }

    public static class ItemStackBuilder {

        private final ItemStack itemStack;

        ItemStackBuilder() {
            itemStack = new ItemStack(Material.QUARTZ);
        }

        public ItemStackBuilder(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStackBuilder setMaterial(Material material) {
            itemStack.setType(material);
            return this;
        }

        public ItemStackBuilder setData(short data) {
            itemStack.setDurability(data);
            return this;
        }

        public ItemStackBuilder setName(String name) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name.equals("") ? " " : Strings.format(name));
            itemStack.setItemMeta(itemMeta);
            return this;
        }

        public ItemStackBuilder addLore(String... lore) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> original = itemMeta.getLore();
            if (original == null) {
                original = new ArrayList<>();
            }
            Collections.addAll(original, Strings.format(lore));
            itemMeta.setLore(original);
            itemStack.setItemMeta(itemMeta);
            return this;
        }

        public ItemStack build() {
            return itemStack;
        }
    }
}
