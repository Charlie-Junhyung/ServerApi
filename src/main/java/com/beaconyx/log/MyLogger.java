package com.beaconyx.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {
    private static String getFilename() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        //return "insert_" + df.format(new Date()) + ".log";
        return "auto_update_" + df.format(new Date()) + ".log";
    }

    public static void z(String message) {
        BufferedOutputStream output;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S | ");

        try {
            output = new BufferedOutputStream(new FileOutputStream(getFilename(), true));
            StringBuilder builder = new StringBuilder(message);
            builder.insert(0, formatter.format(new Date()));
            String line = System.getProperty("line.separator");
            builder.append(line);

            output.write(builder.toString().getBytes());
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
