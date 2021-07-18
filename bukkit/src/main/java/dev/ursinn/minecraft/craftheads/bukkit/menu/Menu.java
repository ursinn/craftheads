package dev.ursinn.minecraft.craftheads.bukkit.menu;

import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import me.deejayarroba.craftheads.Main;

public class Menu {

    protected Main mainInstance;

    public Menu() {
        this.mainInstance = Main.getInstance();
    }

    protected ItemBuilderBukkit addPriceLore(ItemBuilderBukkit item, double price) {
        if (mainInstance.getEconomy() != null) {
            if (price > 0) {
                item.addLore(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_LORE_PRICE_VALUE)
                        .replace("{price}", String.valueOf(price)));
            } else {
                item.addLore(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_LORE_PRICE_FREE));
            }
        }

        return item;
    }
}
