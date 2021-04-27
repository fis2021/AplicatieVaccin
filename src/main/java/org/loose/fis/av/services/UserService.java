package org.loose.fis.av.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.av.exceptions.InvalidEmailException;
import org.loose.fis.av.exceptions.UserDoesNotExist;
import org.loose.fis.av.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.av.exceptions.UsernameAndPasswordDoNotMatchException;
import org.loose.fis.av.exceptions.InvalidEmailException;
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

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("Aplicatie-Vaccin.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }

    public static void addUser(String username, String password, String surname, String name, String code, String role) throws UsernameAlreadyExistsException,InvalidEmailException{
        checkUserDoesNotAlreadyExist(username);
        checkValidEmail(username);
        userRepository.insert(new User(username, encodePassword(username, password), surname, name, code, role));
    }
    public static boolean logIn(String username,String password) throws UserDoesNotExist,UsernameAndPasswordDoNotMatchException{
        boolean ok=false;
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                if (Objects.equals(encodePassword(username, password), user.getPassword()))

                    ok=true;
            }
        }
        checkUserDoesNotExist(username);
        checkUserAndPasswordDoNotMatch(username,password);
        return ok;
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
