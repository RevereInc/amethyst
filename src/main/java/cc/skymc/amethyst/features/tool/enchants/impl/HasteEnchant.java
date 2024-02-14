package cc.skymc.amethyst.features.tool.enchants.impl;

import cc.skymc.amethyst.features.tool.enchants.Enchant;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteEnchant implements Enchant {

    private int level;

    public HasteEnchant(int level) {
        this.level = level;
    }

    public String name() {
        return "Haste";
    }

    public String style() {
        return "&7";
    }

    public Material material() {
        return Material.GOLD_INGOT;
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
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, false));
            return;
        }

        event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
