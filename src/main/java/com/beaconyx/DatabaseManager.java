package com.beaconyx;

/*import java.util.ArrayList;
import java.util.List;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.geojson.GeoJsonObjectType;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import com.mongodb.client.model.geojson.codecs.GeometryCodec;
import com.mongodb.internal.connection.ServerAddressHelper;
import org.bson.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;*/

@SuppressWarnings({"FieldCanBeLocal"})
public class DatabaseManager {

    /*private final String DATABASE_NAME = "FestivalApp";
    private final String USER_NAME = "FestivalBe";
    private final String PASSWORD = "flyinBe5152";
    private final String TABLE_NAME = "TB_Original_Data";

    DB getConnectedDatabase() {
        MongoCredential credential = MongoCredential.createCredential(USER_NAME, DATABASE_NAME, PASSWORD.toCharArray());
        MongoClient client = new MongoClient(ServerAddressHelper.createServerAddress("183.111.236.65", 27017), credential, MongoClientOptions.builder().build());
        return client.getDB(DATABASE_NAME);
    }

    void insertList(DB db, List<Item> items) {
        DBCollection collection = db.getCollection(TABLE_NAME);
        List<BasicDBObject> documents = new ArrayList<BasicDBObject>();

        for (Item item : items) {
            BasicDBObject document = new BasicDBObject();
            document.put(COL_CONTENT_ID, item.getContentid());
            document.put(COL_TITLE, item.getTitle());
            document.put(COL_EVENT_START_DATE, item.getEventstartdate());
            document.put(COL_EVENT_END_DATE, item.getEventenddate());
            document.put(COL_MAP_X, Double.parseDouble(item.getMapx()));
            document.put(COL_MAP_Y, Double.parseDouble(item.getMapy()));
            document.put(COL_MODIFIED_TIME, item.getModifiedtime());
            document.put(COL_READ_COUNT, Integer.parseInt(item.getReadcount()));
            document.put(COL_LIKE_COUNT, 0);

            List<Double> coord = new ArrayList<Double>();
            coord.add(Double.parseDouble(item.getMapx()));
            coord.add(Double.parseDouble(item.getMapy()));

            document.put(COL_MAP_POINT, coord);

            documents.add(document);
        }
        collection.insert(documents);
        //collection.insertMany(documents, new InsertManyOptions());
    }*/
}
