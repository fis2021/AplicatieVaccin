package org.loose.fis.av.controllers;

import com.sun.scenario.effect.Blend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dizitart.no2.NitriteBuilder;
import org.loose.fis.av.controllers.LogInController;
import org.loose.fis.av.exceptions.NoAppointmentsException;
import org.loose.fis.av.model.ModelTable;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.User;
import org.loose.fis.av.services.SessionService;
import org.loose.fis.av.services.FileUnitateService;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.loose.fis.av.services.UserService;

import javax.validation.constraints.Null;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static org.loose.fis.av.services.FileSystemService.getPathToFile;

public class LoggedInPatientController {

    @FXML
    private TableView<ModelTable> table;
    @FXML
    private TableColumn<ModelTable,String> col_nume;
    @FXML
    private TableColumn<ModelTable,String> col_loc;
    @FXML
    public Text patientname;
    @FXML
    public Text Vgroup;
    @FXML
    public Text errormessage;

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        patientname.setText(SessionService.getLoggedInUser().getSurname() + " " + SessionService.getLoggedInUser().getName());
        if(UserService.grupvarsta() == 1){
            Vgroup.setText("I C");
        }
        else{
            if(UserService.grupvarsta() == 2){
                Vgroup.setText("II A");
            }
            else{
                if(UserService.grupvarsta() == 3){
                    Vgroup.setText("III P");
                }
            }
        }
        for(Unitate unitate2 : FileUnitateService.unitateRepository.find()){
            oblist.add(new ModelTable(unitate2.getNume(), String.valueOf(FileUnitateService.checkemptyplaces(unitate2.getCod_unit()))));
        }
        col_nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        col_loc.setCellValueFactory(new PropertyValueFactory<>("locuri"));

        table.setItems(oblist);
    }


    @FXML
    public void MakeAppointment(javafx.event.ActionEvent actionEvent) {
        Parent RegisterView = null;
        try {
            RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("MakeAppointment.fxml"));
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
    public void MyAppointments(javafx.event.ActionEvent actionEvent) throws NoAppointmentsException {
        try {
            UserService.checkAppointment();
            Parent RegisterView = null;
            try {
                RegisterView = FXMLLoader.load(getClass().getClassLoader().getResource("PatientMyAppointments.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene registerViewScene = new Scene(RegisterView);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(registerViewScene);
            window.show();
        } catch (NoAppointmentsException e) {
            errormessage.setText(e.getMessage());
        }
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
}
