package cc.skymc.amethyst.essential.commands.economy;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.utils.chat.Style;
import cc.skymc.amethyst.utils.formatter.MathUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.List;

@CommandAlias("bal|balance")
@RequiredArgsConstructor
public class BalanceCommand extends BaseCommand {

    private final Main core = Main.getInstance();
    private final List<String> helpMessage = Style.translate(Arrays.asList(
            Style.CHAT_BAR,
            "&6&lAvailable Balance Commands",
            Style.CHAT_BAR,
            " &6• &e/balance &7- Showcases this help message",
            " &6• &e/balance coins <player> &7- Check the coins of a player",
            " &6• &e/balance orbs <player> &7- Check the orbs of a player",
            " &6• &e/balance crystals <player> &7- Check crystals coins of a player",
            Style.CHAT_BAR
    ));

    @Default
    public void execute(Player player) {
        helpMessage.forEach(line -> player.sendMessage(Style.translate(line)));
    }

    @Subcommand("coins")
    @Syntax("<player>")
    public void coins(CommandSender sender, OnlinePlayer player) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        sender.sendMessage(Style.translate(player.getPlayer().getName() + "has a balance of" + MathUtils.formatNumber(profile.getBalance()) + "&f &7coins&f."));
    }

    @Subcommand("orbs")
    @Syntax("<player>")
    public void orbs(CommandSender sender, OnlinePlayer player) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        sender.sendMessage(Style.translate(player.getPlayer().getName() + "has a balance of" + MathUtils.formatNumber(profile.getOrbs()) + "&f &corbs&f."));
    }

    @Subcommand("crystals")
    @Syntax("<player>")
    public void crystals(CommandSender sender, OnlinePlayer player) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        sender.sendMessage(Style.translate(player.getPlayer().getName() + "has a balance of" + MathUtils.formatNumber(profile.getCrystals()) + "&f &acrystals&f."));
    }
}
