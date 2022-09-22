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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class AppointmentManagerController extends JavaFXGeneralController implements Initializable {

    @FXML
    void onActionDisplayAddAppointment(ActionEvent event) throws IOException {
        sceneLoader("/view/AddAppointment.fxml", event);
    }

    @FXML
    void onActionDisplayDeleteAppointment(ActionEvent event) throws IOException {
        sceneLoader("/view/DeleteAppointment.fxml", event);
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        sceneLoader("/view/MainMenu.fxml", event);
    }

    @FXML
    void onActionDisplayUpdateAppointment(ActionEvent event) throws IOException {
        sceneLoader("/view/UpdateAppointment.fxml", event);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
}
