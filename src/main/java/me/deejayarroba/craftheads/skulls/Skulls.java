package me.deejayarroba.craftheads.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.ursinn.utils.bukkit.reflections.ReflectionsBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.charset.Charset;
import java.util.Base64;
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
        String encodedData = Base64.getEncoder().encodeToString(
                String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes(Charset.defaultCharset()));
        propertyMap.put("textures", new Property("textures", encodedData));
        ItemStack head = new ItemStack(SkullBukkit.getSkullMaterial(), 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        ReflectionsBukkit.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }
}
