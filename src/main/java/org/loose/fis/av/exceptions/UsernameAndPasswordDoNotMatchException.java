package org.loose.fis.av.exceptions;

public class UsernameAndPasswordDoNotMatchException extends Exception{

    public UsernameAndPasswordDoNotMatchException(){
        super("Username and Password do not match");

    }
}
