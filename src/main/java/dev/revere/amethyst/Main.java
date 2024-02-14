package dev.revere.amethyst;

import dev.revere.amethyst.essential.commands.AutoSellCommand;
import dev.revere.amethyst.essential.commands.CraftCommand;
import dev.revere.amethyst.essential.commands.FlyCommand;
import dev.revere.amethyst.essential.commands.SpawnCommand;
import dev.revere.amethyst.essential.commands.WarpCommand;
import dev.revere.amethyst.essential.commands.economy.*;
import dev.revere.amethyst.features.actionbar.ActionBarHandler;
import dev.revere.amethyst.features.dungeons.DungeonHandler;
import dev.revere.amethyst.features.dungeons.commands.DungeonCommand;
import dev.revere.amethyst.features.generators.commands.GeneratorCommand;
import dev.revere.amethyst.features.islands.commands.IslandCommand;
import dev.revere.amethyst.essential.listeners.PlayerListener;
import dev.revere.amethyst.features.generators.GeneratorHandler;
import dev.revere.amethyst.features.levels.LevelHandler;
import dev.revere.amethyst.features.levels.commands.LevelCommand;
import dev.revere.amethyst.features.pets.Pet;
import dev.revere.amethyst.features.pets.commands.PetCommand;
import dev.revere.amethyst.features.pets.PetHandler;
import dev.revere.amethyst.features.prestige.PrestigeCommand;
import dev.revere.amethyst.features.ui.scoreboard.ScoreboardHandler;
import dev.revere.amethyst.features.tool.enchants.EnchantListener;
import dev.revere.amethyst.features.tool.listener.ToolListener;
import dev.revere.amethyst.features.world.WorldHandler;
import dev.revere.amethyst.hook.ShopGUIPlusHook;
import dev.revere.amethyst.hook.provider.EconomyProvider;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.storage.MongoStorage;
import dev.revere.amethyst.storage.Storage;
import dev.revere.amethyst.features.islands.IslandHandler;
import dev.revere.amethyst.features.tool.ToolHandler;
import dev.revere.amethyst.hook.PlaceholderAPIHook;
import dev.revere.amethyst.profile.ProfileHandler;
import dev.revere.amethyst.utils.Safelock;
import dev.revere.amethyst.utils.config.BasicConfigurationFile;
import co.aikar.commands.PaperCommandManager;
import io.github.nosequel.menu.MenuHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private Storage storage;

    private ProfileHandler profileHandler;
    private GeneratorHandler generatorHandler;
    private ToolHandler toolHandler;
    private IslandHandler islandHandler;
    private PetHandler petHandler;
    private ActionBarHandler actionBarHandler;
    private LevelHandler levelHandler;
    private DungeonHandler dungeonHandler;
    private WorldHandler worldHandler;

    private EconomyProvider economyProvider;
    private ScoreboardHandler scoreboardHandler;

    private BasicConfigurationFile messagesYML;
    private BasicConfigurationFile settingsYML;
    private BasicConfigurationFile configYML;
    private BasicConfigurationFile scoreboardYML;
    private BasicConfigurationFile worldYML;

    /**
     * Loading logic of the plugin
     */
    @Override
    public void onLoad() {
        instance = this;
        this.configYML = new BasicConfigurationFile(this, "config");

        if (!new Safelock(this, configYML.getString("LICENCE"), "https://license.revere.dev/api/client", "3016f575fa8f3c58b9224047695249f3ca4ca773").unlock()) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);
            return;
        }

        this.messagesYML = new BasicConfigurationFile(this, "messages");
        this.settingsYML = new BasicConfigurationFile(this, "settings");
        this.scoreboardYML = new BasicConfigurationFile(this, "scoreboard");
        this.worldYML = new BasicConfigurationFile(this, "worlds");
    }

    /**
     * Enabling logic of the plugin
     */
    @Override
    public void onEnable() {
        loadHandlers();

        if (Bukkit.getPluginManager().getPlugin("ShopGuiPlus") != null) {
            this.economyProvider = new EconomyProvider(this);
        }

        if (configYML.getBoolean("STORAGE.MONGO-STORAGE")) {
            this.storage = new MongoStorage(this);
        }

        islandHandler.loadGrid(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("Hooked into PlaceholderAPI.");
            new PlaceholderAPIHook(this).register();
        }

        hook();
        loadCommands();
        loadListeners();
    }

    private void loadHandlers() {
        this.profileHandler = new ProfileHandler(this);
        this.islandHandler = new IslandHandler(this);
        this.generatorHandler = new GeneratorHandler(this);
        this.toolHandler = new ToolHandler(this);
        this.petHandler = new PetHandler(this);
        this.actionBarHandler = new ActionBarHandler(this);
        this.levelHandler = new LevelHandler(this);
        this.dungeonHandler = new DungeonHandler(this);
        this.scoreboardHandler = new ScoreboardHandler();
        this.worldHandler = new WorldHandler(this);
        new MenuHandler(this);
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerListener(this),
                new EnchantListener(this),
                new ToolListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);

        if (Bukkit.getPluginManager().getPlugin("ShopGuiPlus") != null) {
            manager.registerCommand(new AutoSellCommand());
        }
        Arrays.asList(
                new CrystalsCommand(),
                new EconomyCommand(),
                new BalanceCommand(),
                new OrbsCommand(),
                new SpawnCommand(),
                new IslandCommand(),
                new GeneratorCommand(),
                new LevelCommand(),
                new PetCommand(),
                new WarpCommand(this),
                new DungeonCommand(),
                new PrestigeCommand(),
                new PayCommand(this),
                new FlyCommand(),
                new CraftCommand()
        ).forEach(manager::registerCommand);
    }

    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("ShopGUIPlus") == null) {
            Bukkit.getConsoleSender().sendMessage("ShopGuiPlus was not found.");
            return;
        }

        ShopGUIPlusHook shopGUIPlusHook = new ShopGUIPlusHook(this);
        Bukkit.getPluginManager().registerEvents(shopGUIPlusHook, this);

        Bukkit.getConsoleSender().sendMessage("ShopGuiPlus was found, hooking into.");
    }

    /**
     * Disabling logic of the plugin
     */

    @Override
    public void onDisable() {
        profileHandler.getProfiles().values().forEach(profile -> storage.saveProfile(profile));
        generatorHandler.getGenerators().values().forEach(generator -> storage.saveGenerator(generator));
        toolHandler.getTools().values().forEach(tool -> storage.saveTool(tool));
        islandHandler.getIslands().values().forEach(island -> storage.saveIsland(island));
        storage.saveGrid(islandHandler.getGrid());

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            Optional<Pet> pet = PetHandler.getPet(uuid);
            Profile profile = profileHandler.getProfile(uuid);

            if (pet.isEmpty())
                continue;

            PetHandler.getActiveStands().get(uuid).remove();

            switch (pet.get()) {
                case RABBIT -> player.setWalkSpeed(0.2F);
                case GOAT -> profile.setXpMultiplier(profile.getXpMultiplier() - 0.25);
            }
        }

    }
}
