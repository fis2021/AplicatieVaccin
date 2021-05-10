package org.loose.fis.av.services;

import org.loose.fis.av.model.User;

public class SessionService{
    public static User LoggedInUser;

    public static void setUser(User user){
        LoggedInUser = user;
    }

    public static User getLoggedInUser(){
        return LoggedInUser;
    }
}
