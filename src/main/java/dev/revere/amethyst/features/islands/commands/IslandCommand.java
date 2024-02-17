package dev.revere.amethyst.features.islands.commands;

import dev.revere.amethyst.Locale;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.Generator;
import dev.revere.amethyst.features.generators.listener.GeneratorScheduler;
import dev.revere.amethyst.features.islands.menus.IslandTopMenu;
import dev.revere.amethyst.features.islands.Island;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.Schematic;
import dev.revere.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@CommandAlias("cloud|clouds|cl")
@RequiredArgsConstructor
public class IslandCommand extends BaseCommand {
    private final Main core = Main.getInstance();
    HashMap<UUID, List<Island>> invitedMap = new HashMap<>();

    @Default
    public void cloud(Player sender) {
        Profile profile = core.getProfileHandler().getProfile(sender.getUniqueId());
        Optional<Island> island = profile.getIsland();

        if (island.isEmpty()) {
            sender.sendMessage(Locale.ISLAND_NO_ISLAND.getColoredString());
            return;
        }

        move(sender, island.get());
        sender.sendMessage(Locale.ISLAND_TELEPORT.getColoredString());
    }

    @Subcommand("create")
    public void create(Player sender) {
        Profile profile = core.getProfileHandler().getProfile(sender.getUniqueId());
        if (profile.getIsland().isPresent()) {
            sender.sendMessage(Locale.ISLAND_ALREADY_EXIST.getColoredString());
            return;
        }

        Optional<Island> island = Main.getInstance().getIslandHandler().createIsland(UUID.randomUUID(), sender.getUniqueId(), new ArrayList<>(), 0L);

        if (island.isEmpty())
            return;

        profile.setIsland(island.get().getUuid());


        File schematic = new File(core.getDataFolder().getAbsolutePath(), "/schematics/island.schem");
        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try {
            ClipboardReader reader = format.getReader(new FileInputStream(schematic));
            Clipboard clipboard = reader.read();

            new Schematic(clipboard).paste(island.get().getRegion().getCenter(Bukkit.getWorld("islands")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        move(sender, island.get());
        sender.sendMessage(Locale.ISLAND_CREATE.getColoredString());
    }


    @Subcommand("deposit")
    @Syntax("<crystals>")
    public void deposit(Player sender, @Default("0") Long amount) {
        Profile profile = core.getProfileHandler().getProfile(sender.getUniqueId());
        Optional<Island> island = profile.getIsland();

        if (island.isEmpty()) {
            sender.sendMessage(Locale.ISLAND_NO_ISLAND.getColoredString());
            return;
        }

        if (profile.getCrystals() < amount) {
            sender.sendMessage(Locale.CRYSTALS_NOT_ENOUGH.getColoredString());
            return;
        }

        island.get().setCrystals(island.get().getCrystals() + amount);
        profile.setCrystals(profile.getCrystals() - amount);
        sender.sendMessage(Locale.ISLAND_CRYSTALS_DEPOSITED.getColoredString().replaceAll("<amount>", String.valueOf(amount)));
    }

    @Subcommand("invite")
    @Syntax("<player>")
    public void invite(Player sender, OnlinePlayer player) {
        UUID invitedUUID = player.getPlayer().getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        Profile invitedProfile = core.getProfileHandler().getProfile(invitedUUID);
        Profile senderProfile = core.getProfileHandler().getProfile(senderUUID);

        //sender doesnt not have an island to invite to
        if (senderProfile.getIsland().isEmpty()) {
            sender.sendMessage(Locale.ISLAND_NO_ISLAND.getColoredString());
            return;
        }

        //player already has island
        if (invitedProfile.getIsland().isPresent()) {
            sender.sendMessage(Locale.ISLAND_OTHER_ALREADY_HAS.getColoredString());
            return;
        }

        Optional<Island> island = senderProfile.getIsland();
        List<Island> islands = invitedMap.getOrDefault(invitedUUID, new ArrayList<>());
        islands.add(island.get());

        invitedMap.put(player.getPlayer().getUniqueId(), islands);
        sender.sendMessage(Locale.ISLAND_INVITED.getColoredString().replaceAll("<player>", Bukkit.getPlayer(invitedUUID).getName()));
        player.getPlayer().sendMessage(Locale.ISLAND_OTHER_INVITED.getColoredString().replaceAll("<player>", Bukkit.getPlayer(senderUUID).getName()));
    }

    @Subcommand("top")
    public void top(Player sender) {
        new IslandTopMenu(sender, core.getConfigYML().getString(Style.translate("MENUS.ISLAND-TOP.NAME")), core).updateMenu();
    }

    @Subcommand("accept")
    @Syntax("<player>")
    public void accept(Player sender, OnlinePlayer player) {
        UUID senderUUID = player.getPlayer().getUniqueId();
        UUID invitedUUID = sender.getUniqueId();

        Profile invitedProfile = core.getProfileHandler().getProfile(invitedUUID);
        Profile senderProfile = core.getProfileHandler().getProfile(senderUUID);

        //if the sender's profile no longer had an island to join
        if (senderProfile.getIsland().isEmpty()) {
            sender.sendMessage(Locale.ISLAND_OTHER_NO_ISLAND.getColoredString());
            return;
        }

        //sender already has an island
        if (invitedProfile.getIsland().isPresent()) {
            sender.sendMessage(Locale.ISLAND_ALREADY_HAS.getColoredString());
            return;
        }

        List<Island> islands = invitedMap.getOrDefault(invitedUUID, new ArrayList<>());
        Optional<Island> island = senderProfile.getIsland();

        if (!islands.contains(island.get())) {
            sender.sendMessage(Locale.ISLAND_NOT_INVITED.getColoredString());
            return;
        }

        invitedProfile.setIsland(island.get().getUuid());
        island.get().getMembers().add(invitedUUID);

        invitedMap.remove(invitedUUID);

        for (UUID uuid : island.get().getMembers()) {
            Player member = Bukkit.getPlayer(uuid);

            if(member == null)
                return;

            member.sendMessage(Locale.ISLAND_OTHER_JOINED.getColoredString().replaceAll("<player>", Bukkit.getPlayer(invitedUUID).getName()));
        }

        sender.sendMessage(Locale.ISLAND_JOINED.getColoredString().replaceAll("<player>", Bukkit.getPlayer(senderUUID).getName()));
        move(sender, island.get());
    }

    private void move(Player player, Island island) {
        player.teleportAsync(island.getRegion().getCenter(Bukkit.getWorld("islands")));

        WorldBorder worldBorder = Bukkit.createWorldBorder();
        worldBorder.setWarningDistance(0);
        worldBorder.setCenter(island.getRegion().getCenter(Bukkit.getWorld("islands")));
        worldBorder.setSize(300);

        player.setWorldBorder(worldBorder);
    }

}
