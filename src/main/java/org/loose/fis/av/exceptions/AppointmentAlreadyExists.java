package org.loose.fis.av.exceptions;

public class AppointmentAlreadyExists extends Exception{

    public AppointmentAlreadyExists(){
        super("Appointment already made for this CNP");
    }
}
