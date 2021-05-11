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
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Aplicatie Inscriere Vaccin");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    private void initDirectory() {
        Path applicationHomePath = FileSystemService.APPLICATION_HOME_PATH;
        if (!Files.exists(applicationHomePath))
            applicationHomePath.toFile().mkdirs();
    }

    private void initDirectoryUnitate() {
        Path applicationHomePathUnitate = FileUnitateService.APPLICATION_HOME_PATH_UNITATE;
        if(!Files.exists(applicationHomePathUnitate))
            applicationHomePathUnitate.toFile().mkdirs();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
