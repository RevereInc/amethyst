package dev.revere.amethyst.storage.impl.mongo;

import dev.revere.amethyst.features.tool.Tool;
import dev.revere.amethyst.features.tool.enchants.Enchant;
import dev.revere.amethyst.features.tool.enchants.impl.*;
import dev.revere.amethyst.storage.MongoHandler;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;

public class MongoEnchantStorage {

    private final MongoHandler mongoHandler;

    public MongoEnchantStorage(MongoHandler handler) {
        this.mongoHandler = handler;
    }

    public List<Enchant> load(UUID uuid) {
        final List<Enchant> enchants = new ArrayList<>();
        final FindIterable<Document> documents = mongoHandler.getEnchants().find(Filters.eq("tool", uuid.toString()));
        for (final Document document : documents) {
            try {
                final Integer level = document.getInteger("level");
                final Optional<Enchant> enchant = switch (document.getString("name")) {
                    case "Haste" -> Optional.of(new HasteEnchant(level));
                    case "Level Finder" -> Optional.of(new LevelFinderEnchant(level));
                    case "Orbs Booster" -> Optional.of(new OrbsBoosterEnchant(level));
                    case "Point Finder" -> Optional.of(new CrystalFinderEnchant(level));
                    case "XP Boost" -> Optional.of(new XPBoostEnchant(level));
                    case "Speed" -> Optional.of(new SpeedEnchant(level));
                    case "Key Finder" -> Optional.of(new KeyFinderEnchant(level));

                    default -> {
                        /* XXX: database corruption? */
                        yield Optional.empty();
                    }
                };

                enchant.ifPresent(enchants::add);
            } catch (Exception ex) {
                /* XXX: database corruption? */
                ex.printStackTrace();
            }
        }

        return enchants;
    }

    public void save(Tool tool, Enchant enchant) {
        final Document d = new Document();

        d.put("tool", tool.getUuid().toString());
        d.put("name", enchant.name());
        d.put("level", enchant.level());

        mongoHandler.getEnchants().insertOne(d);
    }

    public void remove(Tool tool, Enchant enchant) {
        mongoHandler.getEnchants().deleteMany(Filters.and(
                Filters.eq("tool", tool.getUuid().toString()),
                Filters.eq("name", enchant.name())
        ));
    }

    public void remove(Tool tool) {
        mongoHandler.getEnchants().deleteMany(Filters.eq("tool", tool.getUuid().toString()));
    }

}
