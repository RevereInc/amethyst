package dev.revere.amethyst.features.tool.listener;

import dev.revere.amethyst.Locale;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.Generator;
import dev.revere.amethyst.features.generators.menus.UpgradeMenu;
import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.ToolHandler;
import dev.revere.amethyst.features.tool.menus.ToolMenu;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.profile.ProfileHandler;
import io.netty.util.internal.ThreadLocalRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ToolListener implements Listener {

    private final ProfileHandler profileHandler;
    private final ToolHandler toolHandler;

    public ToolListener(Main core) {
        this.profileHandler = core.getProfileHandler();
        this.toolHandler = core.getToolHandler();
    }

    List<Material> minable = Arrays.asList(Material.COAL_ORE, Material.REDSTONE_ORE, Material.DIAMOND_ORE);

    List<Material> farmable = Arrays.asList(Material.WHEAT, Material.CARROTS, Material.POTATOES);

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Profile profile = profileHandler.getProfile(uuid);

        if (minable.contains(event.getBlock().getType())) {

            int levelRestriction = 0;
            double xp = 0;



            switch (event.getBlock().getType()) {
                case COAL_ORE -> { xp = 25; levelRestriction = 15; }
                case REDSTONE_ORE -> { xp = 45; levelRestriction = 30; }
                case DIAMOND_ORE -> { xp = 75; levelRestriction = 50; }
            }


            event.setCancelled(true);

            if (profile.getLevel() < levelRestriction) {
                player.sendMessage(Locale.MINING_RESTRICTED.getColoredString().replaceAll("<level>", String.valueOf(levelRestriction)));
                return;
            }


            /* multiply by player's personal xp modifier */
            Main.getInstance().getLevelHandler().gainXP(uuid, xp * profile.getXpMultiplier());
            profile.setOrbs((long) (profile.getOrbs() + 10L * profile.getOrbsMultiplier()));

            Material materialSnapshot = event.getBlock().getType();
            event.getBlock().setType(Material.BEDROCK);

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getBlock().setType(materialSnapshot);
                }
            }.runTaskLater(Main.getInstance(), 200L);
        }
    }

    @EventHandler
    public void onFarm(BlockBreakEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Profile profile = profileHandler.getProfile(uuid);

        Material material = event.getBlock().getType();
        // stupid hack, the material for carrot item isn't the same for carrot blocks...
        Material giveMaterial = null;
        int xp = 0;

        switch (material) {
            case WHEAT -> { xp = 2; giveMaterial = Material.WHEAT; }
            case CARROTS -> { xp = 6; giveMaterial = Material.CARROT; }
            case POTATOES -> { xp = 8; giveMaterial = Material.POTATO; }
        }

        if (farmable.contains(material)) {
            event.setCancelled(true);

            if (giveMaterial == null)
                return;

            /* multiply by player's personal xp modifier */
            Main.getInstance().getLevelHandler().gainXP(uuid, xp * profile.getXpMultiplier());
            event.getBlock().setType(Material.AIR);

            ItemStack itemStack = new ItemStack(giveMaterial);
            itemStack.setAmount(3);

            profile.setOrbs((long) (profile.getOrbs() + 10L * profile.getOrbsMultiplier()));

            if (profile.getAutoSell()) {
                double sellPrice = ShopGuiPlusApi.getItemStackPriceSell(itemStack);
                profile.setBalance(Math.round(profile.getBalance() + sellPrice));
            } else {
                event.getPlayer().getInventory().addItem(itemStack);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getBlock().setType(material);

                    BlockData blockData = event.getBlock().getBlockData();
                    Ageable age = (Ageable) blockData;
                    age.setAge(age.getMaximumAge());

                    event.getBlock().setBlockData(blockData);
                }
            }.runTaskLater(Main.getInstance(), 400L);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking())
            return;

        Optional<Tool> tool = toolHandler.getTool(player.getUniqueId());
        if (tool.isPresent()) {
            if (!tool.get().isSimilar(player.getInventory().getItemInMainHand()))
                return;

            new ToolMenu(player, Main.getInstance(), tool.get()).updateMenu();
            event.setCancelled(true);
        }
    }
}
