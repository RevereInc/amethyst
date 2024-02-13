package cc.skymc.amethyst.features.tool.menus;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.tool.Tool;
import cc.skymc.amethyst.features.tool.ToolHandler;
import cc.skymc.amethyst.features.tool.enchants.impl.*;
import cc.skymc.amethyst.utils.inventory.SkullCreator;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EnchantsMenu extends Menu {
    private ToolHandler toolHandler;
    private Tool tool;

    public EnchantsMenu(Player player, Tool tool) {
        super(player, "Manage Enchantments", 45);
        this.toolHandler = Main.getInstance().getToolHandler();
        this.tool = tool;
    }

    @Override
    public void tick() {
        List<String> enchantsLore = Arrays.asList(
                "&8ᴛᴏᴏʟ ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛ",
                "",
                "&2&lᴏᴘᴛɪᴏɴs",
                " &a• &fClick &7to purchase one level.",
                "",
                "&7&o(( &f&oClick &7&oto upgrade this enchantment &7&o))"
        );

        int slot = 19;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19"))
                .setDisplayName("&a&lsᴘᴇᴇᴅ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new SpeedEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTAwZDc1YThjYzAzZTU5YzQ3NWNhYzc2ODFiYjRlNmQwOGFjNTkyOTBkNWYyMzlkNjA1Njc2MmI0ZTU1ZjkxNSJ9fX0="))
                .setDisplayName("&a&lʜᴀsᴛᴇ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new HasteEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTEzODM1MmY0NzQ1ZTAyYzA5MzkxNDZkYmQzNjZlNjUzNWE3ZjRlZjM5NjUzMDA5YjVjMzljMjRiOTRkNGNhNyJ9fX0="))
                .setDisplayName("&a&lʟᴇᴠᴇʟ ꜰɪɴᴅᴇʀ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new LevelFinderEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI4MDMyOWRjMDNhZTU0MWE3YjNmYjdlMWNmZTYzYThkZDg1ZjNlY2I5N2YzNTNiMDkyYWQ2YzBlMGZiMWM5NSJ9fX0="))
                .setDisplayName("&a&lᴘᴏɪɴᴛ ꜰɪɴᴅᴇʀ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new OrbsBoosterEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1OGY2Njk4MGY1ODhhN2M2ODVlYmI2NWQyOTMyNTE3NDk0ZDgzN2ExZjkyNzY1MGVmYmEzNTQ0NzUwYTI5MSJ9fX0="))
                .setDisplayName("&a&lᴏʀʙs ʙᴏᴏsᴛᴇʀ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new CrystalFinderEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;



        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ4YTdlYTE5OGVjNGVmZDhiNTZiY2RhOGFhNDIzMDAzOWUwNGQxMzM4ZWU5OGZhODU4OTdiZDRmMzQyZDYzMiJ9fX0="))
                .setDisplayName("&a&lxᴘ ʙᴏᴏsᴛᴇʀ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new XPBoostEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;

        this.buttons[slot] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNhYzdhZWE0MDJjZTYyOWE0ODMzYTQ1MjI4ZGY0NWE1MDYyZDExMmYwZjgyNmMzYzM0M2NmMDY4MDkxY2JlYSJ9fX0="))
                .setDisplayName("&a&lᴋᴇʏ ꜰɪɴᴅᴇʀ")
                .setLore(enchantsLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);
                    toolHandler.purchaseEnchant(getPlayer(), tool, new KeyFinderEnchant(1));
                    getPlayer().setItemInHand(tool.getUpdatedItem(getPlayer().getItemInHand().getType()));
                }); slot += 1;
    }
}
