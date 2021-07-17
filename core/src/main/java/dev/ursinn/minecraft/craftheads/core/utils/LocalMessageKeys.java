package dev.ursinn.minecraft.craftheads.core.utils;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

import java.util.Locale;

public enum LocalMessageKeys implements MessageKeyProvider {
    PREFIX,
    MENU_MAIN_NAME,
    MENU_MAIN_OWN_HEAD_NAME,
    MENU_MAIN_OTHER_HEAD_NAME,
    MENU_MAIN_OTHER_HEAD_LORE,
    MENU_MAIN_OTHER_HEAD_COMMAND,
    MENU_CATEGORIES_NAME,
    NOT_ENOUGH_MONEY,
    INVENTORY_FULL,
    HEAD_NAME,
    HEAD_BUY,
    HEAD_GIVE,
    HEAD_LORE_PRICE_VALUE,
    HEAD_LORE_PRICE_FREE,
    ITEM_BUY,
    ITEM_GIVE,
    UPDATE_AVAILABLE,
    ;

    private final MessageKey key = MessageKey.of("acf-craftheads." + this.name().toLowerCase(Locale.ENGLISH));

    @Override
    public MessageKey getMessageKey() {
        return key;
    }
}
