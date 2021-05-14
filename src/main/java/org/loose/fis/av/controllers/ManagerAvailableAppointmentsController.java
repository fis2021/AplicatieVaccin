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
import org.loose.fis.av.exceptions.InvalidAvailableAppointments;
import org.loose.fis.av.exceptions.MustBeNumberException;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.User;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.SendEmailService;
import org.loose.fis.av.services.SessionServiceUnitate;
import org.loose.fis.av.services.UserService;

import java.io.IOException;
import java.util.Objects;

public class ManagerAvailableAppointmentsController {
    @FXML
    TextField nrprog;
    @FXML
    Text successMessage;

    @FXML
    public void modifyNumber()
    {
        try{
            FileUnitateService.isNumber(nrprog.getText());
            FileUnitateService.modifyAppointmentNumber(Integer.parseInt(nrprog.getText()));
            successMessage.setText("Numarul de locuri a fost modificat cu succes!");
        }catch (InvalidAvailableAppointments e)
        {
            successMessage.setText(e.getMessage());
        }
        catch (MustBeNumberException e)
        {
            successMessage.setText(e.getMessage());
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
