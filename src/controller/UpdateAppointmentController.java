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
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class UpdateAppointmentController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Appointment> tempAppointmentList;
    
    private Appointment tempAppointment;

   @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private RadioButton searchCustomerNameRBtn;

    @FXML
    private RadioButton searchTitleRBtn;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField contactTxt;
    
    @FXML
    private TextField url;

    @FXML
    private TextField typeTxt;

    @FXML
    private TextField endTxt;

    @FXML
    private TextField startTxt;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<Appointment> updateTableView;

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
    private TableColumn<Appointment, String> endCol1;

    @FXML
    void onActionDisplayAppointmentManager(ActionEvent event) throws IOException {
        // Confirms exit and returns to Appointment manager
        Optional<ButtonType> result = displayConfirmationYesNoMessage("This will"
                + " clear all values and return to CustomerManager.\n Continue?");
        if (result.isPresent() && result.get()== ButtonType.OK)
            sceneLoader("/view/AppointmentManager.fxml", event);
    }

    @FXML
    void onActionSaveAppointment(ActionEvent event) throws SQLException, ParseException {
        // Saves updated to database
        // To ensure update can't be done without selection from table
        if(tempAppointment.getAppointmentId() > -10000)
        {
            Appointment changedAppointment = new Appointment();
            tempAppointmentList.remove(tempAppointment);
            // Retrieves updated data from text fields   
            changedAppointment.setCustomerId(tempAppointment.getCustomerId());
            changedAppointment.setAppointmentId(tempAppointment.getAppointmentId());
            changedAppointment.setUserId(tempAppointment.getUserId());
            changedAppointment.setCustomerName(customerIdTxt.getText());
            changedAppointment.setTitle(titleTxt.getText());
            changedAppointment.setDescription(descriptionTxt.getText()); 
            changedAppointment.setLocation(locationTxt.getText());
            changedAppointment.setContact(contactTxt.getText());
            changedAppointment.setType(typeTxt.getText());
            changedAppointment.setUrl(url.getText());
            changedAppointment.setStart(startTxt.getText());
            changedAppointment.setEnd(endTxt.getText());
            //Checks to make sure dates were correctly parsed
            ZonedDateTime parseErrorCheck = ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
            if ((changedAppointment.getStartZoned().equals(parseErrorCheck))
                || (changedAppointment.getEndZoned().equals(parseErrorCheck))){
            displayWarningOkMessage("Date time format was incorrect.\n"
                    + "Please use format YYYY-MM-DD HH:MM\n"
                    + "Ex. 2005-02-28 14:25");
            }else if(changedAppointment.getTitle().isEmpty() || 
                    changedAppointment.getTitle().length()>255
                    || changedAppointment.getDescription().isEmpty()
                    || changedAppointment.getLocation().isEmpty()
                    || changedAppointment.getContact().isEmpty() 
                    || changedAppointment.getType().isEmpty()
                    || changedAppointment.getUrl().isEmpty()){
                    // Warning message for invalid entry
                    displayWarningOkMessage("Please Enter a valid value for each field.");
            }else{
                // Checks database for conflicting appointment returns conflicting appointment if found  
                Appointment conflicting = DBInteractionAppointment.updateAppointmentInDataBase(tempAppointment, changedAppointment);
                // Displays Error message for Outside buiness hours ********************
                if(conflicting.getAppointmentId() == -9999){
                    displayWarningOkMessage("The appointment must take place during business hours.\n"
                        + "Business Hours: 07:00-20:00");
                // Displays approperate Error message if appointment could not be add to Database
                // Otherwise confirms appointment has been added
                }else if(conflicting.getAppointmentId() == -10001){
                    displayWarningOkMessage("The Start Time cannot come before the End Time.");
                }else if(conflicting.getAppointmentId() == -10002){
                    displayWarningOkMessage("Date time format was incorrect.\n"
                            + "Please use format YYYY-MM-DD HH:MM\n"
                            + "Ex. 2005-02-28 14:25");
                }else if (conflicting.getAppointmentId() == -10003){
                    displayWarningOkMessage("Conflicting Appointment With " 
                    + conflicting.getCustomerName() + "\n Starts: "
                    + conflicting.getStart() + " Ends: "
                    + conflicting.getEnd()+"\n Title: "
                    + conflicting.getTitle());
                }else if (conflicting.getAppointmentId() == -10004){
                    displayWarningOkMessage("Customer not in Database.");
                }else{
                    // Clears table and varibles for new search
                    tempAppointmentList.clear();
                    updateTableView.setItems(tempAppointmentList);
                    tempAppointment = new Appointment();
                    tempAppointment.setAppointmentId(-10005);
                    // Clears Text fields for next appointment to update
                    customerIdTxt.clear();
                    titleTxt.clear();
                    descriptionTxt.clear();
                    locationTxt.clear();
                    contactTxt.clear();
                    typeTxt.clear();
                    url.clear();
                    startTxt.clear();
                    endTxt.clear();
                    displayConfirmationOkMessage("Appointment has been updated in database.");
                }
            }
        }
    }

    @FXML
    void onActionSearchCustomers(ActionEvent event) throws SQLException, ParseException {
        // Searches customers by name or title can do partial searches
        String searchString = searchTxt.getText();
        if (!searchString.isEmpty()){
            if (searchCustomerNameRBtn.isSelected())
                tempAppointmentList = DBInteractionAppointment.getAppointmentFromNameSearch(searchString);
            else
                tempAppointmentList = DBInteractionAppointment.getAppointmentFromTitleSearch(searchString);
            updateTableView.setItems(tempAppointmentList);
        }
    }

    @FXML
    void onActionSelectCustomerFromTable(ActionEvent event) {
        // Transfers selected customer data to editible fields for updating
        try{
            tempAppointment = updateTableView.getSelectionModel().getSelectedItem();
            customerIdTxt.setText(String.valueOf(tempAppointment.getCustomerName()));
            titleTxt.setText(String.valueOf(tempAppointment.getTitle()));
            descriptionTxt.setText(String.valueOf(tempAppointment.getDescription()));
            locationTxt.setText(String.valueOf(tempAppointment.getLocation()));
            contactTxt.setText(String.valueOf(tempAppointment.getContact()));
            typeTxt.setText(String.valueOf(tempAppointment.getType()));
            url.setText(String.valueOf(tempAppointment.getUrl()));
            startTxt.setText(String.valueOf(tempAppointment.getStart()));
            endTxt.setText(String.valueOf(tempAppointment.getEnd()));
            
        }catch(Exception e){
            // Error to select a appointment to update
            displayWarningOkMessage("Please select a customer to update.");
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // To ensure update can't be done without selection from table
        tempAppointment = new Appointment();
        tempAppointment.setAppointmentId(-10005);
        // Initializes table
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));        
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));        
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));        
        endCol1.setCellValueFactory(new PropertyValueFactory<>("end"));
    }
}
