package org.loose.fis.av.exceptions;

public class EmptyFieldException extends Exception{

    public EmptyFieldException(){
        super("All fields must be filled out");
    }
}
