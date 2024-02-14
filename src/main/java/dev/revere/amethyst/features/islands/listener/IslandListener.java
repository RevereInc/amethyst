package dev.revere.amethyst.features.islands.listener;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.islands.IslandHandler;
import dev.revere.amethyst.profile.ProfileHandler;

public class IslandListener {

    private final IslandHandler islandHandler;
    private final ProfileHandler profileHandler;

    public IslandListener(Main core) {
        this.islandHandler = core.getIslandHandler();
        this.profileHandler = core.getProfileHandler();
    }
}
