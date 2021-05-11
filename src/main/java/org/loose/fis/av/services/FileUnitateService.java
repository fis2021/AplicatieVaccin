package org.loose.fis.av.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.av.model.Unitate;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class FileUnitateService {
    private static final String APPLICATION_FOLDER_UNITATE = ".aplicatie-vaccin-unitate";
    private static final String USER_FOLDER_UNITATE = System.getProperty("user.home");
    public static final Path APPLICATION_HOME_PATH_UNITATE = Paths.get(USER_FOLDER_UNITATE, APPLICATION_FOLDER_UNITATE);

    public static Path getPathToUnitate(String... path) {
        return APPLICATION_HOME_PATH_UNITATE.resolve(Paths.get(".", path));
    }

    public static ObjectRepository<Unitate> unitateRepository;

    public static void initDatabaseUnit() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToUnitate("Aplicatie-Vaccin-Unitate.db").toFile())
                .openOrCreate("test", "test");

        unitateRepository = database.getRepository(Unitate.class);
    }

    public static void addUnits(){
        unitateRepository.insert(new Unitate("Spitalul Judetean","Timisoara","TM1"));
        unitateRepository.insert(new Unitate("Spitalul Militar","Timisoara","TM2"));
        unitateRepository.insert(new Unitate("Spitalul CFR","Timisoara","TM3"));
    }

    public static int checkemptyplaces(String cod){
        for(Unitate unitate : unitateRepository.find()) {
            if(Objects.equals(cod,unitate.getCod_unit())) {
                return 100 - unitate.getContor();
            }
        }
        return 0;
    }


}
