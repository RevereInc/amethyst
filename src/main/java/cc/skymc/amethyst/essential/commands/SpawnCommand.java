package cc.skymc.amethyst.essential.commands;

import cc.skymc.amethyst.Locale;
import cc.skymc.amethyst.Main;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
@RequiredArgsConstructor
public class SpawnCommand extends BaseCommand {

  @Default
  public void spawn(Player sender) {
    ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.SPAWN-POINT");

    Location location = new Location(
        Bukkit.getServer().getWorld("world"),
        section.getDouble("X"),
        section.getDouble("Y"),
        section.getDouble("Z"),
        Integer.valueOf(section.getInt("PITCH")).floatValue(),
        Integer.valueOf(section.getInt("YAW")).floatValue());

    sender.teleportAsync(location);
    sender.sendMessage(Locale.SPAWN_TELEPORT.getColoredString());
  }

}
