package cc.skymc.amethyst.features.tool.enchants;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.tool.Tool;
import cc.skymc.amethyst.features.tool.ToolHandler;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantListener implements Listener {

    private final ToolHandler toolHandler;

    public EnchantListener(Main core) {
        this.toolHandler = core.getToolHandler();
    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Optional<Tool> tool = toolHandler.getTool(player.getUniqueId());
        if (tool.isEmpty())
            return;

        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        for (Enchant enchant : tool.get().getEnchants()) {
            enchant.onHold(event, item != null && tool.get().isSimilar(item));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Optional<Tool> tool = toolHandler.getTool(player.getUniqueId());

        if (tool.isEmpty())
            return;

        for (Enchant enchant : tool.get().getEnchants()) {
            enchant.onBreak(event);
        }
    }
}
