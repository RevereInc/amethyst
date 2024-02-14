package cc.skymc.amethyst;

import cc.skymc.amethyst.essential.commands.AutoSellCommand;
import cc.skymc.amethyst.essential.commands.CraftCommand;
import cc.skymc.amethyst.essential.commands.FlyCommand;
import cc.skymc.amethyst.essential.commands.SpawnCommand;
import cc.skymc.amethyst.essential.commands.WarpCommand;
import cc.skymc.amethyst.essential.commands.economy.*;
import cc.skymc.amethyst.features.actionbar.ActionBarHandler;
import cc.skymc.amethyst.features.dungeons.DungeonHandler;
import cc.skymc.amethyst.features.dungeons.commands.DungeonCommand;
import cc.skymc.amethyst.features.generators.commands.GeneratorCommand;
import cc.skymc.amethyst.features.islands.commands.IslandCommand;
import cc.skymc.amethyst.essential.listeners.PlayerListener;
import cc.skymc.amethyst.features.generators.GeneratorHandler;
import cc.skymc.amethyst.features.levels.LevelHandler;
import cc.skymc.amethyst.features.levels.commands.LevelCommand;
import cc.skymc.amethyst.features.pets.Pet;
import cc.skymc.amethyst.features.pets.commands.PetCommand;
import cc.skymc.amethyst.features.pets.PetHandler;
import cc.skymc.amethyst.features.prestige.PrestigeCommand;
import cc.skymc.amethyst.features.ui.scoreboard.ScoreboardHandler;
import cc.skymc.amethyst.features.tool.enchants.EnchantListener;
import cc.skymc.amethyst.features.tool.listener.ToolListener;
import cc.skymc.amethyst.features.world.WorldHandler;
import cc.skymc.amethyst.hook.ShopGUIPlusHook;
import cc.skymc.amethyst.hook.provider.EconomyProvider;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.storage.MongoStorage;
import cc.skymc.amethyst.storage.Storage;
import cc.skymc.amethyst.features.islands.IslandHandler;
import cc.skymc.amethyst.features.tool.ToolHandler;
import cc.skymc.amethyst.hook.PlaceholderAPIHook;
import cc.skymc.amethyst.profile.ProfileHandler;
import cc.skymc.amethyst.utils.Safelock;
import cc.skymc.amethyst.utils.config.BasicConfigurationFile;
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
