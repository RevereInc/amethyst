package cc.skymc.amethyst.features.dungeons.commands;

import cc.skymc.amethyst.Locale;
import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.dungeons.DungeonHandler;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@CommandAlias("jungle")
@RequiredArgsConstructor
public class DungeonCommand extends BaseCommand {

    public DungeonHandler dungeonHandler = Main.getInstance().getDungeonHandler();

    @CommandAlias("enter")
    public void enter(Player sender) {
        if(!dungeonHandler.checkPass(sender)) {
            sender.sendMessage(Locale.DUNGEON_NO_PASS.getColoredString());
            return;
        }

        sender.getInventory().removeItem(DungeonHandler.DUNGEON_KEY);
        dungeonHandler.startDungeon(sender);
    }

    @CommandAlias("givepass")
    @Syntax("<player> <amount>")
    public void givePass(Player sender, OnlinePlayer player, @Default("1") int amount) {
        //do the rest later icl
        player.getPlayer().getInventory().addItem(DungeonHandler.DUNGEON_KEY);
    }

    @CommandAlias("checkremaining")
    @Syntax("<player>")
    public void givePass(Player sender, OnlinePlayer player) {
        Main.getInstance().getDungeonHandler().getRemaining(player.getPlayer().getUniqueId());
    }

}
