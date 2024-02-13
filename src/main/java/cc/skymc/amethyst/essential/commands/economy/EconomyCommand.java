package cc.skymc.amethyst.essential.commands.economy;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.utils.chat.Style;
import cc.skymc.amethyst.utils.formatter.MathUtils;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eco|economy")
@RequiredArgsConstructor
public class EconomyCommand extends BaseCommand {

  private final Main core = Main.getInstance();
  private final List<String> helpMessage = Style.translate(Arrays.asList(
      Style.CHAT_BAR,
      "&6&lAvailable Economy Commands",
      Style.CHAT_BAR,
      " &6• &e/economy &7- Showcases this help message",
      " &6• &e/economy set <player> <amount> &7- Sets the coins of a player",
      " &6• &e/economy add <player> <amount> &7- Gives the player coins",
      " &6• &e/economy remove <player> <amount> &7- Takes from the player coins",
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
  public void set(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
    final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
    profile.setBalance(amount);
    sender.sendMessage(Style.translate("&fYou set &3" + player.getPlayer().getName() + "'s &fbalance to &3" + MathUtils.formatNumber(amount) + "&f coins."));
  }

  @Subcommand("add")
  @CommandPermission("core.economy.admin")
  @Syntax("<player> <amount>")
  public void add(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
    final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
    profile.setBalance(profile.getBalance() + amount);
    sender.sendMessage(Style.translate("&fYou gave &3" + player.getPlayer().getName() + " " + MathUtils.formatNumber(amount) + "&f coins."));
  }

  @Subcommand("remove")
  @CommandPermission("core.economy.admin")
  @Syntax("<player> <amount>")
  public void remove(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
    final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
    profile.setBalance(profile.getBalance() - amount);
    sender.sendMessage(Style.translate("&fYou took &3" + MathUtils.formatNumber(amount) + " coins &ffrom &3" + player.getPlayer().getName() + "&f."));
  }
}
