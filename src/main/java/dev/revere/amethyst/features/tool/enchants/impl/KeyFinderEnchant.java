package dev.revere.amethyst.features.tool.enchants.impl;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.tool.enchants.Enchant;
import dev.revere.amethyst.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public class KeyFinderEnchant implements Enchant {
    private int level;

    public KeyFinderEnchant(int level) {
        this.level = level;
    }

    public String name() {
        return "Key Finder";
    }

    public String style() {
        return "&7";
    }

    public Material material() {
        return Material.EMERALD_BLOCK;
    }

    public double chance() {
        return 0.01 * level;
    }

    public long price() {
        return 1000L * level;
    }

    public int level() {
        return level;
    }

    public int maxLevel() {
        return 500;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void onBreak(BlockBreakEvent event) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(event.getPlayer().getUniqueId());
        profile.setLevel(profile.getLevel() + 1);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give " + event.getPlayer().getName() + " Gathering 1");
    }
}