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
public class CustomerManagerController extends JavaFXGeneralController implements Initializable {

    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws IOException {
        sceneLoader("/view/DeleteCustomer.fxml", event);
    }

    @FXML
    void onActionDisplayAddCustomer(ActionEvent event) throws IOException {
        sceneLoader("/view/AddCustomer.fxml", event);
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        sceneLoader("/view/MainMenu.fxml", event);
    }

    @FXML
    void onActionDisplayUpdateCustomer(ActionEvent event) throws IOException {
        sceneLoader("/view/UpdateCustomer.fxml", event);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
