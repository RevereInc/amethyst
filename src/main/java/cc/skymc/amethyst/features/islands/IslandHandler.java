package cc.skymc.amethyst.features.islands;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.utils.location.Grid;
import cc.skymc.amethyst.utils.location.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.Getter;

@Getter
public class IslandHandler {

    private final Main core;
    private Grid grid;
    private final Map<UUID, Island> islands;

    public IslandHandler(Main core) {
        this.core = core;
        this.islands = new HashMap<>();
    }

    public void loadGrid(Main core) {
        this.grid = core.getStorage().loadGrid().orElse(new Grid(300, 300));
    }

    public final Optional<Island> createIsland(UUID uuid, UUID leader, List<UUID> members, Long crystals) {
        final Optional<Region> region = grid.nextRegion();
        if (region.isEmpty())
            return Optional.empty();

        members.add(leader);

        final Island island = new Island(uuid, leader, members, region.get(), crystals);
        core.getStorage().saveIsland(island);
        islands.put(uuid, island);

        return Optional.of(island);
    }

    public final void remove(Island island) {
        core.getStorage().removeIsland(island);
        islands.remove(island.getUuid());
    }

    public final Optional<Island> getIsland(UUID uuid) {
        if (islands.containsKey(uuid))
            return Optional.of(islands.get(uuid));

        Optional<Island> island = core.getStorage().loadIsland(uuid);
        if (island.isPresent())
            islands.put(uuid, island.get());

        return island;
    }
}
