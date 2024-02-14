package cc.skymc.amethyst.storage.impl.mongo;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.generators.GeneratorType;
import cc.skymc.amethyst.storage.MongoHandler;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MongoGeneratorStorage {

    private final Main core;
    private final MongoHandler mongoHandler;

    public MongoGeneratorStorage(Main core, MongoHandler handler) {
        this.core = core;
        this.mongoHandler = handler;
    }

    public Optional<Generator> load(UUID uuid) {
        final Document document = mongoHandler.getGenerators().find(Filters.eq("_id", uuid.toString())).first();
        if (document == null)
            return Optional.empty();

        Location location = new Location(
                Bukkit.getWorld(document.getString("world")),
                document.getDouble("x"),
                document.getDouble("y"),
                document.getDouble("z")
        );

        int amount = document.getInteger("amount");

        return Optional.of(new Generator(
                uuid,
                UUID.fromString(document.getString("owner")),
                GeneratorType.valueOf(document.getString("type")),
                location, amount
        ));
    }

    public void save(Generator gen) {
        final Document document = mongoHandler.getGenerators().find(Filters.eq("_id", gen.getUuid().toString())).first();
        if (document == null) {
            mongoHandler.getGenerators().insertOne(gen.toBson());
            return;
        }

        mongoHandler.getGenerators().replaceOne(document, gen.toBson(), new ReplaceOptions().upsert(true));
    }

    public void remove(Generator gen) {
        mongoHandler.getGenerators().deleteMany(Filters.eq("_id", gen.getUuid()));
    }
}
