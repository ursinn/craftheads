package me.deejayarroba.craftheads.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.deejayarroba.craftheads.utils.Base64;
import me.deejayarroba.craftheads.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class Skulls {

    private final String id;

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
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
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

    public static ArrayList<String> get1_8Versions() {
        ArrayList<String> versions = new ArrayList<>();
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

    public static Material getSkullMaterial() {
        if (get1_8Versions().contains(getNmsVersion()))
            return Material.getMaterial("SKULL_ITEM");

        return Material.getMaterial("LEGACY_SKULL_ITEM");
    }

    public static Material getPlayerSkullMaterial() {
        if (get1_8Versions().contains(getNmsVersion()))
            return Material.getMaterial("SKULL_ITEM");

        return Material.getMaterial("PLAYER_HEAD");
    }


    public static String getNmsVersion() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        return ver.substring(ver.lastIndexOf('.') + 1);
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
        ItemStack itemStack = new ItemStack(getSkullMaterial(), 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(id);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
