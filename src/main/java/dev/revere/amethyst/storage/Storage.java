package dev.revere.amethyst.storage;

import dev.revere.amethyst.features.generators.Generator;
import dev.revere.amethyst.features.islands.Island;
import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.enchants.Enchant;
import dev.revere.amethyst.profile.Profile;
import dev.revere.amethyst.utils.location.Grid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Storage {
    Optional<Profile> loadProfile(UUID uuid);
    Optional<Generator> loadGenerator(UUID uuid);
    Optional<Tool> loadTool(UUID uuid);
    Optional<Island> loadIsland(UUID uuid);
    Optional<Grid> loadGrid();

    List<Enchant> loadEnchants(UUID uuid);

    void saveProfile(Profile profile);
    void saveGenerator(Generator generator);
    void saveTool(Tool tool);
    void saveIsland(Island island);
    void saveGrid(Grid grid);
    void saveEnchant(Tool tool, Enchant enchant);

    void removeProfile(Profile profile);
    void removeGenerator(Generator generator);
    void removeTool(Tool tool);
    void removeIsland(Island island);
    void removeEnchant(Tool tool, Enchant enchant);
    void removeEnchants(Tool tool);
}
