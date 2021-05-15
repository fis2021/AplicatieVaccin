package org.loose.fis.av;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.loose.fis.av.model.User;
import org.loose.fis.av.services.FileSystemService;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.UserService;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        initDirectory();
        initDirectoryUnitate();
        UserService.initDatabase();
        FileUnitateService.initDatabaseUnit();
        //FileUnitateService.addUnits();
        // Se decomenteaza functia de mai sus daca este prima data cand rulati aplicatia.
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Aplicatie Inscriere Vaccin");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    public static void initDirectory() {
        Path applicationHomePath = FileSystemService.getApplicationHomePath();
        if (!Files.exists(applicationHomePath))
            applicationHomePath.toFile().mkdirs();
    }


    public static void initDirectoryUnitate() {
        Path applicationHomePathUnitate = FileUnitateService.getApplicationHomePath();
        if(!Files.exists(applicationHomePathUnitate))
            applicationHomePathUnitate.toFile().mkdirs();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
