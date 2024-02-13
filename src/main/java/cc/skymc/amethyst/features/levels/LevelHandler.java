package cc.skymc.amethyst.features.levels;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.actionbar.ActionBarHandler;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.profile.ProfileHandler;
import cc.skymc.amethyst.utils.chat.Style;
import cc.skymc.amethyst.utils.config.BasicConfigurationFile;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;

public class LevelHandler {

  private final ProfileHandler profileHandler;
  private final BasicConfigurationFile settings;
  private final Main core;

  public LevelHandler(Main core) {
    this.core = core;
    this.profileHandler = core.getProfileHandler();
    this.settings = core.getSettingsYML();
  }

  public boolean levelup(UUID uuid) {
    Profile profile = profileHandler.getProfile(uuid);
    long cost = Math.round(300 * Math.pow(1.02, profile.getLevel()) * (profile.getPrestige() + 1));

    if(profile.getXp() < cost)
      return false;

    profile.setLevel(profile.getLevel() + 1);
    profile.setXp((profile.getXp() - cost));

    Bukkit.getPlayer(uuid).sendTitle(
        Style.translate(settings.getString("TITLES.LEVEL-UP.TITLE").replaceAll("<level>", String.valueOf(profile.getLevel()))),
        Style.translate(settings.getString("TITLES.LEVEL-UP.SUBTITLE").replaceAll("<level>", String.valueOf(profile.getLevel()))),
        1, 60, 1
    );

    return true;
  }

  public void gainXP(UUID uuid, double xp) {
    Profile profile = profileHandler.getProfile(uuid);
    if(profile.getLevel() >= 120) {
      Bukkit.getPlayer(uuid).sendTitle(
          Style.translate(settings.getString("TITLES.PRESTIGE-NEEDED.TITLE").replaceAll("<prestige>", String.valueOf(profile.getPrestige()))),
          Style.translate(settings.getString("TITLES.PRESTIGE-NEEDED.SUBTITLE").replaceAll("<prestige>", String.valueOf(profile.getPrestige()))),
          1, 60, 1
      );

      return;
    }

    profile.setXp(profile.getXp() + xp);

    long cost = Math.round(300 * Math.pow(1.02, profile.getLevel()) * (profile.getPrestige() + 1));

    if(profile.getXp() >= cost)
      levelup(uuid);

    core.getActionBarHandler().sendActionBar(Objects.requireNonNull(Bukkit.getPlayer(uuid)), xp);
  }

}
