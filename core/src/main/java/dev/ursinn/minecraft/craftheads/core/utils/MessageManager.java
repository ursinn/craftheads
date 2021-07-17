package dev.ursinn.minecraft.craftheads.core.utils;

public interface MessageManager {

    void info(Object sender, String msg);
    void good(Object sender, String msg);
    void bad(Object sender, String msg);
    void msg(Object sender, Object color, String msg);
}
