package org.loose.fis.av.services;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    public static boolean checkCNP(String code){
        for(int i = 0; i < 13; i++){
            if(!Character.isDigit(code.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean checkUnit(String code){
        for(Unitate unitate : FileUnitateService.unitateRepository.find()){
            if(Objects.equals(code,unitate.getCod_unit())){
                return true;
            }
        }
        return false;
    }

    private static void checkValidCode (String code, String role) throws InvalidCodeException {
        if ((code.length() != 13 && Objects.equals("Patient", role)) || ((checkCNP(code) == false) && Objects.equals("Patient", role))) {
            throw new InvalidCodeException(code);
        }
        else {
            if((code.length() != 3 && Objects.equals("Manager",role)) || ((checkUnit(code) == false) && Objects.equals("Manager",role))){
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

    public static void chechemptypassword(PasswordField field) throws EmptyFieldException{
        if(field.getText().isEmpty()){
            throw new EmptyFieldException();
        }
    }

    public static void chechemptyfield(TextField field) throws EmptyFieldException{
        if(field.getText().isEmpty()){
            throw new EmptyFieldException();
        }
    }

    public static void chechemptychoicebox(ChoiceBox field) throws EmptyFieldException{
        if(field.getValue() == null){
            throw new EmptyFieldException();
        }
    }

    public static int grupvarsta(){
        int temp = 0;
        int sum = 0;
        int grp = 0;
        temp = Character.getNumericValue(SessionService.getLoggedInUser().getCode().charAt(5)) * 10 + Character.getNumericValue(SessionService.getLoggedInUser().getCode().charAt(6));
        if(temp <= 21){
            sum = 21 - temp;
            if(sum < 18){
                grp = 1;
            }
            else{
                if(sum >= 18 && sum < 21){
                    grp = 2;
                }
            }
        }
        else{
            sum = 121 - temp;
            if(sum < 65){
                grp = 2;
            }
            else{
                grp = 3;
            }
        }
        return grp;
    }

}
