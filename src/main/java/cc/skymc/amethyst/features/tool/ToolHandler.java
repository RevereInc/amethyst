package cc.skymc.amethyst.features.tool;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.features.tool.enchants.impl.HasteEnchant;
import cc.skymc.amethyst.features.tool.enchants.impl.CrystalFinderEnchant;
import cc.skymc.amethyst.features.tool.enchants.impl.OrbsBoosterEnchant;
import cc.skymc.amethyst.features.tool.enchants.impl.SpeedEnchant;

import java.util.*;

import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.utils.chat.Style;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ToolHandler {

  private final Main core;
  private final Map<UUID, Tool> tools = new HashMap<>();

  public ToolHandler(Main core) {
    this.core = core;
  }

  public final Tool createTool(UUID uuid, ToolRarity rarity) {
    final Tool tool = new Tool(uuid, rarity, Arrays.asList(new OrbsBoosterEnchant(3)));

    core.getStorage().saveTool(tool);
    tools.put(uuid, tool);

    return tool;
  }

  public final void remove(Tool tool) {
    core.getStorage().removeTool(tool);
    tools.remove(tool.getUuid());
  }

  public final Optional<Tool> getTool(UUID uuid) {
    if(tools.containsKey(uuid))
      return Optional.of(tools.get(uuid));

    Optional<Tool> tool = core.getStorage().loadTool(uuid);
    if(tool.isPresent())
      tools.put(uuid, tool.get());

    return tool;
  }

  public void purchaseEnchant(Player player, Tool tool, Enchant enchant) {
    List<Enchant> currentEnchants = new ArrayList<>(tool.getEnchants());

    // Extract enchant names from currentEnchants
    List<String> enchantNames = new ArrayList<>();
    currentEnchants.forEach(loopEnchant -> enchantNames.add(loopEnchant.name()));

    // Check if the enchant name is already in the list
    boolean enchantExists = enchantNames.contains(enchant.name());

    if (enchantExists) {
      // Enchantment exists, find it and increment the level
      for (Enchant currentEnchant : currentEnchants) {
        if (currentEnchant.name().equals(enchant.name())) {
          int newLevel = currentEnchant.level() + 1;

          if(newLevel > currentEnchant.maxLevel()) {
            player.sendMessage(Style.translate("&4&l[!] &cYou have reached the max level of that enchant."));
            return;
          }

          if(deductOrbsAmount(player, enchant.price()))
            currentEnchant.setLevel(newLevel);
          break; // No need to continue iterating once found
        }
      }
    } else {
      // Enchantment does not exist, add it to the tool
      if(deductOrbsAmount(player, enchant.price()))
        currentEnchants.add(enchant);

    }

    tool.setEnchants(currentEnchants);
  }

  public boolean deductOrbsAmount(Player player, long amount) {
    Profile profile = Main.getInstance().getProfileHandler().getProfile(player.getUniqueId());

    if(profile.getOrbs() < amount) {
      player.sendMessage(Style.translate("&4&l[!] &cYou do not have enough orbs to purchase that."));
      return false;
    }

    profile.setOrbs(profile.getOrbs() - amount);
    player.sendMessage(Style.translate("&2&l[!] &aYou have successfully purchased a level."));
    return true;
  }


}
