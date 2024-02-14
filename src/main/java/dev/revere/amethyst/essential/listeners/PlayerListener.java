package dev.revere.amethyst.essential.listeners;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.Locale;
import dev.revere.amethyst.features.generators.GeneratorType;
import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.ToolHandler;
import dev.revere.amethyst.features.tool.ToolRarity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final ToolHandler toolHandler;

    public PlayerListener(Main core) {
        this.toolHandler = core.getToolHandler();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.SPAWN-POINT");
        Player player = event.getPlayer();

        Location location = new Location(
                Bukkit.getServer().getWorld("world"),
                section.getDouble("X"),
                section.getDouble("Y"),
                section.getDouble("Z"),
                Integer.valueOf(section.getInt("PITCH")).floatValue(),
                Integer.valueOf(section.getInt("YAW")).floatValue()
        );

        player.teleportAsync(location);
        event.setJoinMessage(null);

        if (toolHandler.getTool(player.getUniqueId()).isEmpty()) {
            System.out.println("test");
            final Tool tool = toolHandler.createTool(
                    player.getUniqueId(),
                    ToolRarity.BASIC
            );

            player.getInventory().addItem(tool.getHoe());
            player.getInventory().addItem(GeneratorType.COBBLESTONE.itemStack());
            Bukkit.getServer().broadcastMessage(Locale.FIRST_JOIN.getColoredString()
                    .replace("<player>", event.getPlayer().getName())
                    .replace("<total>", String.valueOf(Bukkit.getOfflinePlayers().length)));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }


    /* handles portals for warzone & spawn teleportation */
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        Location location;

        switch (event.getCause()) {
            case END_PORTAL -> {
                ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.WARZONE");
                location = new Location(
                        Bukkit.getServer().getWorld("world"),
                        section.getDouble("X"),
                        section.getDouble("Y"),
                        section.getDouble("Z"),
                        Integer.valueOf(section.getInt("PITCH")).floatValue(),
                        Integer.valueOf(section.getInt("YAW")).floatValue());

                player.teleportAsync(location);
            }

            case NETHER_PORTAL -> {
                ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.SPAWN-POINT");
                location = new Location(
                        Bukkit.getServer().getWorld("world"),
                        section.getDouble("X"),
                        section.getDouble("Y"),
                        section.getDouble("Z"),
                        Integer.valueOf(section.getInt("PITCH")).floatValue(),
                        Integer.valueOf(section.getInt("YAW")).floatValue());

                player.teleport(location);
            }
        }
    }

    @EventHandler
    public void onVoidDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.SPAWN-POINT");

            Location location = new Location(
                    Bukkit.getServer().getWorld("world"),
                    section.getDouble("X"),
                    section.getDouble("Y"),
                    section.getDouble("Z"),
                    Integer.valueOf(section.getInt("PITCH")).floatValue(),
                    Integer.valueOf(section.getInt("YAW")).floatValue());

            player.teleport(location);
        }
    }
}
