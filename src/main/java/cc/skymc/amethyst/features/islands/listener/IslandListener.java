package cc.skymc.amethyst.features.islands.listener;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.islands.IslandHandler;
import cc.skymc.amethyst.profile.ProfileHandler;

public class IslandListener {

  private final IslandHandler islandHandler;
  private final ProfileHandler profileHandler;

  public IslandListener(Main core) {
    this.islandHandler = core.getIslandHandler();
    this.profileHandler = core.getProfileHandler();
  }

}
