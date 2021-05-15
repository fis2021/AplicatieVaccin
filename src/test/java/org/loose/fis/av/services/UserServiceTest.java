package org.loose.fis.av.services;

import javafx.scene.control.PasswordField;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.av.Main;
import org.loose.fis.av.exceptions.*;
import org.loose.fis.av.model.User;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

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
        SessionService.closeSession();
    }

    @Test
    @DisplayName("Check if patient users are added correctly")
    void addUserPatient() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertEquals(1, UserService.getAllUsersTEST().size());
        assertEquals("Patient", UserService.getAllUsersTEST().get(0).getRole());
        assertEquals("test_patient@yahoo.com",UserService.getAllUsersTEST().get(0).getUsername());
    }

    @Test
    @DisplayName("Check if manager users are added correctly")
    void addUserManager() {
        assertDoesNotThrow( () -> UserService.addUser("test_manager@yahoo.com","test","Test","test","TM1","Manager"));
        assertEquals(1, UserService.getAllUsersTEST().size());
        assertEquals("Manager", UserService.getAllUsersTEST().get(0).getRole());
        assertEquals("test_manager@yahoo.com",UserService.getAllUsersTEST().get(0).getUsername());
    }

    @Test
    @DisplayName("Check if users don't already exist")
    void addUserAlreadyExist() {
        assertDoesNotThrow( () -> UserService.addUser("test@gmail.com","test","Test","test","1010101111111","Patient"));
        assertThrows(UsernameAlreadyExistsException.class, () -> UserService.addUser("test@gmail.com","test","Test","test","TM1","Manager"));
    }

    @Test
    @DisplayName("Check for valid e-mail")
    void validEmail(){
        assertThrows(InvalidEmailException.class, () -> UserService.checkValidEmail("test@test"));
    }

    @Test
    @DisplayName("Check for valid CNP")
    void validCNP() {
        assertEquals(true, UserService.checkCNP("1010101111111"));
        assertEquals(false, UserService.checkCNP("11/10a01111b1"));
    }

    @Test
    @DisplayName("Check for valid unit code")
    void validUnitCode() {
        assertEquals(true, UserService.checkUnit("TM1"));
        assertEquals(true, UserService.checkUnit("TM2"));
        assertEquals(true, UserService.checkUnit("TM3"));
        assertEquals(false, UserService.checkUnit("AR4"));
        assertEquals(false, UserService.checkUnit("COOKIE"));
    }

    @Test
    @DisplayName("Check for valid input code")
    void validCode() {
        assertThrows(InvalidCodeException.class, () -> UserService.checkValidCode("420","Patient"));
        assertThrows(InvalidCodeException.class, () -> UserService.checkValidCode("420a567891234","Patient"));
        assertThrows(InvalidCodeException.class, () -> UserService.checkValidCode("4230","Manager"));
        assertThrows(InvalidCodeException.class, () -> UserService.checkValidCode("420","Manager"));
        assertDoesNotThrow( () -> UserService.checkValidCode("1010101111111","Patient"));
        assertDoesNotThrow( () -> UserService.checkValidCode("TM1","Manager"));
        assertDoesNotThrow( () -> UserService.checkValidCode("TM2","Manager"));
        assertDoesNotThrow( () -> UserService.checkValidCode("TM3","Manager"));
    }

    @Test
    @DisplayName("Check if an appointment already exists")
    void verifyAppointment() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertEquals(true, UserService.checkifAppointmentExists());
    }

    @Test
    @DisplayName("Check if an appointment already exists and throws exception")
    void checkAppointment() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertThrows(NoAppointmentsException.class, () -> UserService.checkAppointment());
    }

    @Test
    @DisplayName("Check if  field is empty")
    void checkpass() {
        assertThrows(EmptyFieldException.class, () -> UserService.checkemptyfield(""));
    }

    @Test
    @DisplayName("Check if patient is in proper age group")
    void agegroup() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010106111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertEquals(1, UserService.grupvarsta());
        assertDoesNotThrow( () -> UserService.addUser("test_patient2@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient2@yahoo.com","test")));
        assertEquals(2, UserService.grupvarsta());
        assertDoesNotThrow( () -> UserService.addUser("test_patient3@yahoo.com","test","Test","test","1010140111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient3@yahoo.com","test")));
        assertEquals(3, UserService.grupvarsta());

    }
    @Test
    @DisplayName("Check the log in function for patient")
    void checkloginpatient(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010106111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        User user =new User("test_patient@yahoo.com","test","Test","test","1010106111111","Patient");
        assertEquals(user.getUsername(),SessionService.getLoggedInUser().getUsername());

    }
    @Test
    @DisplayName("Check the log in function for manager")
    void checkloginmanager(){
        assertDoesNotThrow( () -> UserService.addUser("test_manager@yahoo.com","test","Test","test","TM1","Manager"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_manager@yahoo.com","test")));
        User user =new User("test_manager@yahoo.com","test","Test","test","TM1","Manager");
        assertEquals(user.getUsername(),SessionService.getLoggedInUser().getUsername());

    }

    @Test
    @DisplayName("Check if user does not exist")
    void checkuserdoesnotexist(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010106111111","Patient"));
        assertDoesNotThrow( () -> UserService.checkUserDoesNotExist("test_patient@yahoo.com"));
        assertThrows(UserDoesNotExist.class, () -> UserService.checkUserDoesNotExist("mihaita"));
    }
    @Test
    @DisplayName("Check if username and password do not match")
    void checkuserpwdonotmatch(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010106111111","Patient"));
        assertDoesNotThrow( () -> UserService.checkUserAndPasswordDoNotMatch("test_patient@yahoo.com","test"));
        assertThrows(UsernameAndPasswordDoNotMatchException.class, () -> UserService.checkUserAndPasswordDoNotMatch("test_patient@yahoo.com","gresita"));

    }
}