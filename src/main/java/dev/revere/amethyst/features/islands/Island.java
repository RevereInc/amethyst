package dev.revere.amethyst.features.islands;

import dev.revere.amethyst.utils.location.Region;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class Island {
    public UUID uuid;
    public UUID leader;
    public List<UUID> members;
    public Region region;
    public Long crystals;

    public final Document toBson() {
        Document document = new Document("_id", uuid.toString());

        document.append("uuid", uuid);
        document.append("leader", leader);
        document.append("members", members);
        document.append("crystals", crystals);

        document.append("x1", region.x1);
        document.append("x2", region.x2);
        document.append("y1", region.y1);
        document.append("y2", region.y2);
        document.append("z1", region.z1);
        document.append("z2", region.z2);

        return document;
    }
}
