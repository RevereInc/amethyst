package cc.skymc.amethyst.profile;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.islands.Island;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
public class Profile {
    private final UUID uuid;

    private int level;
    private double xp;
    private int slots;

    private int prestige;

    private double moneyMultiplier;
    private double orbsMultiplier;
    private double xpMultiplier;

    private long balance;
    private long orbs;
    private long crystals;

    private boolean autoSell;

    private List<Generator> generators;
    private UUID island;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.generators = new ArrayList<>();
    }

    public final Document toBson() {
        Document document = new Document("_id", uuid.toString());

        document.append("level", level);
        document.append("xp", xp);
        document.append("slots", slots);
        document.append("prestige", prestige);
        document.append("moneyMulti", moneyMultiplier);
        document.append("orbsMulti", orbsMultiplier);
        document.append("xpMulti", xpMultiplier);
        document.append("balance", balance);
        document.append("orbs", orbs);
        document.append("crystals", crystals);
        document.append("autoSell", autoSell);

        List<UUID> generatorIDs = new ArrayList<>();
        for(Generator generator : generators) {
            generatorIDs.add(generator.getUuid());
        }

        document.append("generators", generatorIDs);
        document.append("island", island);

        return document;
    }

    public Optional<Island> getIsland() {
        if(island == null)
            return Optional.empty();

        return Main.getInstance().getIslandHandler().getIsland(island);
    }

    public boolean getAutoSell() {
        return autoSell;
    }
}
