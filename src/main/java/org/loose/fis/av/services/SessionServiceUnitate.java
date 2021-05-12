package org.loose.fis.av.services;

import org.loose.fis.av.model.Unitate;

public class SessionServiceUnitate{
    public static Unitate LoggedInUnitate;

    public static void setUnitate(Unitate unitate){LoggedInUnitate=unitate;}

    public static Unitate getLoggedInUnitate(){return LoggedInUnitate;}
}

