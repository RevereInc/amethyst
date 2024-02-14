package cc.skymc.amethyst.storage.impl.mongo;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.generators.GeneratorHandler;
import cc.skymc.amethyst.features.islands.IslandHandler;
import cc.skymc.amethyst.profile.Profile;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import cc.skymc.amethyst.storage.MongoHandler;

public class MongoProfileStorage {

    private final MongoHandler mongoHandler;
    private final GeneratorHandler generatorHandler;
    private final IslandHandler islandHandler;

    public MongoProfileStorage(Main core, MongoHandler handler) {
        this.mongoHandler = handler;
        this.generatorHandler = core.getGeneratorHandler();
        this.islandHandler = core.getIslandHandler();
    }

    public Optional<Profile> load(UUID uuid) {
        final Document document = mongoHandler.getProfiles().find(Filters.eq("_id", uuid.toString())).first();
        if (document == null)
            return Optional.empty();

        final Profile profile = new Profile(uuid);

        profile.setLevel(document.getInteger("level"));
        profile.setXp(document.getDouble("xp"));
        profile.setPrestige(document.getInteger("prestige"));
        profile.setSlots(document.getInteger("slots"));
        profile.setMoneyMultiplier(document.getDouble("moneyMulti"));
        profile.setOrbsMultiplier(document.getDouble("orbsMulti"));
        profile.setXpMultiplier(document.getDouble("xpMulti"));
        profile.setBalance(document.getLong("balance"));
        profile.setOrbs(document.getLong("orbs"));
        profile.setCrystals(document.getLong("crystals"));
        profile.setAutoSell(document.getBoolean("autoSell"));

        List<Generator> generators = new ArrayList<>();
        List<UUID> uuidList = document.get("generators", ArrayList.class);
        for (UUID id : uuidList) {
            Optional<Generator> generator = generatorHandler.getGenerator(id);
            if (generator.isEmpty())
                continue;

            generators.add(generator.get());
        }

        profile.setGenerators(generators);

        UUID islandID = document.get("island", UUID.class);
        if (islandID != null)
            islandHandler.getIsland(islandID).ifPresent(island -> profile.setIsland(islandID));

        return Optional.of(profile);
    }

    public void save(Profile profile) {
        final Document document = mongoHandler.getProfiles().find(Filters.eq("_id", profile.getUuid().toString())).first();
        if (document == null) {
            mongoHandler.getProfiles().insertOne(profile.toBson());
            return;
        }

        mongoHandler.getProfiles().replaceOne(document, profile.toBson(), new ReplaceOptions().upsert(true));
    }

}
