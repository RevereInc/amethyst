package dev.revere.amethyst.features.generators;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class Generator {
    public UUID uuid;
    public UUID owner;
    public GeneratorType type;
    public Location location;
    public int amount;

    public final Document toBson() {
        Document document = new Document("_id", uuid.toString());

        document.append("uuid", uuid);
        document.append("owner", owner.toString());
        document.append("type", type.name());
        document.append("amount", amount);
        document.append("world", Objects.requireNonNull(location.getWorld()).getName());
        document.append("x", location.getX());
        document.append("y", location.getY());
        document.append("z", location.getZ());

        return document;
    }
}
