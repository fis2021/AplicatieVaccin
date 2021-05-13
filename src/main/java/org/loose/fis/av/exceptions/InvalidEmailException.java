package org.loose.fis.av.exceptions;

public class InvalidEmailException extends Exception {
    private String username;

    public InvalidEmailException (String username) {
        super(String.format("%s is not a valid email adress!", username));
        this.username = username;
    }
}
