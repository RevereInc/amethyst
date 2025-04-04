package dev.revere.amethyst.utils.chat;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Style {

    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String YELLOW = ChatColor.YELLOW.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String AQUA = ChatColor.AQUA.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String WHITE = ChatColor.WHITE.toString();
    public static final String BLACK = ChatColor.BLACK.toString();
    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();
    public static final String UNDER_LINE = ChatColor.UNDERLINE.toString();
    public static final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = ChatColor.RESET.toString();
    public static final String MAGIC = ChatColor.MAGIC.toString();
    public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static final String DARK_RED = ChatColor.DARK_RED.toString();
    public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static final String PINK = ChatColor.LIGHT_PURPLE.toString();
    public static final String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static final String BLANK_LINE = "§a §b §c §d §e §f §0 §r";
    public static final String MENU_BAR = translate("&7&m------------------------");
    public static final String DARK_BAR = translate("&8&m------------------------");
    public static final String CHAT_BAR = ChatColor.translateAlternateColorCodes('&', "&7&m--------" + StringUtils.repeat("-", 37) + "&7&m--------");
    public static final String SB_BAR = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------";

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    public static String untranslate(String in) {
        return in.replace("§", "&");
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return toReturn;
    }

    public static String progressBar(int currentBar, long maxBar, int totalBar, String symbol, ChatColor finishedColor, ChatColor unfinishedColor) {
        float percent = (float) currentBar / maxBar;
        int progress = (int)  (totalBar * percent);

        return Strings.repeat("" + finishedColor + symbol, progress) + Strings.repeat("" + unfinishedColor + symbol, totalBar - progress);
    }
}
