package com.beaconyx.db;

import com.beaconyx.ConstantDepot;
import com.beaconyx.log.MyLogger;
import com.beaconyx.model.Item;
import org.parse4j.*;
import org.parse4j.callback.FindCallback;
import org.parse4j.callback.SaveCallback;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("Duplicates")
public class ParseManager {
    private int totalCount;

    public ParseManager() {
        Parse.initialize(ConstantDepot.APP_ID, ConstantDepot.REST_KEY, ConstantDepot.SERVER_URL);
    }

    /*public void updateAll(List<Item> items) {
        Queue<Item> queue = new LinkedList<>(items);
        totalCount = queue.size();
        updateRecursive(queue);
    }

    private void updateRecursive(Queue<Item> items) {
        if (items.size() == 0) {
            MyLogger.z(totalCount + " items updated successfully");
            System.exit(0);
        }

        Item item = items.poll();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ConstantDepot.TABLE_CHINESE_S);
        query.whereEqualTo(ConstantDepot.COL_CONTENT_ID, item.getContentid());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ParseObject object;
                if (list == null || list.size() == 0) {
                    object = new ParseObject(ConstantDepot.TABLE_CHINESE_S);
                    object.put(ConstantDepot.COL_CONTENT_ID, item.getContentid());
                    object.put(ConstantDepot.COL_LIKE_COUNT, 0);
                } else {
                    object = list.get(0);
                }

                object.put(ConstantDepot.COL_TITLE, item.getTitle());
                object.put(ConstantDepot.COL_AREA_CODE, item.getAreacode());
                object.put(ConstantDepot.COL_EVENT_START_DATE, item.getEventstartdate());
                object.put(ConstantDepot.COL_EVENT_END_DATE, item.getEventenddate());
                object.put(ConstantDepot.COL_FIRST_IMAGE, item.getFirstimage());
                object.put(ConstantDepot.COL_MAP_X, Double.parseDouble(item.getMapx()));
                object.put(ConstantDepot.COL_MAP_Y, Double.parseDouble(item.getMapy()));
                object.put(ConstantDepot.COL_READ_COUNT, Integer.parseInt(item.getReadcount()));
                object.put(ConstantDepot.COL_MODIFIED_TIME, item.getModifiedtime());
                double lat = Double.parseDouble(item.getMapy()),
                        lng = Double.parseDouble(item.getMapx());
                object.put(ConstantDepot.COL_MAP_POINT, new ParseGeoPoint(lat, lng));
                object.saveInBackground();

                updateRecursive(items);
            }
        });
    }*/

    public void insertAll(List<Item> items) {
        Queue<Item> queue = new LinkedList<>(items);
        totalCount = queue.size();
        insertRecursive(queue);
    }

    private void insertRecursive(Queue<Item> items) {
        if (items.size() == 0) {
            MyLogger.z(totalCount + " items saved successfully\n");
            System.exit(0);
        }

        Item item = items.poll();

        ParseObject originalData = new ParseObject(ConstantDepot.TABLE_RUSSIAN);
        originalData.put(ConstantDepot.COL_CONTENT_ID, item.getContentid());
        originalData.put(ConstantDepot.COL_TITLE, item.getTitle());
        originalData.put(ConstantDepot.COL_AREA_CODE, item.getAreacode());
        originalData.put(ConstantDepot.COL_EVENT_START_DATE, item.getEventstartdate());
        originalData.put(ConstantDepot.COL_EVENT_END_DATE, item.getEventenddate());
        originalData.put(ConstantDepot.COL_FIRST_IMAGE, item.getFirstimage());
        originalData.put(ConstantDepot.COL_MAP_X, Double.parseDouble(item.getMapx()));
        originalData.put(ConstantDepot.COL_MAP_Y, Double.parseDouble(item.getMapy()));
        originalData.put(ConstantDepot.COL_READ_COUNT, Integer.parseInt(item.getReadcount()));
        originalData.put(ConstantDepot.COL_MODIFIED_TIME, item.getModifiedtime());
        originalData.put(ConstantDepot.COL_LIKE_COUNT, 0);

        double lat = Double.parseDouble(item.getMapy()),
                lng = Double.parseDouble(item.getMapx());
        originalData.put(ConstantDepot.COL_MAP_POINT, new ParseGeoPoint(lat, lng));

        originalData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException parseException) {
                insertRecursive(items);
            }
        });
    }
}
