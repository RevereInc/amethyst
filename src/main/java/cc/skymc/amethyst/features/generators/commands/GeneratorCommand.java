package cc.skymc.amethyst.features.generators.commands;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.GeneratorType;
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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gen|generator|gens|generators")
@RequiredArgsConstructor
public class GeneratorCommand extends BaseCommand {
    private final List<String> helpMessage = Style.translate(Arrays.asList(
            Style.CHAT_BAR,
            "&d&lAvailable Generator Commands",
            Style.CHAT_BAR,
            " &5• &d/gen &7- Showcases this help message",
            " &5• &d/gen shop &7- Opens the generator shop for the player.",
            " &5• &d/gen give <player> <type> &7- Gives a generator of a certain type to a player",
            Style.CHAT_BAR
    ));

    @Default
    @CommandPermission("core.generators.admin")
    public void gens(Player player) {
        helpMessage.forEach(line -> player.sendMessage(Style.translate(line)));
    }

    @Subcommand("give")
    @CommandPermission("core.generators.admin.give")
    @Syntax("<player> <type>")
    public void give(CommandSender sender, OnlinePlayer player, String type) {
        player.getPlayer().getInventory().addItem(GeneratorType.valueOf(type).itemStack());
        sender.sendMessage(Style.translate("&fYou have given &d" + player.getPlayer().getName() + "&f a &d" + type.toLowerCase() + " generator&f."));
        Bukkit.getConsoleSender().sendMessage("&d" + sender + "&f has given &d" + player.getPlayer().getName() + "&f a &d" + type.toLowerCase() + " generator&f.");
    }
}
