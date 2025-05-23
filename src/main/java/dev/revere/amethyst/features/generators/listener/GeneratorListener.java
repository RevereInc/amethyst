package dev.revere.amethyst.features.generators.listener;

import dev.revere.amethyst.Locale;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.Generator;
import dev.revere.amethyst.features.generators.GeneratorHandler;
import dev.revere.amethyst.features.generators.GeneratorType;
import dev.revere.amethyst.features.generators.menus.UpgradeMenu;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.profile.ProfileHandler;
import dev.revere.amethyst.utils.chat.Style;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneratorListener implements Listener {

    private final GeneratorHandler generatorHandler;
    private final ProfileHandler profileHandler;

    public GeneratorListener(GeneratorHandler generatorHandler, ProfileHandler profileHandler) {
        this.generatorHandler = generatorHandler;
        this.profileHandler = profileHandler;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Optional<GeneratorType> generatorType = GeneratorType.get(event.getItemInHand());
        Profile profile = profileHandler.getProfile(event.getPlayer().getUniqueId());

        if (generatorType.isEmpty())
            return;

        /*
        if(profile.getSlots() == 25) {
          event.setCancelled(true);
          event.getPlayer().sendMessage(Style.translate("&4&l[!] &cYou have reached the &4generator limit &cof 25."));
          return;
        }
         */

        Generator generator = generatorHandler.createGenerator(UUID.randomUUID(), event.getPlayer().getUniqueId(), generatorType.get(), event.getBlockPlaced().getLocation(), 1);
        profile.setSlots(profile.getSlots() + 1);
        profile.getGenerators().add(generator);

        event.getPlayer().sendMessage(Locale.GENERATOR_PLACED.getColoredString().replace("<generator>", Style.translate(generatorType.get().display)));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        List<Location> generatorLocations = generatorHandler.getGeneratorLocations();

        if(generatorLocations.contains(event.getBlock().getLocation())) {
            player.sendMessage(Style.translate("&4&l[!] &cYou must open the menu to remove generators. " + "&7(Shift + Right Click)"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(event.getPlayer().getUniqueId());

        if (profile.getGenerators().isEmpty())
            return;

        for (Generator gen : profile.getGenerators()) {
            GeneratorScheduler.createTask(gen);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Profile profile = Main.getInstance().getProfileHandler().getProfile(event.getPlayer().getUniqueId());

        if (profile.getGenerators().isEmpty())
            return;

        for (Generator gen : profile.getGenerators()) {
            generatorHandler.remove(gen, false);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!player.isSneaking())
            return;

        Profile profile = profileHandler.getProfile(player.getUniqueId());
        for (Generator gen : profile.getGenerators()) {
            if (gen.location.equals(event.getClickedBlock().getLocation())) {
                new UpgradeMenu(player, Main.getInstance(), gen).updateMenu();
                event.setCancelled(true);
            }
        }
    }
}
