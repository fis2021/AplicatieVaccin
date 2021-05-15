package org.loose.fis.av.services;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemService {
    private static String APPLICATION_FOLDER = ".aplicatie-vaccin";
    private static final String USER_FOLDER = System.getProperty("user.home");


    public static void setApplicationFolder(String applicationFolder){
        APPLICATION_FOLDER = applicationFolder;
    }

    public static Path getApplicationHomePath(){
        return Paths.get(USER_FOLDER, APPLICATION_FOLDER);
    }

    public static Path getPathToFile(String... path) {
        return getApplicationHomePath().resolve(Paths.get(".", path));
    }
}
