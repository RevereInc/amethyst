package cc.skymc.amethyst.storage;

import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.islands.Island;
import cc.skymc.amethyst.features.tool.Tool;
import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.utils.location.Grid;

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
