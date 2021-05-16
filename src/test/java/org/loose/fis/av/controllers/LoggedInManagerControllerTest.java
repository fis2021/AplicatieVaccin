package org.loose.fis.av.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loose.fis.av.Main;
import org.loose.fis.av.services.FileSystemService;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class LoggedInManagerControllerTest {
    private static final String USERNAME_MANAGER = "test_manager@gmail.com";
    private static final String PASSWORD = "test";
    private static final String SURNAME = "test";
    private static final String NAME = "test";
    private static final String COD = "TM1";

    @BeforeAll
    static void beforeall(){
        FileSystemService.setApplicationFolder(".aplicatie-vaccin-test");
        FileUnitateService.setApplicationFolderUnitate(".aplicatie-vaccin-unitate-test");

        Main.initDirectory();
        Main.initDirectoryUnitate();
    }

    @AfterAll
    static void afterAll() throws IOException {
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomePath().toFile());
        FileUtils.cleanDirectory(FileUnitateService.getApplicationHomePath().toFile());
    }

    @BeforeEach
    void setUp()  throws IOException {
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomePath().toFile());
        FileUtils.cleanDirectory(FileUnitateService.getApplicationHomePath().toFile());


        UserService.initDatabase();
        FileUnitateService.initDatabaseUnit();
        FileUnitateService.addUnits();
    }

    @AfterEach
    void tearDown(){
        UserService.closeDatabase();
        FileUnitateService.closeDatabase();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("AplicatieVaccin - Testing");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    @Test
    @DisplayName("Check if fields are properly displayed")
    void checkproperlyshown(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(COD);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        assertEquals("Spitalul Judetean",robot.lookup("#numeunit").queryText().getText());
        assertEquals(COD,robot.lookup("#codunit").queryText().getText());
        assertEquals("100",robot.lookup("#nrlocuri").queryText().getText());
    }

    @Test
    @DisplayName("Test if log out button works")
    void checklogout(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(COD);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Log out");

    }

    @Test
    @DisplayName("Test if delete appointment button works")
    void checkdelappscene(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(COD);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Anuleaza Programare");

    }

    @Test
    @DisplayName("Test if Modifica Locuri Disponibile button works")
    void checklocuridisponibile(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(COD);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Modifica Locuri Disponibile");

    }

    @Test
    @DisplayName("Test if reprogrameaza button works")
    void checkreprog(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(COD);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Reprogrameaza");

    }

}