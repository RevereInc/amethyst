package cc.skymc.amethyst.features.tool.enchants;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public interface Enchant {

  String name();
  String style();
  Material material();
  double chance();
  long price();
  int level();

  int maxLevel();

  void setLevel(int level); // New method to set the level

  default void onBreak(BlockBreakEvent event) {

  }
  default void onHold(PlayerItemHeldEvent event, boolean holding) {}

}
