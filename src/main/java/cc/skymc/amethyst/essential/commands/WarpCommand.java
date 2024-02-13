package cc.skymc.amethyst.essential.commands;

import cc.skymc.amethyst.Locale;
import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.profile.ProfileHandler;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@CommandAlias("warp")
@RequiredArgsConstructor
public class WarpCommand extends BaseCommand {

  private final ProfileHandler profileHandler;

  public WarpCommand(Main core) {
    this.profileHandler = core.getProfileHandler();
  }

  @CommandAlias("pvp")
  public void pvp(Player sender) {
    ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.WARZONE");

    Location location = new Location(
        Bukkit.getServer().getWorld("world"),
        section.getDouble("X"),
        section.getDouble("Y"),
        section.getDouble("Z"),
        Integer.valueOf(section.getInt("PITCH")).floatValue(),
        Integer.valueOf(section.getInt("YAW")).floatValue());

    sender.teleportAsync(location);
    sender.sendMessage(Locale.WARZONE_TELEPORT.getColoredString());
  }

  @CommandAlias("farm")
  public void farm(Player sender) {
    Profile profile = profileHandler.getProfile(sender.getUniqueId());
    sender.sendMessage(Locale.FARM_TELEPORT.getColoredString());

    if(profile.getPrestige() >= 10) {
      ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.POTATO");

      Location location = new Location(
          Bukkit.getServer().getWorld("world"),
          section.getDouble("X"),
          section.getDouble("Y"),
          section.getDouble("Z"),
          Integer.valueOf(section.getInt("PITCH")).floatValue(),
          Integer.valueOf(section.getInt("YAW")).floatValue());

      sender.teleportAsync(location);
    } else if(profile.getPrestige() >= 3) {
      ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.CARROT");

      Location location = new Location(
          Bukkit.getServer().getWorld("world"),
          section.getDouble("X"),
          section.getDouble("Y"),
          section.getDouble("Z"),
          Integer.valueOf(section.getInt("PITCH")).floatValue(),
          Integer.valueOf(section.getInt("YAW")).floatValue());

      sender.teleportAsync(location);
    } else {
      ConfigurationSection section = Main.getInstance().getSettingsYML().getConfig().getConfigurationSection("LOCATIONS.WHEAT");

      Location location = new Location(
          Bukkit.getServer().getWorld("world"),
          section.getDouble("X"),
          section.getDouble("Y"),
          section.getDouble("Z"),
          Integer.valueOf(section.getInt("PITCH")).floatValue(),
          Integer.valueOf(section.getInt("YAW")).floatValue());

      sender.teleportAsync(location);
    }
  }

}
