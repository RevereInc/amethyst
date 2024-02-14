package dev.revere.amethyst.features.ui.scoreboard;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.utils.chat.Style;
import dev.revere.amethyst.utils.config.BasicConfigurationFile;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardHandler implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private final BasicConfigurationFile config = Main.getInstance().getScoreboardYML();

    public ScoreboardHandler() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());

        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0, 20L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FastBoard board = new FastBoard(player);

        board.updateTitle(Style.translate(config.getString("SCOREBOARD.TITLE")));
        this.boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        FastBoard board = this.boards.remove(event.getPlayer().getUniqueId());

        if (board == null)
            return;

        board.delete();
    }

    private void updateBoard(FastBoard board) {
        board.updateLines(Style.translate(config.getStringList("SCOREBOARD.LINES")));
    }

}
