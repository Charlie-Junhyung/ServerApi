package com.beaconyx;

import com.beaconyx.api.ApiManager;
import com.beaconyx.api.XmlParser;
import com.beaconyx.db.ParseManager;
import com.beaconyx.log.MyLogger;
import com.beaconyx.model.Item;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static List<Item> refiningDataSet(List<Item> list) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -2);
        Date yesterday = cal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        Iterator<Item> iterator = list.iterator();
        while (iterator.hasNext()) {
            Item temp = iterator.next();
            if (temp.getModifiedtime() == null) {
                iterator.remove();
            } else {
                try {
                    Date modifiedTime = formatter.parse(temp.getModifiedtime());
                    if (modifiedTime.before(yesterday))
                        iterator.remove();
                } catch(ParseException e) {
                }
            }
        }

        return list;
    }

    public static void main(String[] args) {
        MyLogger.z("Start to connecting API...");
        ApiManager api = new ApiManager();
        String responseXml = api.getData();

        MyLogger.z("Start to parsing xml response...");

        XmlParser parser = new XmlParser();
        List<Item> itemList = parser.parse(responseXml);

        if (itemList == null) {
            MyLogger.z("occured xml parsing error");
            throw new RuntimeException("API returns OK code but there is no data!");
        } else {
            MyLogger.z(itemList.size() + " items parsed successfully");
        }

        List<Item> refinedList = refiningDataSet(itemList);
        refinedList.forEach(System.out::println);
        System.out.println(refinedList.size() + " items updated at yesterday");

        ParseManager parseManager = new ParseManager();
        //parseManager.insertAll(itemList);
        parseManager.updateAll(refinedList);
    }
}
