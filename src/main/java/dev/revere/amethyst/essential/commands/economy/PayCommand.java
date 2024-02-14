package dev.revere.amethyst.essential.commands.economy;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.profile.ProfileHandler;
import dev.revere.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("pay")
public class PayCommand extends BaseCommand {

    private final ProfileHandler profileHandler;

    public PayCommand(Main core) {
        this.profileHandler = core.getProfileHandler();
    }

    @Default
    @CommandPermission("core.economy.admin")
    public void execute(Player sender, Player receiver, long amount) {
        Profile profile = profileHandler.getProfile(receiver.getUniqueId());
        Profile profile1 = profileHandler.getProfile(sender.getUniqueId());

        if (amount > profile1.getBalance()) {
            sender.sendMessage(Style.translate("&cYou do not have enough money."));
            return;
        }

        profile.setBalance(profile1.getBalance() - amount);
        profile.setBalance(profile.getBalance() + amount);
    }

}
