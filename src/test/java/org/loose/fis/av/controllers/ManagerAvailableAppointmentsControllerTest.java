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
class ManagerAvailableAppointmentsControllerTest {
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
    @DisplayName("Check modify number invalid number")
    void checkvalidnumber(FxRobot robot){
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

        robot.clickOn("#nrprog").write("asdf");
        robot.clickOn("Modifica Programari Disponibile");

        assertEquals("Va rog introduceti un numar!",robot.lookup("#successMessage").queryText().getText());

    }

    @Test
    @DisplayName("Check the home button")
    void checkhome(FxRobot robot) {
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
        robot.clickOn("Home");
        robot.clickOn("Modifica Locuri Disponibile");
        robot.clickOn("Home");
    }
    @Test
    @DisplayName("Check fi the modification was successful")
    void checkworking(FxRobot robot){
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

        assertEquals("100",robot.lookup("#nrlocuri").queryText().getText());
        robot.clickOn("Modifica Locuri Disponibile");

        robot.clickOn("#nrprog").write("50");
        robot.clickOn("Modifica Programari Disponibile");
        assertEquals("Numarul de locuri a fost modificat cu succes!",robot.lookup("#successMessage").queryText().getText());
        robot.clickOn("Home");
        assertEquals("50",robot.lookup("#nrlocuri").queryText().getText());
    }

    @Test
    @DisplayName("Check if the modified number is not less than the number of appointed patients")
    void checklessthanpatients(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write("pacient@yahoo.com");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write("1010101111111");
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write("pacient@yahoo.com");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("#UnitateV").clickOn("Spitalul Judetean");
        robot.clickOn("#data").write("25/07/2021");
        robot.clickOn("Programeaza");
        robot.clickOn("Home");
        robot.clickOn("Log out");

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

        assertEquals("99",robot.lookup("#nrlocuri").queryText().getText());
        robot.clickOn("Modifica Locuri Disponibile");

        robot.clickOn("#nrprog").write("0");
        robot.clickOn("Modifica Programari Disponibile");
        assertEquals("Numarul de programari nu poate fi mai mic de numarul de programati!",robot.lookup("#successMessage").queryText().getText());

    }
}