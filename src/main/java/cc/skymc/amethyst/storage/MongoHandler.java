package cc.skymc.amethyst.storage;

import cc.skymc.amethyst.Main;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import cc.skymc.amethyst.utils.config.BasicConfigurationFile;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoHandler {

    private final Main plugin;
    private final BasicConfigurationFile config;

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> profiles;
    private MongoCollection<Document> generators;
    private MongoCollection<Document> tools;
    private MongoCollection<Document> islands;
    private MongoCollection<Document> grid;
    private MongoCollection<Document> enchants;

    public MongoHandler(Main plugin, BasicConfigurationFile config) {
        this.plugin = plugin;
        this.config = config;
        init();
    }

    public void init() {
        this.disableLogging();

        if (config.getBoolean("STORAGE.MONGO.URI-MODE")) {
            this.client = MongoClients.create(config.getString("STORAGE.MONGO.URI.CONNECTION_STRING"));
            this.database = client.getDatabase(config.getString("STORAGE.MONGO.URI.DATABASE"));

            this.profiles = this.database.getCollection("profiles");
            this.generators = this.database.getCollection("generators");
            this.tools = this.database.getCollection("tools");
            this.islands = this.database.getCollection("islands");
            this.grid = this.database.getCollection("grid");
            this.enchants = this.database.getCollection("enchants");

            plugin.getLogger().info("&7Initialized MongoDB successfully!");
            return;
        }

        boolean auth = config.getBoolean("STORAGE.MONGO.NORMAL.AUTHENTICATION.ENABLED");
        String host = config.getString("STORAGE.MONGO.NORMAL.HOST");
        int port = config.getInteger("STORAGE.MONGO.NORMAL.PORT");

        String uri = "mongodb://" + host + ":" + port;

        if (auth) {
            String username = config.getString("STORAGE.MONGO.NORMAL.AUTHENTICATION.USERNAME");
            String password = config.getString("STORAGE.MONGO.NORMAL.AUTHENTICATION.PASSWORD");
            uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }


        this.client = MongoClients.create(uri);
        this.database = client.getDatabase(config.getString("STORAGE.MONGO.URI.DATABASE"));

        this.profiles = this.database.getCollection("profiles");
        this.generators = this.database.getCollection("generators");
        this.tools = this.database.getCollection("tools");
        this.islands = this.database.getCollection("islands");
        this.grid = this.database.getCollection("grid");
        this.enchants = this.database.getCollection("enchants");

        plugin.getLogger().info("&7Initialized MongoDB successfully.");
    }

    public void shutdown() {
        plugin.getLogger().info("&7Disconnecting &cMongo&7...");
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (this.client != null) this.client.close();
        plugin.getLogger().info("&7Disconnected &cMongo &7Successfully!");
    }

    public void disableLogging() {
        Logger mongoLogger = Logger.getLogger( "com.mongodb" );
        mongoLogger.setLevel(Level.SEVERE);

        Logger legacyLogger = Logger.getLogger( "org.mongodb" );
        legacyLogger.setLevel(Level.SEVERE);
    }
}
