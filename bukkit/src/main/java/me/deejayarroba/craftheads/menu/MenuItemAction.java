package me.deejayarroba.craftheads.menu;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface MenuItemAction {

    void execute(@Nonnull Player p);
}
