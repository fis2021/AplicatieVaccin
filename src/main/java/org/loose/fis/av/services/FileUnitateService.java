package org.loose.fis.av.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.av.exceptions.AppointmentAlreadyExists;
import org.loose.fis.av.exceptions.InvalidAvailableAppointments;
import org.loose.fis.av.exceptions.InvalidDateException;
import org.loose.fis.av.exceptions.MustBeNumberException;
import org.loose.fis.av.model.Programat;
import org.loose.fis.av.model.Unitate;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class FileUnitateService {
    private static String APPLICATION_FOLDER_UNITATE = ".aplicatie-vaccin-unitate";
    private static final String USER_FOLDER_UNITATE = System.getProperty("user.home");
    private static Nitrite database;

    public static void setApplicationFolderUnitate(String applicationFolderUnitate){
        APPLICATION_FOLDER_UNITATE = applicationFolderUnitate;
    }

    public static Path getApplicationHomePath(){
        return Paths.get(USER_FOLDER_UNITATE, APPLICATION_FOLDER_UNITATE);
    }

    public static Path getPathToUnitate(String... path) {
        return getApplicationHomePath().resolve(Paths.get(".", path));
    }

    public static ObjectRepository<Unitate> unitateRepository;

    public static void initDatabaseUnit() {
        database = Nitrite.builder()
                .filePath(getPathToUnitate("Aplicatie-Vaccin-Unitate.db").toFile())
                .openOrCreate("test", "test");

        unitateRepository = database.getRepository(Unitate.class);
    }

    public static void closeDatabase() {
        database.close();
    }

    public static void addUnits(){
        unitateRepository.insert(new Unitate("Spitalul Judetean","Timisoara","TM1"));
        unitateRepository.insert(new Unitate("Spitalul Militar","Timisoara","TM2"));
        unitateRepository.insert(new Unitate("Spitalul CFR","Timisoara","TM3"));
    }

    public static int checkemptyplaces(String cod){
        for(Unitate unitate : unitateRepository.find()) {
            if(Objects.equals(cod,unitate.getCod_unit())) {
                return unitate.getLocuri() - unitate.getContor();
            }
        }
        return 0;
    }

    public static void addAppointment(String nume,String data) throws InvalidDateException, AppointmentAlreadyExists{
        checkValidDate(data);
        checkAlreadyAppointment();
        for(Unitate unitate : unitateRepository.find()){
            if(Objects.equals(nume,unitate.getNume())){
                Programat prog = new Programat(SessionService.getLoggedInUser().getSurname() + " " + SessionService.getLoggedInUser().getName() , data , SessionService.getLoggedInUser().getCode());
                unitate.addProgramat(prog);
                unitateRepository.update(unitate);
            }
        }
    }

    public static void checkValidDate(String data) throws InvalidDateException {
        int temp1;
        Character temp2;
        int temp3 ;
        Character temp4;
        int temp5 ;
        Character slash = '/';

        temp1 = Character.getNumericValue(data.charAt(0)) * 10 + Character.getNumericValue(data.charAt(1));
        temp2 = data.charAt(2);
        temp3 = Character.getNumericValue(data.charAt(3)) * 10 + Character.getNumericValue(data.charAt(4));
        temp4 = data.charAt(5);
        temp5 = Character.getNumericValue(data.charAt(6)) * 1000 + Character.getNumericValue(data.charAt(7)) * 100 + Character.getNumericValue(data.charAt(8)) * 10 + Character.getNumericValue(data.charAt(9));


        if(temp1 > 31 || temp1 < 1){
            throw new InvalidDateException(data);
        }

        if(!temp2.equals(slash)){
            throw new InvalidDateException(data);
        }

        if(temp3 > 12 || temp3 < 1){
            throw new InvalidDateException(data);
        }

        if(!temp4.equals(slash)){
            throw new InvalidDateException(data);
        }

        if(temp5 > 3000 || temp5 < 2021){
            throw new InvalidDateException(data);
        }

    }

    public static void checkAlreadyAppointment() throws AppointmentAlreadyExists {
        for(Unitate unitate : unitateRepository.find()){
            for(int j = 0 ; j < unitate.getContor(); j++){
                if(unitate.getProgramat(j) != null){
                    if(Objects.equals(unitate.getProgramat(j).getCNP(),SessionService.getLoggedInUser().getCode())){
                        throw new AppointmentAlreadyExists();
                    }
                }
            }
        }
    }

    public static void deleteAppointment() {
        for(Unitate unitate : unitateRepository.find()){
            for(int i = 0; i < unitate.getContor(); i++){
                if(Objects.equals(unitate.getProgramat(i).getCNP(),SessionService.getLoggedInUser().getCode())){
                    unitate.deleteProgamat(i);
                    unitateRepository.update(unitate);
                }
            }
        }
    }
    public static void deleteAppointmentManager(String cnp) {
        for(Unitate unitate : unitateRepository.find()){
            for(int i = 0; i < unitate.getContor(); i++){
                if(Objects.equals(unitate.getProgramat(i).getCNP(),cnp)){
                    unitate.deleteProgamat(i);
                    unitateRepository.update(unitate);
                }
            }
        }
    }
    public static void modifyAppointmentNumber(int nr) throws InvalidAvailableAppointments
    {
        for(Unitate unitate: unitateRepository.find())
        {
            if(Objects.equals(unitate.getCod_unit(),SessionServiceUnitate.getLoggedInUnitate().getCod_unit()))
            {
                if(nr<unitate.getContor())
                {
                    throw new InvalidAvailableAppointments();
                }
                unitate.setLocuri(nr);
                unitateRepository.update(unitate);
            }
        }
    }
    public static void rescheduleAppointmentManager(String cnp,String data) throws InvalidDateException{
        checkValidDate(data);
        for(Unitate unitate : unitateRepository.find()){
            for(int i = 0; i < unitate.getContor(); i++){
                if(Objects.equals(unitate.getProgramat(i).getCNP(),cnp)){
                    unitate.getProgramat(i).setData(data);
                    unitateRepository.update(unitate);
                }
            }
        }
    }
    public static void isNumber(String number) throws MustBeNumberException
    {
        for(int i=0;i<number.length();i++)
        {
            if(!Character.isDigit(number.charAt(i)))
            {
                throw new MustBeNumberException();
            }
        }
    }


}
