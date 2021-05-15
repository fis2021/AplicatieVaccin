package org.loose.fis.av.services;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.loose.fis.av.Main;
import org.loose.fis.av.exceptions.*;

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
    @Test
    @DisplayName("Check if empty places are shown accordingly")
    void checkemptyplaces(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertEquals(99,FileUnitateService.checkemptyplaces("TM1"));
        assertDoesNotThrow( () -> UserService.addUser("test_patient2@yahoo.com","test","Test","test","1010101111122","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient2@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","25/06/2021"));
        assertEquals(98,FileUnitateService.checkemptyplaces("TM1"));

    }
    @Test
    @DisplayName("Check if a date is valid")
    void checkvaliddate(){
        assertDoesNotThrow( () -> FileUnitateService.checkValidDate("22/06/2021"));
        assertThrows(InvalidDateException.class, () -> FileUnitateService.checkValidDate("69/06/2021"));
        assertThrows(InvalidDateException.class, () -> FileUnitateService.checkValidDate("aa/bb/cccc"));
        assertThrows(InvalidDateException.class, () -> FileUnitateService.checkValidDate("lskdpoasts"));
        assertThrows(InvalidDateException.class, () -> FileUnitateService.checkValidDate("22.06.2021"));
    }
    @Test
    @DisplayName("Check if the appointment gets deleted by the manager")
    void checkdelappointmentmanager() {
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertDoesNotThrow( () -> FileUnitateService.deleteAppointmentManager("1010101111111"));
        assertThrows(NoAppointmentsException.class, () -> UserService.checkAppointment());

    }
    @Test
    @DisplayName("Check if the number of appointments gets modified")
    void checkmodifynumber(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertEquals(99,FileUnitateService.checkemptyplaces("TM1"));
        assertDoesNotThrow( () -> UserService.addUser("test_patient2@yahoo.com","test","Test","test","1010101111122","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient2@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","25/06/2021"));
        assertEquals(98,FileUnitateService.checkemptyplaces("TM1"));
        assertDoesNotThrow( () -> UserService.addUser("test_manager@yahoo.com","test","Test","test","TM1","Manager"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_manager@yahoo.com","test")));
        assertDoesNotThrow( () ->FileUnitateService.modifyAppointmentNumber(50));
        assertEquals(48,FileUnitateService.checkemptyplaces("TM1"));
        assertThrows( InvalidAvailableAppointments.class, () ->FileUnitateService.modifyAppointmentNumber(1));
    }
    @Test
    @DisplayName("Check if the reschedule appointment feature works")
    void checkreschedule(){
        assertDoesNotThrow( () -> UserService.addUser("test_patient@yahoo.com","test","Test","test","1010101111111","Patient"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_patient@yahoo.com","test")));
        assertDoesNotThrow( () -> FileUnitateService.addAppointment("Spitalul Judetean","23/06/2021"));
        assertDoesNotThrow( () -> UserService.addUser("test_manager@yahoo.com","test","Test","test","TM1","Manager"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_manager@yahoo.com","test")));
        assertEquals("23/06/2021",SessionServiceUnitate.getLoggedInUnitate().getProgramat(0).getData());
        assertDoesNotThrow( ()-> FileUnitateService.rescheduleAppointmentManager("1010101111111","26/07/2021"));
        assertDoesNotThrow( () -> SessionService.setUser(UserService.logIn("test_manager@yahoo.com","test")));
        assertEquals("26/07/2021",SessionServiceUnitate.getLoggedInUnitate().getProgramat(0).getData());
        assertThrows(InvalidDateException.class, ()-> FileUnitateService.rescheduleAppointmentManager("1010101111111","asdfghjklm"));

    }
    @Test
    @DisplayName("Check if it's a number")
    void checknumber(){
        assertDoesNotThrow( () -> FileUnitateService.isNumber("69"));
        assertThrows(MustBeNumberException.class, ()-> FileUnitateService.isNumber("asdfgh"));
        assertThrows(MustBeNumberException.class, ()-> FileUnitateService.isNumber("69df5h"));
    }

}