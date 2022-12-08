/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crjavafx;

import DataBaseObj.DBConnection;
import DataBaseObj.LogManData;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author conor
 */
public class CRJavaFX extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CalanderView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ParseException, FileNotFoundException {
        
        DBConnection.startConnection();  
        
        launch(args);
        
        DBConnection.closeConnection();
        
    }
    
}
