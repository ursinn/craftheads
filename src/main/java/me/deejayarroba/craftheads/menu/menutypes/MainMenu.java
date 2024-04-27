package me.deejayarroba.craftheads.menu.menutypes;

import dev.ursinn.utils.bukkit.builder.ItemBuilderBukkit;
import dev.ursinn.utils.bukkit.skull.SkullBukkit;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.deejayarroba.craftheads.Main;
import me.deejayarroba.craftheads.menu.Menu;
import me.deejayarroba.craftheads.menu.MenuManager;
import me.deejayarroba.craftheads.skulls.Skulls;
import me.deejayarroba.craftheads.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends Menu implements InventoryProvider {

    private static final String CATEGORIES_ITEM_URL = "http://textures.minecraft.net/texture/3e8aad673157c92317a88b1f86f5271f1cd7397d7fc8ec3281f733f751634";
    private static final String OTHER_PLAYER_ITEM_URL = "http://textures.minecraft.net/texture/f937e1c45bb8da29b2c564dd9a7da780dd2fe54468a5dfb4113b4ff658f043e1";

    MessageManager msg = MessageManager.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillColumn(2, ClickableItem.of(getOwnHeadItem(), (InventoryClickEvent inventoryClickEvent) -> {
            if (Main.getInstance().getEconomy() != null) {
                double balance = Main.getInstance().getEconomy().getBalance(player);
                if (balance < Main.getInstance().getOwnHeadPrice()) {
                    msg.bad(player, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.money.other", "You can't afford that head!")));
                    return;
                }
            }

            if (player.getInventory().firstEmpty() == -1) {
                msg.bad(player, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("error.inv", "Your inventory is full!")));
                return;
            }

            ItemStack head = new ItemBuilderBukkit(SkullBukkit.getPlayerSkull(player.getName()))
                    .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("item", "&6Head: &b%args0%")
                            .replaceAll("%args0%", player.getName())))
                    .build();

            player.getInventory().addItem(head);

            if (Main.getInstance().getEconomy() != null && Main.getInstance().getOwnHeadPrice() > 0) {
                Main.getInstance().getEconomy().withdrawPlayer(player, Main.getInstance().getOwnHeadPrice());
                msg.good(player, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("buy.own", "You bought your own head for &b%ownHeadPrice%")
                        .replaceAll("%ownHeadPrice%", String.valueOf(Main.getInstance().getOwnHeadPrice()))));
            } else {
                msg.good(player, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.own", "You now have your own head!")));
            }

            player.closeInventory();
        }));

        contents.fillColumn(4, ClickableItem.of(getCategoriesHeadItem(), (InventoryClickEvent inventoryClickEvent) ->
                MenuManager.CATEGORIES_MENU.open(player)));

        contents.fillColumn(6, ClickableItem.of(getOtherHeadItem(), (InventoryClickEvent inventoryClickEvent) -> {
            msg.info(player, ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("give.other.helper", "§aUse: §e/craftheads <player name>")));
            player.closeInventory();
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        //
    }

    private ItemStack getOwnHeadItem() {
        ItemBuilderBukkit item = new ItemBuilderBukkit(SkullBukkit.getSkullMaterial())
                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.own", "§6Get your own head")))
                .setDurability((short) SkullType.PLAYER.ordinal());

        return addPriceLore(item, Main.getInstance().getOwnHeadPrice()).build();
    }

    private ItemStack getOtherHeadItem() {
        ItemBuilderBukkit item = new ItemBuilderBukkit(Skulls.getCustomSkull(OTHER_PLAYER_ITEM_URL))
                .setName(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.other", "§6Get someone else's head")))
                .addLore(ChatColor.translateAlternateColorCodes('&', Main.getLanguage().getLanguageConfig().getString("menu.lore.other", "§a§oUse: §e§o/craftheads <player name>")));

        return addPriceLore(item, Main.getInstance().getOtherHeadPrice()).build();
    }

    private ItemStack getCategoriesHeadItem() {
        ItemBuilderBukkit item = new ItemBuilderBukkit(Skulls.getCustomSkull(CATEGORIES_ITEM_URL))
                .setName(ChatColor.GOLD + Main.getLanguage().getLanguageConfig().getString("menu.categories", "Categories"));

        return item.build();
    }
}