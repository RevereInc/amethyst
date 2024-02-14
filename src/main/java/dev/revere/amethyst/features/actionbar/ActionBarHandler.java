package dev.revere.amethyst.features.actionbar;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.chat.Style;
import dev.revere.amethyst.utils.config.BasicConfigurationFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBarHandler {
    private final BasicConfigurationFile settings;

    public ActionBarHandler(Main core) {
        this.settings = core.getSettingsYML();
    }

    public void sendActionBar(Player player, double xp) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        long cost = Math.round(300 * Math.pow(1.02, profile.getLevel()) * (profile.getPrestige() + 1));

        if (profile.getXp() >= cost) {
            return;
        }

        String bar = Style.translate(settings.getString("ACTION-BAR.PREFIX")) +
                Style.progressBar(
                        (int) profile.getXp(), cost,
                        settings.getInteger("ACTION-BAR.REPEAT"),
                        settings.getString("ACTION-BAR.SYMBOL"),
                        ChatColor.GREEN, ChatColor.RED
                )
                + Style.translate(settings.getString("ACTION-BAR.SUFFIX"));

        bar = bar.replaceAll("<xp>", String.valueOf(xp));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(bar));
    }
}
