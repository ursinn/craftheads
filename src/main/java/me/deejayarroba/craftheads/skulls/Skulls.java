package me.deejayarroba.craftheads.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import dev.ursinn.utils.bukkit.utils.UtilsBukkit;
import me.deejayarroba.craftheads.utils.Base64;
import me.deejayarroba.craftheads.utils.Reflections;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class Skulls {

    /**
     * Return a skull that has a custom texture specified by url.
     *
     * @param url skin url
     * @return itemstack
     */
    public static ItemStack getCustomSkull(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), "profile");
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        String encodedData = Base64.encodeBytes(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
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
    public static ItemStack getPlayerSkull(String name) {
        ItemStack itemStack = new ItemStack(getSkullMaterial(), 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Material getSkullMaterial() {
        if (SkullBukkit.get18Versions().contains(UtilsBukkit.getNmsVersion()))
            return Material.getMaterial("SKULL_ITEM");

        return Material.getMaterial("LEGACY_SKULL_ITEM");
    }

    public static Material getPlayerSkullMaterial() {
        if (SkullBukkit.get18Versions().contains(UtilsBukkit.getNmsVersion()))
            return Material.getMaterial("SKULL_ITEM");

        return Material.getMaterial("PLAYER_HEAD");
    }
}
