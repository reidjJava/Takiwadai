package me.reidj.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    public static Properties loadFile(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
