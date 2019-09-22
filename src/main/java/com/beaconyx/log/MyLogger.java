package com.beaconyx.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {
    private static final String FILENAME = "AutoDBUpdater.log";

    public static void z(String message) {
        BufferedOutputStream output;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S | ");

        try {
            output = new BufferedOutputStream(new FileOutputStream(FILENAME, true));
            StringBuilder builder = new StringBuilder(message);
            builder.insert(0, formatter.format(new Date()));
            builder.append("\n");

            output.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
