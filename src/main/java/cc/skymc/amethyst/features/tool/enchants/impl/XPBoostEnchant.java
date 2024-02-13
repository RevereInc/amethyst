package cc.skymc.amethyst.features.tool.enchants.impl;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.hook.provider.EconomyProvider;
import cc.skymc.amethyst.profile.Profile;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class XPBoostEnchant implements Enchant {
  private int level;

  public XPBoostEnchant(int level) {
    this.level = level;
  }

  public String name() { return "XP Boost"; }
  public String style() { return "&7"; }
  public Material material() { return Material.EXPERIENCE_BOTTLE; }
  public double chance() { return 100; }
  public long price() { return 1000L * level; }
  public int level() { return level; }
  public int maxLevel() { return 1000; }
  public void setLevel(int level) { this.level = level; }

  public void onHold(PlayerItemHeldEvent event, boolean holding) {
    Profile profile = Main.getInstance().getProfileHandler().getProfile(event.getPlayer().getUniqueId());

    if(holding) {
      profile.setXpMultiplier(profile.getXpMultiplier() + (0.003 * level));
      return;
    }

    profile.setXpMultiplier(profile.getXpMultiplier() - (0.003 * level));
  }

}
