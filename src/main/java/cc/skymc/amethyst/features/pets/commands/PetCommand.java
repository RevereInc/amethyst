package cc.skymc.amethyst.features.pets.commands;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.pets.Pet;
import cc.skymc.amethyst.utils.chat.Style;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("pet|pets")
@RequiredArgsConstructor
public class PetCommand extends BaseCommand {

    private final Main core = Main.getInstance();
    private final List<String> helpMessage = Style.translate(Arrays.asList(
            Style.CHAT_BAR,
            "&9&lAvailable Pet Commands",
            Style.CHAT_BAR,
            " &1• &9/pet &7- Showcases this help message",
            " &1• &9/pet menu&7- Opens the pets menu for the player.",
            " &1• &9/pet give <player> <type> <rarity> &7- Gives a pet of a certain type to a player",
            Style.CHAT_BAR
    ));

    @Default
    @CommandPermission("core.pets.admin")
    public void gens(Player player) {
        helpMessage.forEach(line -> player.sendMessage(Style.translate(line)));
    }

    @Subcommand("give")
    @CommandPermission("core.pets.admin.give")
    @Syntax("<player> <type>")
    public void give(CommandSender sender, OnlinePlayer player, String type) {
        player.getPlayer().getInventory().addItem(Pet.valueOf(type).itemStack());
        sender.sendMessage(Style.translate("&fYou have given &9" + player.getPlayer().getName() + "&f a &9" + type.toLowerCase() + " pet&f."));
    }
}
