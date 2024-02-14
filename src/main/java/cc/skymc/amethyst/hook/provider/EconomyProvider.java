package cc.skymc.amethyst.hook.provider;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.profile.ProfileHandler;
import org.bukkit.entity.Player;

public class EconomyProvider extends net.brcdev.shopgui.provider.economy.EconomyProvider {

    private final ProfileHandler profileHandler;

    public EconomyProvider(Main core) {
        this.profileHandler = core.getProfileHandler();

        this.currencyPrefix = "$";
        this.currencySuffix = "";
    }

    @Override
    public String getName() {
        return "Amethyst";
    }

    @Override
    public double getBalance(Player player) {
        return profileHandler.getProfile(player.getUniqueId()).getBalance();
    }

    @Override
    public void deposit(Player player, double v) {
        Profile profile = profileHandler.getProfile(player.getUniqueId());
        profile.setBalance(profile.getBalance() + Math.round(v));
    }

    @Override
    public void withdraw(Player player, double v) {
        Profile profile = profileHandler.getProfile(player.getUniqueId());
        if (profile.getBalance() < v)
            return;

        profile.setBalance(profile.getBalance() - Math.round(v));
    }
}
