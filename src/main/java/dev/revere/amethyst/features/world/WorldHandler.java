package dev.revere.amethyst.features.world;

import dev.revere.amethyst.Main;
import it.unimi.dsi.fastutil.chars.Char2ReferenceArrayMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.HashMap;
import java.util.Map;

public class WorldHandler {

    private final Main core;
    private final Map<String, World> worlds = new HashMap<>();


    public WorldHandler(Main core) {
        this.core = core;
        loadWorlds();
    }

    public void loadWorlds() {
        for (String world : core.getWorldYML().getStringList("WORLD.WORLDS")) {
            createWorld(world);
        }
    }

    public void createWorld(String name) {
        if (Bukkit.getWorld(name) != null) {
            return;
        }

        final WorldCreator creator = new WorldCreator(name);
        creator.type(WorldType.FLAT);
        creator.generatorSettings("3;minecraft;air;");
        creator.generateStructures(false);

        World world = core.getServer().createWorld(creator);
        worlds.put(name, world);
    }

    public final World getWorld(String name) {
        return worlds.get(name);
    }
}
