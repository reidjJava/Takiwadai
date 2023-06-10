package me.reidj.application.util;

import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.net.URL;

@UtilityClass
public class PathHelper {

    public URL getResource(String path) throws FileNotFoundException {
        if (PathHelper.class.getResource(path) == null) {
            throw new FileNotFoundException(String.format("File %s not found", path));
        }
        return PathHelper.class.getResource(path);
    }
}
