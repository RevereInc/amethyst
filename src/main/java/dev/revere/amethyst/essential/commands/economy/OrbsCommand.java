package dev.revere.amethyst.essential.commands.economy;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.chat.Style;
import dev.revere.amethyst.utils.formatter.MathUtils;
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

@CommandAlias("orbs")
@RequiredArgsConstructor
public class OrbsCommand extends BaseCommand {

    private final Main core = Main.getInstance();
    private final List<String> helpMessage = Style.translate(Arrays.asList(
            Style.CHAT_BAR,
            "&c&lAvailable Orbs Commands",
            Style.CHAT_BAR,
            " &4• &c/orbs &7- Showcases this help message",
            " &4• &c/orbs set <player> amount &7- Sets the orbs of a player",
            " &4• &c/orbs add <player> <amount> &7- Gives the player orbs",
            " &4• &c/orbs remove <player> <amount> &7- Takes from the player orbs",
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
        profile.setOrbs(amount);
        sender.sendMessage(Style.translate("&fYou set &c" + player.getPlayer().getName() + "'s &forbs to &c" + MathUtils.formatNumber(amount) + "&f."));
    }

    @Subcommand("add")
    @CommandPermission("core.economy.admin")
    @Syntax("<player> <amount>")
    public void add(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        profile.setOrbs(profile.getOrbs() + amount);
        sender.sendMessage(Style.translate("&fYou gave &c" + player.getPlayer().getName() + " " + MathUtils.formatNumber(amount) + "&f of orbs."));
    }

    @Subcommand("remove")
    @CommandPermission("core.economy.admin")
    @Syntax("<player> <amount>")
    public void remove(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        profile.setOrbs(profile.getOrbs() - amount);
        sender.sendMessage(Style.translate("&fYou took &c" + MathUtils.formatNumber(amount) + " orbs &ffrom &c" + player.getPlayer().getName() + "&f."));
    }
}
