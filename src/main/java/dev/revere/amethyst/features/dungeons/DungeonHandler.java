package dev.revere.amethyst.features.dungeons;

import dev.revere.amethyst.Locale;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.utils.inventory.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class DungeonHandler implements Listener {

    public DungeonHandler(Main core) {
        core.getServer().getPluginManager().registerEvents(this, core);
    }
    public HashMap<UUID, Instant> timeMap = new HashMap<>();
    public HashMap<UUID, Integer> killMap = new HashMap<>();


    public static final ItemStack DUNGEON_KEY = new ItemBuilder(Material.NAME_TAG).name("&2&lJungle Pass").lore(
            Arrays.asList("&8Jungle Item", "", "&7ꜰɪɴᴅ ᴛʜᴇ &fɴᴘᴄ &7ɪɴ ᴛʜᴇ &fᴡᴀʀᴢᴏɴᴇ &7ᴛᴏ ᴜsᴇ ᴛʜɪs ᴘᴀss", "&7ᴛᴏ ᴇɴᴛᴇʀ ᴀ &fᴅɪᴍᴇɴsɪᴏɴ &7ꜰɪʟʟᴇᴅ ᴡɪᴛʜ &fᴅᴀɴɢᴇʀ", "", "&7sʜᴏᴘ.sᴏʟᴛɪx.ʀɪᴘ")
    ).build();

    public boolean checkPass(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null)
                continue;

            if(item.isSimilar(DUNGEON_KEY))
                return true;
        }
        return false;
    }

    public void startDungeon(Player player) {
        ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.DUNGEON");

        Location location = new Location(
                Bukkit.getServer().getWorld("jungle"),
                section.getDouble("X"),
                section.getDouble("Y"),
                section.getDouble("Z"),
                Integer.valueOf(section.getInt("PITCH")).floatValue(),
                Integer.valueOf(section.getInt("YAW")).floatValue());

        player.teleportAsync(location);
        player.sendMessage(Locale.DUNGEON_TELEPORT.getColoredString());

        timeMap.put(player.getUniqueId(), Instant.now().plus(1800, ChronoUnit.SECONDS));
        killMap.remove(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(Locale.DUNGEON_EXPIRED.getColoredString());

                ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.SPAWN-POINT");
                Location location = new Location(
                        Bukkit.getServer().getWorld("world"),
                        section.getDouble("X"),
                        section.getDouble("Y"),
                        section.getDouble("Z"),
                        Integer.valueOf(section.getInt("PITCH")).floatValue(),
                        Integer.valueOf(section.getInt("YAW")).floatValue());

                player.teleportAsync(location);
                timeMap.remove(player.getUniqueId());
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 36000L);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if(killer == null || !inDungeon(killer.getUniqueId()))
            return;

        killMap.merge(killer.getUniqueId(), 1, Integer::sum);
    }

    public String getRemaining(UUID uuid) {
        long remaining = Instant.now().until(timeMap.get(uuid), ChronoUnit.SECONDS);

        long minutes = (remaining % 3600) / 60;
        long seconds = remaining % 60;

        return String.format("%2dm %2ds", minutes, seconds);
    }

    public boolean inDungeon(UUID uuid) {
        return timeMap.containsKey(uuid);
    }
}
