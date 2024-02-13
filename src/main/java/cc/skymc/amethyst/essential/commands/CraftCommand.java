package cc.skymc.amethyst.essential.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("craft")
public class CraftCommand extends BaseCommand {

  @Default
  @CommandPermission("amethyst.craft")
  public void craft(Player sender) {
    sender.openWorkbench(sender.getLocation(), false);
  }
}