package dev.ursinn.utils.bukkit;

import com.google.common.reflect.ClassPath;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @author Ursin Filli
 * @version 1.0
 */
@API(status = API.Status.MAINTAINED)
public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Register Listeners from Package
     *
     * @param packageName Package Name
     * @param plugin      Plugin Instance
     */
    public static void registerListener(@Nonnull String packageName, @Nonnull Plugin plugin) {
        PluginManager pluginManager = Objects.requireNonNull(plugin).getServer().getPluginManager();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getTopLevelClasses(Objects.requireNonNull(packageName))) {
                Class<Listener> clazz = (Class<Listener>) Class.forName(classInfo.getName());
                if (Listener.class.isAssignableFrom(clazz)) {
                    pluginManager.registerEvents(clazz.getDeclaredConstructor().newInstance(), plugin);
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            plugin.getLogger().warning(String.valueOf(exception));
        }
    }

    /**
     * @return Get NMS Version
     */
    public static @Nonnull
    String getNmsVersion() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        return ver.substring(ver.lastIndexOf('.') + 1);
    }

    /**
     * Build String from String array
     *
     * @param args  String array
     * @param start Start position in array
     * @return String
     */
    public static @Nonnull
    String buildString(@Nonnull String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i == args.length - 1) {
                sb.append(args[i]);
            } else {
                sb.append(args[i]).append(" ");
            }
        }
        return sb.toString();
    }
}
