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

@CommandAlias("crystals")
@RequiredArgsConstructor
public class CrystalsCommand extends BaseCommand {

    private final Main core = Main.getInstance();
    private final List<String> helpMessage = Style.translate(Arrays.asList(
            Style.CHAT_BAR,
            "&2&lAvailable Crystals Commands",
            Style.CHAT_BAR,
            " &2• &a/crystals &7- Showcases this help message",
            " &2• &a/crystals set <player> <amount> &7- Sets the crystals of a player",
            " &2• &a/crystals add <player> <amount> &7- Gives the player crystals",
            " &2• &a/crystals remove <player> <amount> &7- Takes from the player crystals",
            Style.CHAT_BAR
    ));

    @Default
    @CommandPermission("core.economy.admin")
    public void crystals(CommandSender player) {
        helpMessage.forEach(line -> player.sendMessage(Style.translate(line)));
    }

    @Subcommand("set")
    @CommandPermission("core.economy.admin")
    @Syntax("<player> <amount>")
    public void set(CommandSender sender, OnlinePlayer player, @Default("0") Long amount) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        profile.setCrystals(amount);
        sender.sendMessage(Style.translate("&fYou set &2" + player.getPlayer().getName() + "'s &fcrystals to &2" + MathUtils.formatNumber(amount) + "&f."));
    }
}
