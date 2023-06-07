package me.reidj.application.util;

import java.io.FileNotFoundException;
import java.net.URL;

public class PathHelper {

    public static URL getResource(String path) throws FileNotFoundException {
        if (PathHelper.class.getResource(path) == null) {
            throw new FileNotFoundException(String.format("File %s not found", path));
        }
        return PathHelper.class.getResource(path);
    }
}
