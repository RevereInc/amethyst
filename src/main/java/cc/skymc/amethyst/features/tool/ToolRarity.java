package cc.skymc.amethyst.features.tool;

import cc.skymc.amethyst.utils.inventory.ItemBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum ToolRarity {

  BASIC(ChatColor.GRAY, "basic", "Basic"),
  RARE(ChatColor.AQUA, "rare", "Rare"),
  EPIC(ChatColor.DARK_PURPLE, "epic", "Epic"),
  LEGENDARY(ChatColor.GOLD, "legendary", "Legendary"),
  MYTHIC(ChatColor.DARK_RED, "mythic", "Mythic");

  public ChatColor color;
  public String icon;
  public String name;

  public List<Material> getItems() {
    final List<Material> items = new ArrayList<>();
    switch (this) {
      case BASIC -> {
        items.add(Material.WOODEN_HOE);
        items.add(Material.WOODEN_PICKAXE);
      }

      case RARE -> {
        items.add(Material.STONE_HOE);
        items.add(Material.STONE_PICKAXE);
      }

      case EPIC -> {
        items.add(Material.IRON_HOE);
        items.add(Material.IRON_PICKAXE);
      }

      case LEGENDARY -> {
        items.add(Material.DIAMOND_HOE);
        items.add(Material.DIAMOND_PICKAXE);
      }

      case MYTHIC -> {
        items.add(Material.NETHERITE_HOE);
        items.add(Material.NETHERITE_PICKAXE);
      }
    }

    items.add(Material.FISHING_ROD);
    return items;
  }
}
