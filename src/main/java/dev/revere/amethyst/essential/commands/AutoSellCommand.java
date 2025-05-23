package dev.revere.amethyst.essential.commands;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("autosell")
public class AutoSellCommand extends BaseCommand {

    @Default
    @CommandPermission("amethyst.autosell")
    public void autoSell(Player sender) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(sender.getUniqueId());

        if (profile.getAutoSell()) {
            sender.sendMessage(Style.translate("&4&l[!] &cYou have disabled &4autosell&c."));
            profile.setAutoSell(false);
        } else {
            sender.sendMessage(Style.translate("&2&l[!] &aYou have enabled &2autosell&a."));
            profile.setAutoSell(true);
        }
    }
}
