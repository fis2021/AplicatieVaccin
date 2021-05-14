package org.loose.fis.av.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.loose.fis.av.exceptions.EmptyFieldException;
import org.loose.fis.av.exceptions.InvalidCodeException;
import org.loose.fis.av.exceptions.InvalidEmailException;
import org.loose.fis.av.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.av.services.UserService;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    private ChoiceBox role;

    @FXML
    public void initialize() {
        role.getItems().addAll("Patient", "Manager");
    }

    @FXML
    public void handleRegisterAction() {
        try {
            UserService.chechemptyfield(usernameField);
            UserService.chechemptypassword(passwordField);
            UserService.chechemptyfield(surnameField);
            UserService.chechemptyfield(nameField);
            UserService.chechemptyfield(codeField);
            UserService.chechemptychoicebox(role);
            UserService.addUser(usernameField.getText(), passwordField.getText(),surnameField.getText(),nameField.getText(),codeField.getText(),(String) role.getValue());
            registrationMessage.setText("Account created successfully!");
        }
        catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        }
        catch (InvalidEmailException e) {
            registrationMessage.setText(e.getMessage());
        }
        catch (InvalidCodeException e) {
            registrationMessage.setText(e.getMessage());
        }
        catch (EmptyFieldException e){
            registrationMessage.setText(e.getMessage());
        }
    }

    public void changeToLogIn(ActionEvent actionEvent) {
        Parent LogInView = null;
        try {
            LogInView = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene logInViewScene = new Scene(LogInView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(logInViewScene);
        window.show();
    }
}
