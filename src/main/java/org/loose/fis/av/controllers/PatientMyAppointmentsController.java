package org.loose.fis.av.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.SessionService;


import java.io.IOException;
import java.util.Objects;

public class PatientMyAppointmentsController {

    @FXML
    public Text nume;

    @FXML
    public Text CNP;

    @FXML
    public Text unitateP;

    @FXML
    public Text data;

    @FXML
    public void initialize(){
        nume.setText(SessionService.getLoggedInUser().getSurname() + " " + SessionService.getLoggedInUser().getName());
        CNP.setText(SessionService.getLoggedInUser().getCode());
        for(Unitate unitate : FileUnitateService.unitateRepository.find()){
            for(int i = 0; i < unitate.getContor(); i++){
                if(Objects.equals(unitate.getProgramat(i).getCNP(),SessionService.getLoggedInUser().getCode())){
                    unitateP.setText(unitate.getNume());
                    data.setText(unitate.getProgramat(i).getData());
                }
            }
        }
    }


    @FXML
    public void returnHome(javafx.event.ActionEvent actionEvent) {
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
