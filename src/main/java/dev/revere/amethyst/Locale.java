package dev.revere.amethyst;

import dev.revere.amethyst.utils.chat.Style;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public enum Locale {

    GENERATOR_PLACED("GENERATORS.GENERATOR-PLACED"),
    GENERATOR_BROKEN("GENERATORS.GENERATOR-BROKEN"),
    NOT_OWNED("GENERATORS.NOT-OWNED"),
    INVENTORY_FULL("GENERATORS.INVENTORY-FULL"),
    FIRST_JOIN("SERVER.FIRST-JOIN"),
    ISLAND_CREATE("ISLAND.CREATE"),
    ISLAND_TELEPORT("ISLAND.TELEPORT"),
    ISLAND_ALREADY_EXIST("ISLAND.ALREADY-EXISTS"),
    ISLAND_NO_ISLAND("ISLAND.NO-ISLAND"),
    ISLAND_OTHER_NO_ISLAND("ISLAND.OTHER-NO-ISLAND"),
    ISLAND_ALREADY_HAS("ISLAND.ALREADY-HAS"),
    ISLAND_OTHER_ALREADY_HAS("ISLAND.ALREADY-HAS"),
    ISLAND_INVITED("ISLAND.INVITED"),
    ISLAND_OTHER_INVITED("ISLAND.OTHER-INVITED"),
    ISLAND_JOINED("ISLAND.JOINED"),
    ISLAND_OTHER_JOINED("ISLAND.OTHER-JOINED"),
    ISLAND_NOT_INVITED("ISLAND.NOT-INIVTED"),
    ISLAND_CRYSTALS_DEPOSITED("ISLAND.CRYSTALS-DEPOSITED"), //you have deposited x cyrstals into your island
    CRYSTALS_NOT_ENOUGH("CRYSTALS.NOT-ENOUGH"),
    SPAWN_TELEPORT("SERVER.SPAWN-TELEPORT"),
    WARZONE_TELEPORT("SERVER.WARZONE-TELEPORT"),
    FARM_TELEPORT("SERVER.FARM-TELEPORT"),
    DUNGEON_TELEPORT("SERVER.DUNGEON-TELEPORT"),
    DUNGEON_NO_PASS("SERVER.DUNGEON-NO-PASS"),
    DUNGEON_EXPIRED("SERVER.DUNGEON-EXPIRED"),
    PET_ENABLE("PETS.ENABLE"),
    PET_DISABLE("PETS.DISABLE"),
    PET_NOT_ACTIVE("PETS.NOT-ACTIVE"),
    MINING_RESTRICTED("TOOL.MINING-RESTRICTED");

    private final FileConfiguration fileConfiguration;
    private final String path;

    /**
     * Locale used to get messages
     *
     * @param fileConfiguration configuration file to get from
     * @param path path to get from
     */

    Locale(FileConfiguration fileConfiguration, String path) {
        this.fileConfiguration = fileConfiguration;
        this.path = path;
    }

    /**
     * Locale used to get messages
     *
     * @param path path to get from
     */

    Locale(String path) {
        this.fileConfiguration = Main.getInstance().getMessagesYML().getConfig();
        this.path = path;
    }

    /**
     * Gets the string of that path
     *
     * @return {@link String}
     */

    public final String getString() {
        return fileConfiguration.getString(path);
    }

    public final String getColoredString() {
        return Style.translate(fileConfiguration.getString(path));
    }

    /**
     * Gets the integer of that path
     *
     * @return {@link Integer}
     */

    public final int getInteger() {
        return fileConfiguration.getInt(path);
    }

    /**
     * Gets the boolean of that path
     *
     * @return {@link Boolean}
     */

    public final boolean getBoolean() {
        return fileConfiguration.getBoolean(path);
    }

    /**
     * Sends a message to a sender
     *
     * @param sender sender to send the message to
     */

    public final void sendMessage(CommandSender sender) {
        sender.sendMessage(getString());
    }

    /**
     * Sends a message to a player
     *
     * @param player player to send the message to
     */

    public final void sendMessage(Player player) {
        player.sendMessage(getString());
    }

}
