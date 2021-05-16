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
class ManagerRescheduleControllerTest {
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
    @DisplayName("Check if the appointment gets rescheduled")
    void checkreschedule(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write("1010101111111");
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
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
        robot.clickOn("Reprogrameaza");
        robot.clickOn("#pacientRe").clickOn("test test");
        robot.clickOn("#data").write("29/08/2021");
        robot.clickOn("#remessage").write("Suntem understaffed");
        robot.clickOn("Reprogrameaza");

        assertEquals("Pacientul a fost reprogramat pe data de 29/08/2021",robot.lookup("#successmessage").queryText().getText());

    }
    @Test
    @DisplayName("Check the all fields must be filled out exception")
    void checkempty(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write("1010101111111");
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
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
        robot.clickOn("Reprogrameaza");
        robot.clickOn("Reprogrameaza");
        assertEquals("All fields must be filled out",robot.lookup("#successmessage").queryText().getText());
        robot.clickOn("#pacientRe").clickOn("test test");
        robot.clickOn("Reprogrameaza");
        assertEquals("All fields must be filled out",robot.lookup("#successmessage").queryText().getText());
        robot.clickOn("#data").write("29/08/2021");
        robot.clickOn("Reprogrameaza");
        assertEquals("All fields must be filled out",robot.lookup("#successmessage").queryText().getText());
        robot.clickOn("#remessage").write("Sotia mea naste in ziua aia.");
        robot.clickOn("Reprogrameaza");

        assertEquals("Pacientul a fost reprogramat pe data de 29/08/2021",robot.lookup("#successmessage").queryText().getText());
    }
    @Test
    @DisplayName("Check if the invalid date is working")
    void checkdate(FxRobot robot){
        robot.clickOn("Register");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
        robot.clickOn("#passwordField").write(PASSWORD);
        robot.clickOn("#surnameField").write(SURNAME);
        robot.clickOn("#nameField").write(NAME);
        robot.clickOn("#codeField").write("1010101111111");
        robot.clickOn("#role").clickOn("Patient");
        robot.clickOn("Register");

        robot.clickOn("Log In");
        robot.clickOn("#usernameField").write("andrei.thonus@gmail.com");
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
        robot.clickOn("Reprogrameaza");
        robot.clickOn("#pacientRe").clickOn("test test");
        robot.clickOn("#data").write("asdasdadss");
        robot.clickOn("#remessage").write("Suntem understaffed");
        robot.clickOn("Reprogrameaza");
        assertEquals("asdasdadss is not a valid date!",robot.lookup("#successmessage").queryText().getText());
    }

    @Test
    @DisplayName("Check if the home buttons is working")
    void checkhome(FxRobot robot){
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
        robot.clickOn("Home");
        robot.clickOn("Reprogrameaza");
        robot.clickOn("Home");
    }


}