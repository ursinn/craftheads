package dev.ursinn.minecraft.craftheads.core.utils;

public interface Language {

    void createLanguageFile();

    String getMessage(String key, String defaultMessage);
}
