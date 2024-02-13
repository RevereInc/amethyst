package cc.skymc.amethyst.storage.impl.mongo;

import cc.skymc.amethyst.storage.MongoHandler;
import cc.skymc.amethyst.utils.location.Grid;
import cc.skymc.amethyst.utils.location.Region;
import org.bson.Document;

import java.util.Optional;

public class MongoGridStorage {

    private final MongoHandler mongoHandler;

    public MongoGridStorage(MongoHandler mongoHandler) {
        this.mongoHandler = mongoHandler;
    }

    public Optional<Grid> load() {
        final Document document = mongoHandler.getGrid().find().first();
        if(document == null)
            return Optional.empty();

        return Optional.of(new Grid(
                300, 300,
                new Region(
                        document.getInteger("x1"),
                        document.getInteger("y1"),
                        document.getInteger("z1"),
                        document.getInteger("x2"),
                        document.getInteger("y2"),
                        document.getInteger("z2")
                ),
                new Region(
                        document.getInteger("fx1"),
                        document.getInteger("fy1"),
                        document.getInteger("fz1"),
                        document.getInteger("fx2"),
                        document.getInteger("fy2"),
                        document.getInteger("fz2")
                ),
                Grid.Direction.valueOf(document.getString("fd"))
        ));
    }

    public void save(Grid grid) {
        final Document prev = mongoHandler.getGrid().find().first();
        final Document document = new Document();

        document.put("x1", grid.getFilledRegion().x1);
        document.put("y1", grid.getFilledRegion().y1);
        document.put("z1", grid.getFilledRegion().z1);
        document.put("x2", grid.getFilledRegion().x2);
        document.put("y2", grid.getFilledRegion().y2);
        document.put("z2", grid.getFilledRegion().z2);
        document.put("fx1", grid.getFillingRegion().region.x1);
        document.put("fy1", grid.getFillingRegion().region.y1);
        document.put("fz1", grid.getFillingRegion().region.z1);
        document.put("fx2", grid.getFillingRegion().region.x2);
        document.put("fy2", grid.getFillingRegion().region.y2);
        document.put("fz2", grid.getFillingRegion().region.z2);
        document.put("fd", grid.getFillingRegion().direction.name());

        if(prev != null)
            mongoHandler.getGrid().drop();

        mongoHandler.getGrid().insertOne(document);
    }

}
