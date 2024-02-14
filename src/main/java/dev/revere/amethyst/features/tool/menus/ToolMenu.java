package dev.revere.amethyst.features.tool.menus;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.ToolHandler;
import dev.revere.amethyst.features.tool.ToolRarity;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.profile.ProfileHandler;
import dev.revere.amethyst.utils.chat.Style;
import dev.revere.amethyst.utils.inventory.SkullCreator;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ToolMenu extends Menu {
    private final ProfileHandler profileHandler;
    private final ToolHandler toolHandler;
    private final Tool tool;

    public ToolMenu(Player player, Main core, Tool tool) {
        super(player, "Modify Tool", 36);
        this.tool = tool;
        profileHandler = core.getProfileHandler();
        toolHandler = core.getToolHandler();
    }

    @Override
    public void tick() {
        List<String> enchantsLore = Arrays.asList(
                "&8ᴛᴏᴏʟ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to upgrade &c&oenchantments",
                "&f&oon your &e&otool &f&oto create &c&ooverpowered &f&ocombinations.",
                "",
                "&7&o(( &f&oClick &7&oto upgrade your enchants &7&o))"
        );

        this.buttons[20] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJ"
                + "lcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmNzkwMTZjYWQ4NGQxYW"
                + "UyMTYwOWM0ODEzNzgyNTk4ZTM4Nzk2MWJlMTNjMTU2ODI3NTJmMTI2Z"
                + "GNlN2EifX19"))

                .setDisplayName("&c&lᴜᴘɢʀᴀᴅᴇ ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    Profile profile = profileHandler.getProfile(event.getWhoClicked().getUniqueId());
                    new EnchantsMenu(getPlayer(), tool).updateMenu();
                });

        List<String> swapToolLore = Arrays.asList(
                "&8ᴛᴏᴏʟ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to upgrade &c&oenchantments",
                "&f&oon your &e&otool &f&oto create &c&ooverpowered &f&ocombinations.",
                "",
                "&7&o(( &f&oClick &7&oto upgrade your enchants &7&o))"
        );

        this.buttons[22] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJ"
                + "lcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmNzkwMTZjYWQ4NGQxYW"
                + "UyMTYwOWM0ODEzNzgyNTk4ZTM4Nzk2MWJlMTNjMTU2ODI3NTJmMTI2Z"
                + "GNlN2EifX19"))

                .setDisplayName("&2&lsᴡᴀᴘ ᴛᴏᴏʟ")
                .setLore(swapToolLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    String typeName = getPlayer().getItemInHand().getType().name();

                    if (typeName.endsWith("_HOE")) {
                        getPlayer().setItemInHand(tool.getPickaxe());
                    } else if (typeName.endsWith("_PICKAXE")) {
                        getPlayer().setItemInHand(tool.getRod());
                    } else if (typeName.endsWith("_ROD")) {
                        getPlayer().setItemInHand(tool.getHoe());
                    }
                });

        List<String> upgradeLore = Arrays.asList(
                "&8ᴛᴏᴏʟ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to upgrade your &c&otool",
                "&f&oto the next tier for a boost in &a&ostats &f&oand &c&oenchants&f&o.",
                "",
                "&7&o(( &f&oClick &7&oto upgrade your generator &7&o))"
        );

        this.buttons[24] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh"
                + "0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhm"
                + "Y2EzNDVlNGZkOWM0MjJiNzNjNGMxYzUwNTZmMzc5Z"
                + "GU5MjUxMGZjOTRiNGNjOTA3ZmIyMGNlNzUwZGM5MCJ9fX0="))
                .setDisplayName("&6&lᴜᴘɢʀᴀᴅᴇ ᴛᴏᴏʟ").setLore(upgradeLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);

                    switch (tool.getRarity()) {
                        case BASIC -> tool.setRarity(ToolRarity.RARE);
                        case RARE -> tool.setRarity(ToolRarity.EPIC);
                        case EPIC -> tool.setRarity(ToolRarity.LEGENDARY);
                        case LEGENDARY -> tool.setRarity(ToolRarity.MYTHIC);
                    }

                    String typeName = getPlayer().getItemInHand().getType().name();

                    if (typeName.endsWith("_HOE")) {
                        getPlayer().setItemInHand(tool.getHoe());
                    } else if (typeName.endsWith("_PICKAXE")) {
                        getPlayer().setItemInHand(tool.getPickaxe());
                    } else if (typeName.endsWith("_ROD")) {
                        getPlayer().setItemInHand(tool.getRod());
                    }
                });


        ItemStack item = getPlayer().getItemInHand();
        if (item.getType() == Material.AIR) {
            this.buttons[13] = new Button(Material.BARRIER).setDisplayName(Style.translate("&c&lɴᴏ ᴛᴏᴏʟ ꜰᴏᴜɴᴅ"));
            return;
        }

        this.buttons[13] = new Button(item.getType()).setDisplayName(item.getItemMeta().getDisplayName()).setLore(item.getLore().toArray(new String[0]));

    }
}
