package dev.revere.amethyst.features.tool.enchants.impl;

import dev.revere.amethyst.features.tool.enchants.Enchant;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class SpeedEnchant implements Enchant {

    private int level;

    public SpeedEnchant(int level) {
        this.level = level;
    }

    public String name() {
        return "Speed";
    }

    public String style() {
        return "&7";
    }

    public Material material() {
        return Material.SUGAR;
    }

    public double chance() {
        return 100;
    }

    public long price() {
        return 100000L * level;
    }

    public int level() {
        return level;
    }

    public int maxLevel() {
        return 3;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void onHold(PlayerItemHeldEvent event, boolean holding) {
        if (holding) {
            event.getPlayer().setWalkSpeed(0.2F + 0.1F * level);
            return;
        }

        event.getPlayer().setWalkSpeed(0.2F);
    }
}
