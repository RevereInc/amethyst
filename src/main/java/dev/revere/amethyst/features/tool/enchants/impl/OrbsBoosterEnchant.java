package dev.revere.amethyst.features.tool.enchants.impl;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.tool.enchants.Enchant;
import dev.revere.amethyst.profile.Profile;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class OrbsBoosterEnchant implements Enchant {
    private int level;

    public OrbsBoosterEnchant(int level) {
        this.level = level;
    }

    public String name() {
        return "Orbs Booster";
    }

    public String style() {
        return "&7";
    }

    public Material material() {
        return Material.ENDER_EYE;
    }

    public double chance() {
        return 100;
    }

    public long price() {
        return 20000L * level;
    }

    public int level() {
        return level;
    }

    public int maxLevel() {
        return 1000;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void onHold(PlayerItemHeldEvent event, boolean holding) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(event.getPlayer().getUniqueId());

        if (holding) {
            profile.setOrbsMultiplier(profile.getOrbsMultiplier() + (0.01 * level));
            return;
        }

        profile.setOrbsMultiplier(profile.getOrbsMultiplier() - (0.01 * level));
    }
}
