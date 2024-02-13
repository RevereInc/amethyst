package cc.skymc.amethyst.features.tool;

import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.utils.inventory.ItemBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class Tool {
  public UUID uuid;
  public ToolRarity rarity;
  public List<Enchant> enchants;

  public final Document toBson() {
    Document document = new Document("_id", uuid.toString());

    document.append("rarity", rarity.name());

    return document;
  }

  private ItemStack itemStack(Material material) {
    List<String> lores = new ArrayList<>();

    lores.add("");
    lores.add(rarity.color + "▎ &fRarity: " + PlaceholderAPI.setPlaceholders(null, "%img_" + rarity.icon + "%"));

    if(!this.enchants.isEmpty()) {
      lores.add("");
      lores.add(rarity.color + "Enchants:");

      for (Enchant enchant : this.enchants) {
        lores.add(rarity.color + "▎ &f" + enchant.name() + " " + enchant.level());
      }
    }

    lores.add("");
    lores.add("&7&o(( &f&oShift click &7&oopen the tool menu. ))");

    return new ItemBuilder(material)
        .name(rarity.color + "" + ChatColor.BOLD + rarity.name + " Omnitool")
        .lore(lores)
        .unbreakable(true)
        .build();
  }

  public ItemStack getHoe() { return itemStack(this.rarity.getItems().get(0)); }
  public ItemStack getPickaxe() { return itemStack(this.rarity.getItems().get(1)); }
  public ItemStack getRod() { return itemStack(this.rarity.getItems().get(2)); }

  public boolean isSimilar(ItemStack itemStack) {
    for(Material material : this.rarity.getItems()) {
      if(itemStack.isSimilar(itemStack(material)))
        return true;
    }

    return false;
  }

  public ItemStack getUpdatedItem(Material material) {
    return itemStack(material);
  }

}
