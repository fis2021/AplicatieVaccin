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
import org.loose.fis.av.exceptions.AppointmentAlreadyExists;
import org.loose.fis.av.exceptions.InvalidDateException;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.services.FileUnitateService;

import java.io.IOException;

public class MakeAppointmentController {

    @FXML
    public Text appointmentmessage;

    @FXML
    public ChoiceBox UnitateV;

    @FXML
    public TextField data;

    public void initialize(){
        for(Unitate unitate : FileUnitateService.unitateRepository.find()){
            UnitateV.getItems().addAll(unitate.getNume());
        }
    }
    @FXML
    public void MakeAppointment(){
        try{
            FileUnitateService.addAppointment((String) UnitateV.getValue(),data.getText());
            appointmentmessage.setText("Programarea a fost facuta cu success");
        }
        catch (InvalidDateException e) {
            appointmentmessage.setText(e.getMessage());
        }
        catch (AppointmentAlreadyExists e) {
            appointmentmessage.setText(e.getMessage());
        }


    }
    @FXML
    public void LogOut(javafx.event.ActionEvent actionEvent) {
        Parent RegisterView = null;
        try {
            RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("LoggedInPatient.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene registerViewScene = new Scene(RegisterView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();

    }
}
