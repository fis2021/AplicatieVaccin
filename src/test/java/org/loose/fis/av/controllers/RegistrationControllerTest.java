package org.loose.fis.av.controllers;

import javafx.application.Platform;
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
class RegistrationControllerTest {

    private static final String USERNAME_PATIENT = "test_patient@gmail.com";
    private static final String USERNAME_MANAGER = "test_manager@gmail.com";
    private static final String PASSWORD = "test";
    private static final String SURNAME = "test";
    private static final String NAME = "test";
    private static final String CNP = "1010101111111";
    private static final String INVALIDCNP = "/010a011b111c";
    private static final String UNIT_CODE = "TM1";
    private static final String INVALID_UNIT_CODE = "420";

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
    @DisplayName("Test switching between buttons")
    void switchbuttons(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("Log In");
    }

    @Test
    @DisplayName("Test successuful registration for a patient")
    void registerPatient(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        assertEquals("Account created successfully!",robot.lookup("#registrationMessage").queryText().getText());
        assertEquals(USERNAME_PATIENT, UserService.getAllUsersTEST().get(0).getUsername());
    }

    @Test
    @DisplayName("Test successuful registration for a manager")
    void registerManager(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(UNIT_CODE);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        assertEquals("Account created successfully!",robot.lookup("#registrationMessage").queryText().getText());
        assertEquals(USERNAME_MANAGER, UserService.getAllUsersTEST().get(0).getUsername());
    }

    @Test
    @DisplayName("Test register with empty field")
    void registerEmptyField(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());

        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());

        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());

        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());

        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());

        robot.clickOn("#codeField").write(CNP);
        robot.clickOn("Register");
        assertEquals("All fields must be filled out",robot.lookup("#registrationMessage").queryText().getText());
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        assertEquals("Account created successfully!",robot.lookup("#registrationMessage").queryText().getText());
        assertEquals(USERNAME_PATIENT, UserService.getAllUsersTEST().get(0).getUsername());
    }

    @Test
    @DisplayName("Test to create a new account with an existing username")
    void registerExistingUsername(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(UNIT_CODE);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");
        robot.clickOn("Register");

        assertEquals(String.format("An account with the username %s already exists!", USERNAME_MANAGER),robot.lookup("#registrationMessage").queryText().getText());


    }

    @Test
    @DisplayName("Test registering with an invalid username input")
    void registerInvalidUsername(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write("test_manager@test");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(UNIT_CODE);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        assertEquals(String.format("%s is not a valid email adress!", "test_manager@test"),robot.lookup("#registrationMessage").queryText().getText());
    }

    @Test
    @DisplayName("Test registering a patient with an invalid code")
    void registerPatientInvalidCode(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_PATIENT);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(INVALIDCNP);
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        assertEquals(String.format("%s is not a valid code!",INVALIDCNP),robot.lookup("#registrationMessage").queryText().getText());
    }

    @Test
    @DisplayName("Test registering a manager with an invalid code")
    void registerManagerInvalidCode(FxRobot robot) {
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write(USERNAME_MANAGER);
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write(INVALID_UNIT_CODE);
        robot.clickOn("#role").clickOn("Manager");
        robot.clickOn("Register");

        assertEquals(String.format("%s is not a valid code!",INVALID_UNIT_CODE),robot.lookup("#registrationMessage").queryText().getText());
    }

}