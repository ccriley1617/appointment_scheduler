/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DataBaseObj.Appointment;
import DataBaseObj.Customer;
import DataBaseObj.DBInteractionAppointment;
import DataBaseObj.DBInteractionCustomer;
import DataBaseObj.LogManData;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class AddAppointmentController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Customer> tempCustomerList;
    
    private Customer tempCustomer;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField contactTxt;

    @FXML
    private TextField typeTxt;
    
    @FXML
    private TextField urlTxt;

    @FXML
    private TextField endTxt;

    @FXML
    private TextField startTxt;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<Customer> addAppointmentTableView;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, String> address2Col;

    @FXML
    private TableColumn<Customer, String> cityCol;

    @FXML
    private TableColumn<Customer, String> countryCol;

    @FXML
    private TableColumn<Customer, String> postalCodeCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, Integer> activeCol;
    
    @FXML
    void onActionDisplayAppointmentManager(ActionEvent event) throws IOException {
        // Gets conformation and returns to Appointment manager
        Optional<ButtonType> result = displayConfirmationYesNoMessage("This will"
                + " clear all values and return to CustomerManager.\n Continue?");
        if (result.isPresent() && result.get()== ButtonType.OK)
            sceneLoader("/view/AppointmentManager.fxml", event);
    }

    @FXML
    void onActionSaveAppointment(ActionEvent event) throws SQLException, ParseException {
        // Adds appointment to database
        Appointment appointment = new Appointment();
        try{
            //Gets data from use and adds to appointment object
            appointment.setCustomerId(tempCustomer.getCustomerId());
            appointment.setUserId(LogManData.getUserIdLoggedIn());
            appointment.setTitle(titleTxt.getText());
            appointment.setDescription(descriptionTxt.getText());
            appointment.setLocation(locationTxt.getText());
            appointment.setContact(contactTxt.getText());
            appointment.setType(typeTxt.getText());
            appointment.setUrl(urlTxt.getText());
            appointment.setStart(startTxt.getText());
            appointment.setEnd(endTxt.getText());
        }catch (Exception e){
            //Error for incorrect data entry
            displayWarningOkMessage("Please enter valid values for each field."
                    + "Error: " + e.getMessage());
        }
        // Checks dates were parsed correctly
        ZonedDateTime parseErrorCheck = ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        if ((appointment.getStartZoned().equals(parseErrorCheck))
                || (appointment.getEndZoned().equals(parseErrorCheck))){
            displayWarningOkMessage("Date time format was incorrect.\n"
                    + "Please use format YYYY-MM-DD HH:MM\n"
                    + "Ex. 2005-02-28 14:25");
        }else if(appointment.getTitle().isEmpty() || 
                    appointment.getTitle().length()>255
                    || appointment.getDescription().isEmpty()
                    || appointment.getLocation().isEmpty()
                    || appointment.getContact().isEmpty() 
                    || appointment.getType().isEmpty()
                    || appointment.getUrl().isEmpty()){
                    // Warning message for invalid entry
                    displayWarningOkMessage("Please Enter a valid value for each field.");
        }else{
            // Checks database for conflicting appointment returns conflicting appointment if found    
            Appointment conflicting = DBInteractionAppointment.addToAppointmentTableGetPrimaryKey(appointment);
            // Displays Error message for Outside buiness hours ********************
            if(conflicting.getAppointmentId() == -9999){
                displayWarningOkMessage("The appointment must take place during business hours.\n"
                    + "Business Hours: 07:00-20:00");
            // Displays approperate Error message if appointment could not be add to Database
            // Otherwise confirms appointment has been added
            }else if(conflicting.getAppointmentId() == -10001){
                displayWarningOkMessage("The Start Time cannot be equal to or greater than the End Time.");
            }else if (conflicting.getAppointmentId() == -10003){
                displayWarningOkMessage("Conflicting Appointment With " 
                + conflicting.getCustomerName() + "\n Starts: "
                + conflicting.getStart() + " Ends: "
                + conflicting.getEnd()+"\n Title: "
                + conflicting.getTitle());
            }else{
                displayConfirmationOkMessage("Appointment has been saved to database.");
                // Adds the appointments fetched primary key and corisondings customer's name
                appointment.setAppointmentId(conflicting.getAppointmentId());
                appointment.setCustomerName(conflicting.getCustomerName());
                // Clears Fields for next appointment to be added
                customerNameTxt.clear();    
                titleTxt.clear();
                descriptionTxt.clear();
                locationTxt.clear();
                contactTxt.clear();
                typeTxt.clear();
                urlTxt.clear();
                endTxt.clear();
                startTxt.clear();
                searchTxt.clear();
            }  
        }
    }

    @FXML
    void onActionSearchCustomers(ActionEvent event) throws SQLException {
        // Searches customer based on name can do partial searches
        String searchString = searchTxt.getText();
        if (!searchString.isEmpty()){
            tempCustomerList = DBInteractionCustomer.getCustomerFromNameSearch(searchString);
            
            addAppointmentTableView.setItems(tempCustomerList);
        }
    }

    @FXML
    void onActionSelectCustomerFromTable(ActionEvent event) {
        // Grabs selected customer from table and puts in text field for appointment creation
         try{
            tempCustomer = addAppointmentTableView.getSelectionModel().getSelectedItem();
            customerNameTxt.setText(String.valueOf(tempCustomer.getCustomerName()));
        }catch(Exception e){
            // Error messege to select a customer for appointment creation
            displayWarningOkMessage("Please select a customer to update.");
        }
    }   
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializes table
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
    } 
}
