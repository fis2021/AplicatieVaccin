package org.loose.fis.av.exceptions;

public class InvalidDateException extends Exception{
    private String name;

    public InvalidDateException(String name){
        super(String.format("%s is not a valid date!", name));
        this.name = name;
    }
}
