package cc.skymc.amethyst.features.levels.commands;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("level|levels")
@RequiredArgsConstructor
public class LevelCommand extends BaseCommand {

  private final Main core = Main.getInstance();
  private final List<String> helpMessage = Style.translate(Arrays.asList(
      Style.CHAT_BAR,
      "&2&lAvailable Level Commands",
      Style.CHAT_BAR,
      " &a• &2/level &7- Showcases this help message",
      " &a• &2/level set <player> <value> &7- Sets the level of a player.",
      " &a• &2/level add <player> <amount> &7- Add to the player's level.",
      " &a• &2/level setxp <player> <value> &7- Sets the xp of a player.",
      " &a• &2/level addxp <player> <amount> &7- Add to the player's xp.",
      Style.CHAT_BAR
  ));

  @Default
  @CommandPermission("core.economy.admin")
  public void execute(Player player) {
    helpMessage.forEach(line -> player.sendMessage(Style.translate(line)));
  }

  @Subcommand("set")
  @CommandPermission("core.economy.admin")
  @Syntax("<player> <amount>")
  public void set(CommandSender sender, OnlinePlayer player, @Default("0") int amount) {
    final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
    profile.setLevel(amount);
    sender.sendMessage(Style.translate("&fYou have set &2" + player.getPlayer().getName() + "'s &flevel to &2" + amount + "&f."));
  }

  @Subcommand("setxp")
  @CommandPermission("core.economy.admin")
  @Syntax("<player> <amount>")
  public void setXP(Player sender, OnlinePlayer player, @Default("0") int amount) {
    final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
    profile.setXp(amount);
    sender.sendMessage(Style.translate("&fYou have set &2" + player.getPlayer().getName() + "'s &fXP to &2" + amount + "&f."));
  }


}
