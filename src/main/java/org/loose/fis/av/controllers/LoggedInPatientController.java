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
import org.loose.fis.av.model.ModelTable;
import org.loose.fis.av.model.Unitate;
import org.loose.fis.av.model.User;
import org.loose.fis.av.services.SessionService;
import org.loose.fis.av.services.FileUnitateService;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.validation.constraints.Null;
import java.awt.*;
import java.io.IOException;

import static org.loose.fis.av.services.FileSystemService.getPathToFile;

public class LoggedInPatientController {

    public int grupvarsta(){
        int temp = 0;
        int sum = 0;
        int grp = 0;
        temp = Character.getNumericValue(SessionService.getLoggedInUser().getCode().charAt(5)) * 10 + Character.getNumericValue(SessionService.getLoggedInUser().getCode().charAt(6));
        System.out.println(temp);
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

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        patientname.setText(SessionService.getLoggedInUser().getSurname() + " " + SessionService.getLoggedInUser().getName());
        if(grupvarsta() == 1){
            Vgroup.setText("I C");
        }
        else{
            if(grupvarsta() == 2){
                Vgroup.setText("II A");
            }
            else{
                if(grupvarsta() == 3){
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
