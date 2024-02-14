package dev.revere.amethyst.features.prestige;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("prestige")
@RequiredArgsConstructor
public class PrestigeCommand extends BaseCommand {

    private final Main core = Main.getInstance();

    @Default
    public void prestige(Player sender) {
        Profile profile = core.getProfileHandler().getProfile(sender.getUniqueId());
        if (profile.getLevel() < 120) {
            sender.sendMessage(Style.translate("&cYou must be &2Level 120 &cto prestige. You are currently &2Level " + profile.getLevel() + "&c."));
            return;
        }

        sender.sendTitle(
                Style.translate(core.getSettingsYML().getString("TITLES.PRESTIGED.TITLE").replaceAll("<prestige>", String.valueOf(profile.getPrestige()))),
                Style.translate(core.getSettingsYML().getString("TITLES.PRESTIGED.SUBTITLE").replaceAll("<prestige>", String.valueOf(profile.getPrestige()))),
                1, 60, 1
        );

        profile.setLevel(0);
        profile.setPrestige(profile.getPrestige() + 1);

        profile.setBalance(0);
        profile.setOrbs(0);
    }

    @Subcommand("set")
    @CommandPermission("core.economy.admin")
    @Syntax("<player> <amount>")
    public void set(CommandSender sender, OnlinePlayer player, @Default("0") int amount) {
        final Profile profile = core.getProfileHandler().getProfile(player.getPlayer().getUniqueId());
        profile.setPrestige(amount);
        sender.sendMessage(Style.translate("&fYou have set &a" + player.getPlayer().getName() + "'s &fprestige to &a" + amount + "&f."));
    }
}