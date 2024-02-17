package dev.revere.amethyst.features.generators;

import com.mongodb.client.MongoCursor;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.listener.GeneratorListener;
import dev.revere.amethyst.features.generators.listener.GeneratorScheduler;

import java.util.*;

import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public class GeneratorHandler {

    private final Main core;
    private final Map<UUID, Generator> generators = new HashMap<>();
    private final List<Location> generatorLocations;

    public GeneratorHandler(Main core) {
        this.core = core;
        this.generatorLocations = loadGeneratorLocations();

        core.getServer().getPluginManager().registerEvents(
                new GeneratorListener(this, core.getProfileHandler()
                ),
                core
        );
    }

    public final Generator createGenerator(UUID uuid, UUID owner, GeneratorType type, Location location, int amount) {
        final Generator generator = new Generator(uuid, owner, type, location, amount);

        core.getStorage().saveGenerator(generator);
        generators.put(uuid, generator);
        generatorLocations.add(location);

        //add hologram

        GeneratorScheduler.createTask(generator);

        return generator;
    }

    public final void remove(Generator generator, boolean remove) {
        if (remove)
            core.getStorage().removeGenerator(generator);

        GeneratorScheduler.removeTask(generator);
        generators.remove(generator.getUuid());

        //remove hologram
    }


    public final Optional<Generator> getGenerator(UUID uuid) {
        if (generators.containsKey(uuid))
            return Optional.of(generators.get(uuid));

        Optional<Generator> generator = core.getStorage().loadGenerator(uuid);
        generator.ifPresent(value -> generators.put(uuid, value));

        return generator;
    }

    public List<Location> loadGeneratorLocations() {
        List<Location> toReturn = new ArrayList<>();
        MongoCursor<Document> cursor = Main.getInstance().getMongoHandler().getGenerators().find().iterator();

        try {
            while(cursor.hasNext()) {
                Document document = cursor.next();

                Location location = new Location(
                        Bukkit.getWorld(document.getString("world")),
                        document.getDouble("x"),
                        document.getDouble("y"),
                        document.getDouble("z"));

                toReturn.add(location);
            }
        } finally {
            cursor.close();
        }

        return toReturn;
    }

}
