package me.deejayarroba.craftheads.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Useful methods for items.
 *
 * @author TigerHix
 */
public final class Items {

    private Items() {
        throw new IllegalStateException("Utility class");
    }

    public static @Nonnull
    ItemStackBuilder builder() {
        return new ItemStackBuilder();
    }

    public static @Nonnull ItemStackBuilder editor(@Nonnull ItemStack itemStack) {
        return new ItemStackBuilder(itemStack);
    }

    public static class ItemStackBuilder {

        private final ItemStack itemStack;

        ItemStackBuilder() {
            itemStack = new ItemStack(Material.QUARTZ);
        }

        public ItemStackBuilder(@Nonnull ItemStack itemStack) {
            this.itemStack = Objects.requireNonNull(itemStack);
        }

        public @Nonnull ItemStackBuilder setMaterial(@Nonnull Material material) {
            itemStack.setType(material);
            return this;
        }

        public @Nonnull ItemStackBuilder setData(@Nonnull short data) {
            itemStack.setDurability(data);
            return this;
        }

        public @Nonnull ItemStackBuilder setName(@Nonnull String name) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name.equals("") ? " " : Strings.format(name));
            itemStack.setItemMeta(itemMeta);
            return this;
        }

        public @Nonnull ItemStackBuilder addLore(@Nonnull String... lore) {
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

        public @Nonnull ItemStack build() {
            return itemStack;
        }
    }
}
