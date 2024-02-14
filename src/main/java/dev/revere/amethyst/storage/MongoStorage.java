package dev.revere.amethyst.storage;

import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.Generator;
import dev.revere.amethyst.features.islands.Island;
import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.enchants.Enchant;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.storage.impl.mongo.*;
import dev.revere.amethyst.utils.location.Grid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MongoStorage implements Storage {

    private final MongoProfileStorage profiles;
    private final MongoGeneratorStorage generators;
    private final MongoToolStorage tools;
    private final MongoIslandStorage islands;
    private final MongoGridStorage grid;
    private final MongoEnchantStorage enchants;

    public MongoStorage(Main core) {
        MongoHandler handler = new MongoHandler(core, core.getConfigYML());

        this.profiles = new MongoProfileStorage(core, handler);
        this.generators = new MongoGeneratorStorage(core, handler);
        this.islands = new MongoIslandStorage(handler);
        this.grid = new MongoGridStorage(handler);
        this.enchants = new MongoEnchantStorage(handler);
        this.tools = new MongoToolStorage(handler, this.enchants);
    }

    public Optional<Profile> loadProfile(UUID uuid) {
        return profiles.load(uuid);
    }

    public Optional<Generator> loadGenerator(UUID uuid) {
        return generators.load(uuid);
    }

    public Optional<Tool> loadTool(UUID uuid) {
        return tools.load(uuid);
    }

    public Optional<Island> loadIsland(UUID uuid) {
        return islands.load(uuid);
    }

    public Optional<Grid> loadGrid() {
        return this.grid.load();
    }

    public List<Enchant> loadEnchants(UUID uuid) {
        return this.enchants.load(uuid);
    }

    public void saveProfile(Profile profile) {
        this.profiles.save(profile);
    }

    public void saveGenerator(Generator generator) {
        this.generators.save(generator);
    }

    public void saveTool(Tool tool) {
        this.tools.save(tool);
    }

    public void saveIsland(Island island) {
        this.islands.save(island);
    }

    public void saveGrid(Grid grid) {
        this.grid.save(grid);
    }

    public void saveEnchant(Tool tool, Enchant enchant) {
        this.enchants.save(tool, enchant);
    }

    public void removeProfile(Profile profile) {
    }

    public void removeGenerator(Generator generator) {
        this.generators.remove(generator);
    }

    public void removeTool(Tool tool) {
    }

    public void removeIsland(Island island) {
    }

    public void removeEnchant(Tool tool, Enchant enchant) {
        this.enchants.remove(tool, enchant);
    }

    public void removeEnchants(Tool tool) {
        this.enchants.remove(tool);
    }
}
