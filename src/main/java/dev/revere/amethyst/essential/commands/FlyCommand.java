package dev.revere.amethyst.essential.commands;

import dev.revere.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("fly")
public class FlyCommand extends BaseCommand {

    @Default
    public void fly(Player sender) {
        if (sender.getAllowFlight()) {
            sender.sendMessage(Style.translate("&4&l[!] &cYou have disabled &4fly"));
            sender.setFlying(false);
            sender.setAllowFlight(false);
        } else {
            sender.sendMessage(Style.translate("&2&l[!] &aYou have enabled &2fly&a."));
            sender.setAllowFlight(true);
            sender.setFlying(true);
        }
    }
}
