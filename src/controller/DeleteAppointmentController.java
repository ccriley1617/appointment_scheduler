/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DataBaseObj.Appointment;
import DataBaseObj.DBInteractionAppointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class DeleteAppointmentController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Appointment> tempAppointmentList;
    
    @FXML
    private RadioButton searchCustomerNameRBtn;

    @FXML
    private TextField searchTxt;
    
    @FXML
    private TableView<Appointment> deleteTableView;

    @FXML
    private TableColumn<Appointment, String> customerNameCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> urlCol;

    @FXML
    private TableColumn<Appointment, String> startCol;

    @FXML
    private TableColumn<Appointment, String> endCol;
    @FXML
    private ToggleGroup searchTG;
    @FXML
    private RadioButton searchTitleRBtn;

    @FXML
    void onActionDeleteAppointment(ActionEvent event) {
        // Deletes appointment in database and updates table
        Optional<ButtonType> result = displayConfirmationYesNoMessage(
            "This will delete the selected appointment from database\nContinue?");
        if (result.isPresent() && result.get()== ButtonType.OK){
            try{
                Appointment tempAppointment = deleteTableView.getSelectionModel().getSelectedItem();
                DBInteractionAppointment.deleteAppointmentFromDataBase(tempAppointment);
                tempAppointmentList.remove(tempAppointment);
                deleteTableView.setItems(tempAppointmentList);
            }catch (Exception e){
                // Error message to select a appointment to delete
                displayWarningOkMessage("Please select an appointment to delete.");
            }
        }
    }

    @FXML
    void onActionDisplayAppointmentManager(ActionEvent event) throws IOException {
        sceneLoader("/view/AppointmentManager.fxml", event);
    }

    @FXML
    void onActionSearchAppointments(ActionEvent event) throws SQLException, ParseException {
        // Searches database on customer name or appointment title
        String searchString = searchTxt.getText();
        if (!searchString.isEmpty()){
            if (searchCustomerNameRBtn.isSelected())
                tempAppointmentList = DBInteractionAppointment.getAppointmentFromNameSearch(searchString);
            else
                tempAppointmentList = DBInteractionAppointment.getAppointmentFromTitleSearch(searchString);
            deleteTableView.setItems(tempAppointmentList);
        }
    }
    
    // * Initializes the controller class.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializes table
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));        
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));        
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));        
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
    } 
}
