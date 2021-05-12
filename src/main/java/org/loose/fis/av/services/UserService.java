package org.loose.fis.av.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.av.exceptions.*;
import org.loose.fis.av.exceptions.InvalidEmailException;
import org.loose.fis.av.exceptions.InvalidCodeException;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.*;
import java.lang.*;
import java.io.*;

import static org.loose.fis.av.services.FileSystemService.getPathToFile;

public class UserService {

    public static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("Aplicatie-Vaccin.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }


    public static void addUser(String username, String password, String surname, String name, String code, String role) throws UsernameAlreadyExistsException, InvalidEmailException, InvalidCodeException {
        checkUserDoesNotAlreadyExist(username);
        checkValidEmail(username);
        checkValidCode(code,role);
        userRepository.insert(new User(username, encodePassword(username, password), surname, name, code, role));
    }
    public static User logIn(String username,String password) throws UserDoesNotExist,UsernameAndPasswordDoNotMatchException{
        checkUserDoesNotExist(username);
        checkUserAndPasswordDoNotMatch(username,password);
        User LoggedInUser=new User("fals","fals","fals","fals","fals","fals");
        Unitate LoggedInUnitate=new Unitate("fals","fals","fals");
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                if (Objects.equals(encodePassword(username, password), user.getPassword()))
                {
                    LoggedInUser=new User(user.getUsername(),user.getPassword(),user.getSurname(),user.getName(),user.getCode(),user.getRole());
                    if(Objects.equals(user.getRole(),"Manager")){
                        for(Unitate unitate : FileUnitateService.unitateRepository.find())
                        {
                            if(Objects.equals(user.getCode(),unitate.getCod_unit()))
                                SessionServiceUnitate.setUnitate(unitate);
                        }
                    }

                }
            }
            }
        return LoggedInUser;

        }



    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }
    private static void checkUserDoesNotExist(String username) throws UserDoesNotExist {
        boolean check =false;
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                check = true;
            }
        }
        if(!check) {
            throw new UserDoesNotExist(username);
        }
    }
    private static void checkUserAndPasswordDoNotMatch(String username,String password) throws UsernameAndPasswordDoNotMatchException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                if (!Objects.equals(encodePassword(username, password), user.getPassword()))

                    throw new UsernameAndPasswordDoNotMatchException();
            }
        }
    }

    private static void checkValidEmail(String username) throws InvalidEmailException {
        if(username.contains("@yahoo.com") == false && username.contains("@gmail.com") == false && username.contains("@student.upt.ro") == false){
            throw new InvalidEmailException(username);
        }
    }

    private static void checkValidCode (String code, String role) throws InvalidCodeException {
        if (code.length() != 13 && Objects.equals("Patient", role)) {
            throw new InvalidCodeException(code);
        }
        else {
            if(code.length() != 3 && Objects.equals("Manager",role)){
                throw new InvalidCodeException(code);
            }
        }
    }

    public static boolean checkifAppointmentExists(){
        boolean ok = false;
        for(Unitate unitate : FileUnitateService.unitateRepository.find()){
            for(int i = 0; i < unitate.getContor(); i++){
                if(unitate.getProgramat(i) != null){
                    if(Objects.equals(unitate.getProgramat(i).getCNP(),SessionService.getLoggedInUser().getCode())){
                        ok = true;
                    }
                }

            }
        }
        return ok;
    }

    public static void checkAppointment() throws NoAppointmentsException {
        if(checkifAppointmentExists() == false){
            throw  new NoAppointmentsException();
        }
    }

    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }


}
