package cc.skymc.amethyst.storage;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.islands.Island;
import cc.skymc.amethyst.features.tool.Tool;
import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.storage.impl.mongo.*;
import cc.skymc.amethyst.utils.location.Grid;

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

  public Optional<Profile> loadProfile(UUID uuid) { return profiles.load(uuid); }
  public Optional<Generator> loadGenerator(UUID uuid) { return generators.load(uuid); }
  public Optional<Tool> loadTool(UUID uuid) { return tools.load(uuid); }
  public Optional<Island> loadIsland(UUID uuid) { return islands.load(uuid); }
  public Optional<Grid> loadGrid() {
    return this.grid.load();
  }

  public List<Enchant> loadEnchants(UUID uuid) { return this.enchants.load(uuid); }

  public void saveProfile(Profile profile) { this.profiles.save(profile); }
  public void saveGenerator(Generator generator) { this.generators.save(generator); }
  public void saveTool(Tool tool) { this.tools.save(tool); }
  public void saveIsland(Island island) { this.islands.save(island); }
  public void saveGrid(Grid grid) {
    this.grid.save(grid);
  }
  public void saveEnchant(Tool tool, Enchant enchant) { this.enchants.save(tool, enchant); }

  public void removeProfile(Profile profile) {}
  public void removeGenerator(Generator generator) { this.generators.remove(generator); }
  public void removeTool(Tool tool) {}
  public void removeIsland(Island island) {}
  public void removeEnchant(Tool tool, Enchant enchant) { this.enchants.remove(tool, enchant); }
  public void removeEnchants(Tool tool) { this.enchants.remove(tool); }
}
