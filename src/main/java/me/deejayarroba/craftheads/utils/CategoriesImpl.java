package me.deejayarroba.craftheads.utils;

import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Getter
public class CategoriesImpl implements Categories {

    private final JSONArray headCategories = new JSONArray();

    @Override
    public void loadFiles() {
        if (Main.getInstance().getConfig().getBoolean("reset-categories")) {
            extractFiles("categories/");
            Main.getInstance().getConfig().set("reset-categories", false);
            Main.getInstance().saveConfig();
        }

        load();
    }

    public void load() {
        JSONParser parser = new JSONParser();

        for (File file : Objects.requireNonNull(Main.getInstance().getDataFolder().listFiles())) {
            if (file.isDirectory()) {
                if ("categories".equals(file.getName())) {
                    for (File categoryFile : Objects.requireNonNull(file.listFiles())) {
                        if (categoryFile.isFile()) {
                            try {
                                headCategories.add(parser.parse(
                                        new String(Files.readAllBytes(categoryFile.toPath()), StandardCharsets.UTF_8)));
                            } catch (IOException | ParseException e) {
                                Main.getInstance().getLogger().warning(String.valueOf(e));
                            }
                        }
                    }
                }
            }
        }
    }

    private void extractFiles(String dir) {
        try {
            JarFile jarfile = new JarFile(Main.getInstance().getPluginFile());

            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(dir) && !dir.equals(name)) {
                    Main.getInstance().saveResource(name, true);
                }
            }
            jarfile.close();
        } catch (IOException e) {
            Main.getInstance().getLogger().warning(String.valueOf(e));
        }
    }
}
