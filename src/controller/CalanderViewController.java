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
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class CalanderViewController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Appointment> tempAppointmentList;
    
    @FXML
    private RadioButton displayByWeekRBtn;

    @FXML
    private ToggleGroup displayTG;

    @FXML
    private RadioButton displayByMonthRBtn;

    @FXML
    private RadioButton displayAllRbtn;

    @FXML
    private TextField initialDateTxt;

    @FXML
    private TableView<Appointment> calanderTableView;

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
    void onActionAllSelcted(ActionEvent event) {

    }

    @FXML
    void onActionDisplayData(ActionEvent event) throws SQLException, ParseException {
        // Displays appointments for a week or month based on an intial start date
        String initialDate;
        // Parsecheck to ensure date was entered correctly
        ZonedDateTime parseErrorCheck = ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        if (displayByWeekRBtn.isSelected()){
            //Display by week
            initialDate = initialDateTxt.getText();
            tempAppointmentList = DBInteractionAppointment.getWeekAppointments(initialDate);
            if (tempAppointmentList.isEmpty()){
                // Display messege if no appointments were found for that week
                displayConfirmationOkMessage("There are no appointments to display\n"
                        + "for the desired time frame.");
                calanderTableView.setItems(tempAppointmentList);
            }else if ((tempAppointmentList.size() == 1)
                    &&(tempAppointmentList.get(0).getStartZoned().equals(parseErrorCheck))){
                // Display errormessage if date format was incorrect
                displayWarningOkMessage("Date time format was incorrect.\n"
                    + "Please use format YYYY-MM-DD Ex. 2005-02-28");
                tempAppointmentList.clear();
                calanderTableView.setItems(tempAppointmentList);
            }else{
                // Displays weekly appointmentssorted by date and time
                tempAppointmentList = DBInteractionAppointment.sortAppointmentListByStart(tempAppointmentList);
                calanderTableView.setItems(tempAppointmentList);
            }
        }
        if (displayByMonthRBtn.isSelected()){
            // Display by month
            initialDate = initialDateTxt.getText();
            tempAppointmentList = DBInteractionAppointment.getMonthAppointments(initialDate);
            if (tempAppointmentList.isEmpty()){
                // Display messege if no appointments were found for that month
                displayConfirmationOkMessage("There are no appointments to display\n"
                        + "for the desired time frame.");
                calanderTableView.setItems(tempAppointmentList);
            }else if ((tempAppointmentList.size() == 1)
                    && (tempAppointmentList.get(0).getStartZoned().equals(parseErrorCheck))){
                // Display errormessage if date format was incorrect 
                displayWarningOkMessage("Date time format was incorrect.\n"
                    + "Please use format YYYY-MM-DD Ex. 2005-02-28");
                tempAppointmentList.clear();
                calanderTableView.setItems(tempAppointmentList);
            }else{
                // Displays monthly appointments sorted by date and time
                tempAppointmentList = DBInteractionAppointment.sortAppointmentListByStart(tempAppointmentList);
                calanderTableView.setItems(tempAppointmentList);
            }
        }
        if (displayAllRbtn.isSelected()){
            // Displays all appointments sorted by date and time
            tempAppointmentList = DBInteractionAppointment.sortAppointmentListByStart(DBInteractionAppointment.getAllAppointments());
            calanderTableView.setItems(tempAppointmentList);
        }
    }

    @FXML
    void onActionDisplayReportManager(ActionEvent event) throws IOException {
        sceneLoader("/view/ReportManager.fxml", event);
    }

    /**
     * Initializes the controller class.
     */
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
