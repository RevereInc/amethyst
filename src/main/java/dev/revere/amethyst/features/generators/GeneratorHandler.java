package dev.revere.amethyst.features.generators;

import com.mongodb.client.MongoCursor;
import dev.revere.amethyst.Main;
import dev.revere.amethyst.features.generators.listener.GeneratorListener;
import dev.revere.amethyst.features.generators.listener.GeneratorScheduler;

import java.util.*;

import dev.revere.amethyst.utils.chat.Style;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.HoloEasy;
import org.holoeasy.builder.HologramBuilder;
import org.holoeasy.config.HologramKey;
import org.holoeasy.pool.IHologramPool;

@Getter
public class GeneratorHandler {

    private final Main core;
    private final Map<UUID, Generator> generators = new HashMap<>();
    private final List<Location> generatorLocations;
    private final IHologramPool hologramPool;
    public GeneratorHandler(Main core) {
        this.core = core;
        this.generatorLocations = loadGeneratorLocations();
        this.hologramPool = HoloEasy.startInteractivePool(core, 60, 0.5f, 5f);

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

        HologramBuilder.hologram(new HologramKey(hologramPool, generator.getUuid().toString()), location.clone().add(0.5, -1 , 0.5), () -> {
            HologramBuilder.textline(Style.translate(generator.getType().display));
            HologramBuilder.textline(Style.translate("&l&6⤷ &7Amount: &f{} &6&l⤶"), generator.getAmount());
        });

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


    public final Optional<Generator> getGenerator(UUID uuid, boolean loadHologram) {
        if (generators.containsKey(uuid))
            return Optional.of(generators.get(uuid));

        Optional<Generator> generator = core.getStorage().loadGenerator(uuid);
        generator.ifPresent(value -> generators.put(uuid, value));

        if(loadHologram) {
            // create hologram
        }

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
