package org.loose.fis.av.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.av.Main;
import org.loose.fis.av.exceptions.AppointmentAlreadyExists;
import org.loose.fis.av.exceptions.NoAppointmentsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUnitateServiceTest {
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
    @DisplayName("Check if appointment was added")
    void makeAppointment() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
    }

    @Test
    @DisplayName("Check if appointment already exists")
    void appointmentAlreadyExists() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertThrows(AppointmentAlreadyExists.class, () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
    }

    @Test
    @DisplayName("Check if appointment gets deleted")
    void checkdeleteappointment() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertDoesNotThrow( () -> FileUnitateService.deleteAppointment());
        assertThrows(NoAppointmentsException.class, () -> UserService.checkAppointment());
    }

}