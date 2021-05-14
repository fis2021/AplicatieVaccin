package org.loose.fis.av.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.loose.fis.av.exceptions.EmptyFieldException;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.User;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.SendEmailService;
import org.loose.fis.av.services.SessionServiceUnitate;
import org.loose.fis.av.services.UserService;

import java.io.IOException;
import java.util.Objects;

public class ManagerDeleteAppointmentController {
    @FXML
    public Text deletemessage;
    @FXML
    public ChoiceBox pacient;
    @FXML
    public TextField delmessage;
    public void initialize(){
        for(Unitate unitate: FileUnitateService.unitateRepository.find()){
            if(Objects.equals(unitate.getCod_unit(), SessionServiceUnitate.getLoggedInUnitate().getCod_unit()))
            {
                for(int i=0; i< unitate.getContor(); i++)
                {
                    pacient.getItems().addAll(unitate.getProgramat(i).getNume());
                }
            }
        }
    }
    @FXML
    public void deleteAppointment(){
        try {
            UserService.chechemptychoicebox(pacient);
            UserService.chechemptyfield(delmessage);
            for (User user : UserService.userRepository.find())
            {
                if(Objects.equals(pacient.getValue(),user.getSurname()+ " " +user.getName()))
                {
                    SendEmailService.TrimiteMesaj(user.getUsername(), delmessage.getText(),"Stergere Programare");
                    FileUnitateService.deleteAppointmentManager(user.getCode());
                    deletemessage.setText("Programarea a fost stearsa!");

                }
            }
        }catch (EmptyFieldException e){
            deletemessage.setText(e.getMessage());
        }


    }
    @FXML
    public void returnHome(javafx.event.ActionEvent actionEvent) {
        Parent homeView = null;
        try {
            homeView = FXMLLoader.load(getClass().getClassLoader().getResource("LoggedInManager.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene homeViewScene = new Scene(homeView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(homeViewScene);
        window.show();

    }

}
