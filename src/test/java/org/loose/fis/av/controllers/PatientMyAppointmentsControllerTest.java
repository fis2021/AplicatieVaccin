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

class PatientMyAppointmentsControllerTest {

    private static final String USERNAME_PATIENT = "test_patient@gmail.com";
    private static final String PASSWORD = "test";
    private static final String SURNAME = "test";
    private static final String NAME = "test";
    private static final String CNP = "1010101111111";
    private static final String DATE = "25/07/2021";

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
    @DisplayName("Test if appointment shows properly")
    void appointmentView(FxRobot robot) {
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
        robot.clickOn("Home");

        robot.clickOn("Programarile mele");

        assertEquals(SURNAME + " " + NAME,robot.lookup("#nume").queryText().getText());
        assertEquals(CNP,robot.lookup("#CNP").queryText().getText());
        assertEquals(CNP,robot.lookup("#CNP").queryText().getText());
        assertEquals("Spitalul Judetean",robot.lookup("#unitateP").queryText().getText());
        assertEquals(DATE,robot.lookup("#data").queryText().getText());


    }

    @Test
    @DisplayName("Test if delete function works")
    void deleteAppointment(FxRobot robot) {
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
        robot.clickOn("Home");

        robot.clickOn("Programarile mele");
        robot.clickOn("Sterge Programare");

        robot.clickOn("Programarile mele");

        assertEquals("Nu aveti programare facuta :)",robot.lookup("#errormessage").queryText().getText());
    }

    @Test
    @DisplayName("Test if home button works")
    void returnHome(FxRobot robot) {
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
        robot.clickOn("Home");

        robot.clickOn("Programarile mele");
        robot.clickOn("Home");
    }
}