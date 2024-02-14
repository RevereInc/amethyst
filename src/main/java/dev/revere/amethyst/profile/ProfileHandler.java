package dev.revere.amethyst.profile;

import dev.revere.amethyst.Main;

import java.util.Optional;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileHandler {

    private final Main core;
    private final Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileHandler(Main core) {
        this.core = core;
    }

    private Profile createProfile(UUID uuid) {
        final Profile profile = new Profile(uuid);

        /*
        default multiplier amounts - otherwise
        we end up with a multiplier of (e.g 0.15)
        */

        profile.setMoneyMultiplier(1.0);
        profile.setOrbsMultiplier(1.0);
        profile.setXpMultiplier(1.0);


        core.getStorage().saveProfile(profile);
        profiles.put(uuid, profile);

        return profile;
    }

    public final void remove(Profile profile, boolean async) {
        core.getStorage().removeProfile(profile);
        profiles.remove(profile.getUuid());
    }

    public final Profile getProfile(UUID uuid) {
        if (profiles.containsKey(uuid))
            return profiles.get(uuid);

        Optional<Profile> profile = core.getStorage().loadProfile(uuid);
        if (profile.isPresent()) {
            profiles.put(uuid, profile.get());
            return profile.get();
        }

        return createProfile(uuid);
    }

}
