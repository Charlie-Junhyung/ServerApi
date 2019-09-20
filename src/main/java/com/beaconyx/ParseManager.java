package com.beaconyx;

import org.parse4j.*;
import org.parse4j.callback.SaveCallback;
import org.parse4j.command.ParseBatchCommand;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ParseManager {

    class Constants {
        static final String APP_ID = "FestivalbeconyxjAZx4r86akomIweGfdfOLbT2CnQKFcQ5";
        static final String REST_KEY = "mFxXWGAXodYefwxYYHV3SDJO7hd34ddXEho3V5de";
        static final String SERVER_URL = "http://www.beaconyx.co.kr:1337/parse";

        static final String TABLE_NAME = "TB_Original_Data";
        static final String COL_CONTENT_ID = "contentid";
        static final String COL_TITLE = "title";
        static final String COL_EVENT_START_DATE = "eventstartdate";
        static final String COL_EVENT_END_DATE = "eventenddate";
        static final String COL_FIRST_IMAGE = "firstimage";
        static final String COL_MAP_X = "mapx";
        static final String COL_MAP_Y = "mapy";
        static final String COL_READ_COUNT = "readcount";
        static final String COL_MODIFIED_TIME = "modifiedtime";
        static final String COL_LIKE_COUNT = "likecount";
        static final String COL_MAP_POINT = "mappoint";
    }

    private Queue<Item> queue;

    public void parseTask(List<Item> items) {
        Parse.initialize(Constants.APP_ID, Constants.REST_KEY, Constants.SERVER_URL);

        queue = new LinkedList<>(items);
        batching(queue);
    }

    private void batching(Queue<Item> items) {
        if (items.size() == 0) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + " | Successfully stored data set");
            return;
        }


        ParseObject originalData = new ParseObject(Constants.TABLE_NAME);
        Item item = items.poll();
        originalData.put(Constants.COL_CONTENT_ID, item.getContentid());
        originalData.put(Constants.COL_TITLE, item.getTitle());
        originalData.put(Constants.COL_EVENT_START_DATE, item.getEventstartdate());
        originalData.put(Constants.COL_EVENT_END_DATE, item.getEventenddate());
        originalData.put(Constants.COL_FIRST_IMAGE, item.getFirstimage());
        originalData.put(Constants.COL_MAP_X, Double.parseDouble(item.getMapx()));
        originalData.put(Constants.COL_MAP_Y, Double.parseDouble(item.getMapy()));
        originalData.put(Constants.COL_READ_COUNT, Integer.parseInt(item.getReadcount()));
        originalData.put(Constants.COL_MODIFIED_TIME, item.getModifiedtime());
        originalData.put(Constants.COL_LIKE_COUNT, 0);

        double lat = Double.parseDouble(item.getMapy()),
                lng = Double.parseDouble(item.getMapx());
        originalData.put(Constants.COL_MAP_POINT, new ParseGeoPoint(lat, lng));

        originalData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException parseException) {
                batching(queue);
            }
        });
    }
}
