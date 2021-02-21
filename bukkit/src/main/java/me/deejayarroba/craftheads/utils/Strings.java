package me.deejayarroba.craftheads.utils;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Useful methods for strings.
 *
 * @author TigerHix
 */
public final class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static @Nonnull
    String format(@Nonnull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static @Nonnull
    String[] format(@Nonnull String[] strings) {
        return format(Arrays.asList(strings)).toArray(new String[strings.length]);
    }

    public static @Nonnull
    List<String> format(@Nonnull List<String> strings) {
        List<String> collection = new ArrayList<>();
        for (String string : strings) {
            collection.add(format(string));
        }
        return collection;
    }

    public static @Nonnull
    String repeat(@Nonnull String string, int count) {
        if (count == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(string);
        }
        return builder.toString();
    }
}
