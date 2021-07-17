package dev.ursinn.minecraft.craftheads.bukkit.utils;

import dev.ursinn.minecraft.craftheads.core.utils.Categories;
import lombok.Getter;
import me.deejayarroba.craftheads.Main;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class CategoriesImpl implements Categories {

    @Getter
    private final JSONArray headCategories = new JSONArray();
    private final FileHelper fileHelper;
    private final Main mainInstance;

    public CategoriesImpl() {
        this.fileHelper = new FileHelper();
        mainInstance = Main.getInstance();
    }

    @Override
    public void loadFiles() {
        if (mainInstance.getConfig().getBoolean("reset-categories")) {
            fileHelper.extractFiles("categories/");
            mainInstance.getConfig().set("reset-categories", false);
            mainInstance.saveConfig();
        }

        load();
    }

    public void load() {
        JSONParser parser = new JSONParser();

        for (File file : Objects.requireNonNull(mainInstance.getDataFolder().listFiles())) {
            if (file.isDirectory()) {
                if ("categories".equals(file.getName())) {
                    for (File categoryFile : Objects.requireNonNull(file.listFiles())) {
                        if (categoryFile.isFile()) {
                            try {
                                headCategories.add(parser.parse(
                                        new String(Files.readAllBytes(categoryFile.toPath()), StandardCharsets.UTF_8)));
                            } catch (IOException | ParseException e) {
                                mainInstance.getLogger().warning(String.valueOf(e));
                            }
                        }
                    }
                }
            }
        }
    }
}
