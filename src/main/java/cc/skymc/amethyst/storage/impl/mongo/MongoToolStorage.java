package cc.skymc.amethyst.storage.impl.mongo;

import cc.skymc.amethyst.features.tool.enchants.Enchant;
import cc.skymc.amethyst.storage.MongoHandler;
import cc.skymc.amethyst.features.tool.Tool;
import cc.skymc.amethyst.features.tool.ToolRarity;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.Optional;
import java.util.UUID;
import org.bson.Document;

public class MongoToolStorage  {

  private final MongoHandler mongoHandler;
  private final MongoEnchantStorage mongoEnchantStorage;

  public MongoToolStorage(MongoHandler handler, MongoEnchantStorage enchants) {
    this.mongoHandler = handler;
    this.mongoEnchantStorage = enchants;
  }

  public Optional<Tool> load(UUID uuid) {
      final Document document = mongoHandler.getTools().find(Filters.eq("_id", uuid.toString())).first();
      if (document == null)
        return Optional.empty();

      return Optional.of(new Tool(
          uuid,
          ToolRarity.valueOf(document.getString("rarity")),
          mongoEnchantStorage.load(uuid)
      ));
  }

  public void save(Tool tool) {
    mongoEnchantStorage.remove(tool);
    for(Enchant enchant : tool.getEnchants())
      mongoEnchantStorage.save(tool, enchant);

    final Document document = mongoHandler.getTools().find(Filters.eq("_id", tool.getUuid().toString())).first();
    if (document == null) {
      mongoHandler.getTools().insertOne(tool.toBson());
      return;
    }

    mongoHandler.getTools().replaceOne(document, tool.toBson(), new ReplaceOptions().upsert(true));
  }
}
