package me.deejayarroba.craftheads.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.ursinn.utils.bukkit.Utils;
import me.deejayarroba.craftheads.utils.Reflections;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public final class Skulls {

    private Skulls() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Return a skull that has a custom texture specified by url.
     *
     * @param url skin url
     * @return itemstack
     */
    public static @Nonnull
    ItemStack getCustomSkull(@Nonnull String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }

        String encodedData = Base64.getEncoder().encodeToString(
                String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes(Charset.defaultCharset()));
        propertyMap.put("textures", new Property("textures", encodedData));
        ItemStack head = new ItemStack(getSkullMaterial(), 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Return a skull of a player.
     *
     * @param name player's name
     * @return itemstack
     */
    public static @Nonnull
    ItemStack getPlayerSkull(@Nonnull String name) {
        ItemStack itemStack = new ItemStack(getSkullMaterial(), 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static @Nonnull
    List<String> get18Versions() {
        List<String> versions = new ArrayList<>();
        versions.add("v1_8_R1");
        versions.add("v1_8_R2");
        versions.add("v1_8_R3");
        versions.add("v1_9_R1");
        versions.add("v1_9_R2");
        versions.add("v1_10_R1");
        versions.add("v1_11_R1");
        versions.add("v1_12_R1");
        return versions;
    }

    public static @Nonnull
    Material getSkullMaterial() {
        if (get18Versions().contains(Utils.getNmsVersion())) {
            return Material.getMaterial("SKULL_ITEM");
        }

        return Material.getMaterial("LEGACY_SKULL_ITEM");
    }

    public static @Nonnull
    Material getPlayerSkullMaterial() {
        if (get18Versions().contains(Utils.getNmsVersion())) {
            return Material.getMaterial("SKULL_ITEM");
        }

        return Material.getMaterial("PLAYER_HEAD");
    }
}
