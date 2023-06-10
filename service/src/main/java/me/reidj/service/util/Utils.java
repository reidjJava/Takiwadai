package me.reidj.service.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    public static final String RESOURCES = "service/src/main/resources/";

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
