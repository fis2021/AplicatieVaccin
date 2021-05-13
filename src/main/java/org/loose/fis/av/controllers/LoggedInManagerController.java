package org.loose.fis.av.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.loose.fis.av.model.ModelTable;
import org.loose.fis.av.model.ModelTableManager;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.Programat;
import org.loose.fis.av.services.FileUnitateService;
import org.loose.fis.av.services.SessionServiceUnitate;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.Objects;

public class LoggedInManagerController {
    @FXML
    public Text numeunit;
    @FXML
    public Text codunit;
    @FXML
    public Text nrlocuri;
    @FXML
    public TableView<ModelTableManager> table;
    @FXML
    public TableColumn<ModelTableManager,String> col_nume_pacient;
    @FXML
    public TableColumn<ModelTableManager,String> col_data;

    ObservableList<ModelTableManager> oblist = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        numeunit.setText(SessionServiceUnitate.getLoggedInUnitate().getNume());
        codunit.setText(SessionServiceUnitate.getLoggedInUnitate().getCod_unit());
        for (Unitate unitate : FileUnitateService.unitateRepository.find()) {
            if (Objects.equals(unitate.getCod_unit(), SessionServiceUnitate.getLoggedInUnitate().getCod_unit()))
            {
                nrlocuri.setText(String.valueOf(unitate.getLocuri()-unitate.getContor()));
            }
        }

        for (Unitate unitate : FileUnitateService.unitateRepository.find()){
            if(Objects.equals(unitate.getCod_unit(),SessionServiceUnitate.getLoggedInUnitate().getCod_unit()))
            {
                for(int j=0;j<SessionServiceUnitate.LoggedInUnitate.getContor();j++)
                {
                    if(unitate.getProgramat(j) != null) {
                        oblist.add(new ModelTableManager(unitate.getProgramat(j).getNume(), unitate.getProgramat(j).getData()));
                    }
                }
            }
        }
        col_nume_pacient.setCellValueFactory(new PropertyValueFactory<>("nume"));
        col_data.setCellValueFactory(new PropertyValueFactory<>("data"));
        table.setItems(oblist);

    }

    @FXML
    public void LogOut(javafx.event.ActionEvent actionEvent) {
        Parent RegisterView = null;
        try {
            RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene registerViewScene = new Scene(RegisterView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();

    }
    @FXML
    public void deleteAppointmentScene(javafx.event.ActionEvent actionEvent) {
        Parent RegisterView = null;
        try {
            RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("ManagerDeleteAppointment.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene registerViewScene = new Scene(RegisterView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();

    }
    @FXML
    public void changeModifyNumberAppointmentsScene(javafx.event.ActionEvent actionEvent) {
        Parent AppNumberView = null;
        try {
            AppNumberView = FXMLLoader.load(getClass().getClassLoader().getResource("ModifyAppointmentsNumber.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene registerViewScene = new Scene(AppNumberView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();

    }
    @FXML
    public void rescheduleAppointmentScene(javafx.event.ActionEvent actionEvent) {
        Parent RescheduleView = null;
        try {
            RescheduleView = FXMLLoader.load(getClass().getClassLoader().getResource("ManagerReschedule.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene registerViewScene = new Scene(RescheduleView);

        //This line gets the Stage information
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();

    }

}
