package org.loose.fis.av.exceptions;

public class InvalidCodeException extends Exception{
    private String username;

    public InvalidCodeException (String username){
        super(String.format("%s is not a valid code!",username));
        this.username = username;
    }
}
