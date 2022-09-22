/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author conor
 */
public abstract class JavaFXGeneralController {
    
    private Stage stage;
    private Parent scene;
    
    // Scene Loader
    public void sceneLoader(String destination,ActionEvent event) throws IOException{
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(destination));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    public Optional<ButtonType> displayConfirmationYesNoMessage(String errorMessage){
        // Error alert with yes and no
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,errorMessage);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }
    
    public void displayWarningOkMessage(String errorMessage){
        // Warning message ok box
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error Dialog");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    
    public void displayConfirmationOkMessage(String errorMessage){
        // Confirmation Ok message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Parent getScene() {
        return scene;
    }

    public void setScene(Parent scene) {
        this.scene = scene;
    }
}
