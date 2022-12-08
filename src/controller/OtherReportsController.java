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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class OtherReportsController extends JavaFXGeneralController implements Initializable {

    @FXML
    private RadioButton displayNumOfAppByMonRBtn;


    @FXML
    private RadioButton displayScheduleConsultantRBtn;

    @FXML
    private RadioButton displayMailingListRbtn;
    
    @FXML
    private TextArea reportTxt;


    @FXML
    void onActionDisplayReportManager(ActionEvent event) throws IOException {
        sceneLoader("/view/ReportManager.fxml", event);
    }

    @FXML
    void onActionGenerateReport(ActionEvent event) throws SQLException, ParseException {
        // Generates reports based on which radio button is selected
        ObservableList<Appointment> appointmentList;
        ObservableList<Customer> customerList;
        int[] monthAppointmentCount = new int[12];
        if(displayNumOfAppByMonRBtn.isSelected()){
            // Displays number of appointments per month
            reportTxt.clear();
            //Retrives number from database
            monthAppointmentCount = DBInteractionAppointment.getNumberOfAppointmentsByMonth();
            //Writes to text area
            reportTxt.setText("January:  " + monthAppointmentCount[0] + "\n" +
                              "Febuary:  " + monthAppointmentCount[1] + "\n" +
                              "March:    " + monthAppointmentCount[2] + "\n" +
                              "April:    " + monthAppointmentCount[3] + "\n" +
                              "May:      " + monthAppointmentCount[4] + "\n" +
                              "June:     " + monthAppointmentCount[5] + "\n" +
                              "July:     " + monthAppointmentCount[6] + "\n" +
                              "August:   " + monthAppointmentCount[7] + "\n" +
                              "September:" + monthAppointmentCount[8] + "\n" +
                              "October:  " + monthAppointmentCount[9] + "\n" +
                              "November: " + monthAppointmentCount[10] + "\n" +
                              "December: " + monthAppointmentCount[11]);
        }
        else if (displayScheduleConsultantRBtn.isSelected()){
            // Displays each users appointments ordered by date and time
            // Retrieves userIds from database
            Stack<Integer> userIdStack = DBInteractionAppointment.getUserIds();
            int userId;
            reportTxt.clear();
            while(!userIdStack.isEmpty()){
                // user Ids are used to fetch corrisponding appointments
                userId = userIdStack.pop();
                System.out.println(userId);
                appointmentList = DBInteractionAppointment.getAppointmentsByUserId(userId);
                // Displays Header user names appointments:
                reportTxt.appendText("The appointments for User: " + DBInteractionAppointment.geUserNameByUserId(userId) + "\n\n\n");
                // Displays each appointment
                for (int x = 0; x < appointmentList.size(); x++){
                    reportTxt.appendText("Title: " +appointmentList.get(x).getTitle() + "    " +
                            "Description: " + appointmentList.get(x).getDescription() + "    " +
                            "Location: " + appointmentList.get(x).getLocation() + "    " +
                            "Contact: " + appointmentList.get(x).getContact() + "    " +
                            "Start: " + appointmentList.get(x).getStart() + "    " +
                            "End: " + appointmentList.get(x).getEnd() + "\n\n");
                }
                reportTxt.appendText("\n\n\n\n");
            } 
        }else{
            // Displays mailing list
            reportTxt.clear();
            // Retrives mailing list from data base
            customerList = DBInteractionCustomer.getCustomerMailinglist();
            // Displays header
            reportTxt.setText("                     MAILING LIST \n\n\n");
            // Displays all active customers address
            for (int x = 0; x < customerList.size(); x++){
                reportTxt.appendText(customerList.get(x).getCustomerName() + " "
                    + customerList.get(x).getAddress() + " "
                    + customerList.get(x).getAddress2() + " "
                    + customerList.get(x).getCity() + " "
                    + customerList.get(x).getPostalCode() + " "
                    + customerList.get(x).getCountry() + "\n\n");
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
