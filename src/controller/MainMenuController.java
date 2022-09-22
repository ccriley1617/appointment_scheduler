/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class MainMenuController extends JavaFXGeneralController implements Initializable {


    @FXML
    void onActionDisplayAppointmentManager(ActionEvent event) throws IOException {
        sceneLoader("/view/AppointmentManager.fxml", event);
    }

    @FXML
    void onActionDisplayCustomerManaer(ActionEvent event) throws IOException {
        sceneLoader("/view/CustomerManager.fxml", event);
    }

    @FXML
    void onActionDisplayLogIn(ActionEvent event) throws IOException {
        sceneLoader("/view/LogInForm.fxml", event);
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
        // TODO
    }    
}
