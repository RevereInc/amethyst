package cc.skymc.amethyst.hook;

import cc.skymc.amethyst.Main;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopGUIPlusHook implements Listener {

    private Main core;

    public ShopGUIPlusHook(Main core) {
        this.core = core;
    }

    @EventHandler
    public void pluginEnable(ShopGUIPlusPostEnableEvent event) {
        ShopGuiPlusApi.registerEconomyProvider(core.getEconomyProvider());
        Bukkit.getConsoleSender().sendMessage("Registered economy provider.");
    }
}
