package org.loose.fis.av.exceptions;

public class InvalidAvailableAppointments extends Exception {


    public InvalidAvailableAppointments(){
        super("Numarul de programari nu poate fi mai mic de numarul de programati!");

    }
}
