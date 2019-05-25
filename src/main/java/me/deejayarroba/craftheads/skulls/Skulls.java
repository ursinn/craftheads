package me.deejayarroba.craftheads.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.deejayarroba.craftheads.util.Base64;
import me.deejayarroba.craftheads.util.Reflections;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class Skulls {

    private String id;

    private Skulls(String id) {
        this.id = id;
    }

    /**
     * Return a skull that has a custom texture specified by url.
     *
     * @param url skin url
     * @return itemstack
     */
    public static ItemStack getCustomSkull(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        String encodedData = Base64.encodeBytes(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", encodedData));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
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
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Return the skull's id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Return the skull of the enum.
     *
     * @return itemstack
     */
    public ItemStack getSkull() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(id);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
