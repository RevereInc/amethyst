package dev.revere.amethyst.hook;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.chat.Style;
import dev.revere.amethyst.utils.formatter.MathUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Main core;

    public PlaceholderAPIHook(Main core) {
        this.core = core;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "amethyst";
    }


    @Override
    public @NotNull String getAuthor() {
        return "Revere Development";
    }


    @Override
    public @NotNull String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        final Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        switch (params) {
            case "level":
                return String.valueOf(profile.getLevel());
            case "balance":
                return String.valueOf(profile.getBalance());
            case "orbs":
                return String.valueOf(profile.getOrbs());
            case "crystals":
                return String.valueOf(profile.getCrystals());
            case "level_formatted":
                return MathUtils.formatNumber(profile.getLevel());
            case "balance_formatted":
                return MathUtils.formatNumber(profile.getBalance());
            case "orbs_formatted":
                return MathUtils.formatNumber(profile.getOrbs());
            case "crystals_formatted":
                return MathUtils.formatNumber(profile.getCrystals());
            case "prestige":
                return String.valueOf(profile.getPrestige());
            case "prestige_formatted":
                if (profile.getPrestige() < 1)
                    return "";
                else
                    return Style.translate("&7(" + profile.getPrestige() + ")");
        }

        return null;
    }
}
