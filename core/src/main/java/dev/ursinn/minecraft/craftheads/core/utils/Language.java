package dev.ursinn.minecraft.craftheads.core.utils;

public interface Language {

    void loadFiles();
    String getMessage(String key, String defaultMessage);
}
