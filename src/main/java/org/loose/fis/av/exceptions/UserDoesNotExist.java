package org.loose.fis.av.exceptions;

public class UserDoesNotExist extends Exception{

    private String username;

    public UserDoesNotExist(String username) {
        super(String.format("User %s does not exist!", username));
        this.username = username;
    }

}
