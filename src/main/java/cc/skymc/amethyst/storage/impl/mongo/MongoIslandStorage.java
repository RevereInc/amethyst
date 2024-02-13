package cc.skymc.amethyst.storage.impl.mongo;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.islands.Island;
import cc.skymc.amethyst.storage.MongoHandler;
import cc.skymc.amethyst.utils.location.Region;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.Optional;
import java.util.UUID;
import org.bson.Document;

public class MongoIslandStorage  {

  private final MongoHandler mongoHandler;

  public MongoIslandStorage(MongoHandler handler) {
    this.mongoHandler = handler;
  }

  public Optional<Island> load(UUID uuid) {
      final Document document = mongoHandler.getIslands().find(Filters.eq("_id", uuid.toString())).first();
      if (document == null)
        return Optional.empty();

      return Optional.of(new Island(
          uuid,
          document.get("uuid", UUID.class),
          document.getList("members", UUID.class),
          new Region(
            document.getInteger("x1"),
            document.getInteger("y1"),
            document.getInteger("z1"),
            document.getInteger("x2"),
            document.getInteger("y2"),
            document.getInteger("z2")
          ),
          document.getLong("crystals")
      ));
  }

  public void save(Island island) {
    final Document document = mongoHandler.getIslands().find(Filters.eq("_id", island.getUuid().toString())).first();

    if (document == null) {
      mongoHandler.getIslands().insertOne(island.toBson());
      return;
    }

    mongoHandler.getIslands().replaceOne(document, island.toBson(), new ReplaceOptions().upsert(true));
  }
}
