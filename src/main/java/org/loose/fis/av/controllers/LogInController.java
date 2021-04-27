package org.loose.fis.av.controllers;

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
import org.loose.fis.av.exceptions.UsernameAlreadyExistsException;
import org.loose.fis.av.exceptions.UsernameAndPasswordDoNotMatchException;
import org.loose.fis.av.exceptions.UserDoesNotExist;
import org.loose.fis.av.services.UserService;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class LogInController {
    @FXML
    private Text loginMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    public void handleLogInAction() {
        try {
            UserService.logIn(usernameField.getText(),passwordField.getText());
                loginMessage.setText("Logged in!");
        }
        catch (UsernameAndPasswordDoNotMatchException e) {
            loginMessage.setText(e.getMessage());
        }
        catch (UserDoesNotExist e){
            loginMessage.setText(e.getMessage());
        }
    }
    @FXML
    public void changeToRegister(javafx.event.ActionEvent actionEvent) {
        Parent RegisterView = null;
        try {
            RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
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
