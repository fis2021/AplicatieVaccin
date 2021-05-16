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

class MakeAppointmentControllerTest {

    private static final String USERNAME_PATIENT = "test_patient@gmail.com";
    private static final String PASSWORD = "test";
    private static final String SURNAME = "test";
    private static final String NAME = "test";
    private static final String CNP = "1010101111111";
    private static final String DATE = "25/07/2021";
    private static final String INVALID_DATE = "50/07/2021";


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
    @DisplayName("Test return home button")
    void returnhome(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("Home");
    }

    @Test
    @DisplayName("Test if appointment made successfully")
    void makeAppointmentSuccess(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("#UnitateV").clickOn("Spitalul Judetean");
        robot.clickOn("#data").write(DATE);
        robot.clickOn("Programeaza");

        assertEquals("Programarea a fost facuta cu success",robot.lookup("#appointmentmessage").queryText().getText());

    }

    @Test
    @DisplayName("Test if appointment already exists")
    void appointmentAlreadyExists(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("#UnitateV").clickOn("Spitalul Judetean");
        robot.clickOn("#data").write(DATE);
        robot.clickOn("Programeaza");
        robot.clickOn("Programeaza");

        assertEquals("Appointment already made for this CNP",robot.lookup("#appointmentmessage").queryText().getText());

    }

    @Test
    @DisplayName("Test if it checks for invalid provided date")
    void invalidProvidedDate(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("#UnitateV").clickOn("Spitalul Judetean");
        robot.clickOn("#data").write(INVALID_DATE);
        robot.clickOn("Programeaza");

        assertEquals(String.format("%s is not a valid date!", INVALID_DATE),robot.lookup("#appointmentmessage").queryText().getText());
    }

    @Test
    @DisplayName("Test if data field is empty")
    void emptyDate(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Log In");

        robot.clickOn("Programeaza");
        robot.clickOn("#UnitateV").clickOn("Spitalul Judetean");
        robot.clickOn("Programeaza");

        assertEquals("All fields must be filled out",robot.lookup("#appointmentmessage").queryText().getText());
    }

}