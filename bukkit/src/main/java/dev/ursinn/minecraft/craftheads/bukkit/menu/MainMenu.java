package dev.ursinn.minecraft.craftheads.bukkit.menu;

import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends Menu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillColumn(2, ClickableItem.of(getOwnHeadItem(), (InventoryClickEvent inventoryClickEvent) -> {
            if (mainInstance.getEconomy() != null) {
                double balance = mainInstance.getEconomy().getBalance(player);
                if (balance < mainInstance.getOwnHeadPrice()) {
                    player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.NOT_ENOUGH_MONEY));
                    return;
                }
            }

            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.INVENTORY_FULL));
                return;
            }

            ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(player.getName()))
                    .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_NAME)
                            .replace("{headName}", player.getName()))
                    .build();
            player.getInventory().addItem(head);

            if (mainInstance.getEconomy() != null && mainInstance.getOwnHeadPrice() > 0) {
                mainInstance.getEconomy().withdrawPlayer(player, mainInstance.getOwnHeadPrice());
                player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_BUY)
                        .replace("{playerName}", player.getName())
                        .replace("{headPrice}", String.valueOf(mainInstance.getOwnHeadPrice())));
            }

            player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.HEAD_GIVE)
                    .replace("{playerName}", player.getName()));
            player.closeInventory();
        }));

        contents.fillColumn(4, ClickableItem.of(getCategoriesHeadItem(), (InventoryClickEvent inventoryClickEvent) ->
                MenuManager.CATEGORIES_MENU.open(player)));

        contents.fillColumn(6, ClickableItem.of(getOtherHeadItem(), (InventoryClickEvent inventoryClickEvent) -> {
            player.sendMessage(mainInstance.messageFormatter(CraftHeadsMessageKeys.MENU_MAIN_OTHER_HEAD_COMMAND));
            player.closeInventory();
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        //
    }

    private ItemStack getOwnHeadItem() {
        ItemBuilderBukkit item = new ItemBuilderBukkit(SkullBukkit.getSkullMaterial())
                .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.MENU_MAIN_OWN_HEAD_NAME))
                .setDurability((short) SkullType.PLAYER.ordinal());

        return addPriceLore(item, mainInstance.getOwnHeadPrice()).build();
    }

    private ItemStack getOtherHeadItem() {
        final String OTHER_PLAYER_ITEM_URL = "https://textures.minecraft.net/texture/f937e1c45bb8da29b2c564dd9a7da780dd2fe54468a5dfb4113b4ff658f043e1";

        ItemBuilderBukkit item = new ItemBuilderBukkit(SkullBukkit.getCustomSkull(OTHER_PLAYER_ITEM_URL))
                .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.MENU_MAIN_OTHER_HEAD_NAME))
                .addLore(mainInstance.messageFormatter(CraftHeadsMessageKeys.MENU_MAIN_OTHER_HEAD_LORE));

        return addPriceLore(item, mainInstance.getOtherHeadPrice()).build();
    }

    private ItemStack getCategoriesHeadItem() {
        final String CATEGORIES_ITEM_URL = "https://textures.minecraft.net/texture/3e8aad673157c92317a88b1f86f5271f1cd7397d7fc8ec3281f733f751634";

        ItemBuilderBukkit item = new ItemBuilderBukkit(SkullBukkit.getCustomSkull(CATEGORIES_ITEM_URL))
                .setName(mainInstance.messageFormatter(CraftHeadsMessageKeys.MENU_CATEGORIES_NAME));

        return item.build();
    }
}
