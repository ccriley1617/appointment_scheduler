/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DataBaseObj.Appointment;
import DataBaseObj.DBConnection;
import DataBaseObj.DBInteractionAppointment;
import DataBaseObj.LogManData;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.dateTime;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 *
 * @author Conor Riley
 */
public class LogInFormController  extends JavaFXGeneralController implements Initializable{
    
    @FXML
    private Label AppBookTxtBlock;

    @FXML
    private Label usernameTxtBlock;

    @FXML
    private Label passwordTxtBlock;
    
    @FXML
    private Button logInButton;

    @FXML
    private Button exitBtn;
    
    @FXML
    private TextField usernameTxt;

    @FXML
    private PasswordField passwordTxt;


    @FXML
    void onActionExitProgram(ActionEvent event) {
        DBConnection.closeConnection();
        System.exit(0);
    }

    

    @FXML
    void onActionLogInDisplayMainMenu(ActionEvent event) throws IOException, SQLException, ParseException {
        // Logs in user if username are in database
        ResourceBundle rb2; // For forgien language pack
        String userName = usernameTxt.getText();
        String password = passwordTxt.getText();
        //Output for userlog file name varible
        String fileName = "src/c195bcrjavafx/userlog.txt";
        // Opens the userlog file 
        FileWriter fwriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fwriter);
        // Outputs current attempt to userLog
        outputFile.print("UserName Entered: " + userName + " Password Entered: "
                + password + "TimeStamp: " + LogManData.convertZonedToString(ZonedDateTime.now())
                + " Log in Succeeded: ");
        // Gets the password for the corrisonding username returns an empty result 
        // if username is not found
        ResultSet result = LogManData.getPasswordFromUserName(userName);
        if (result.next()){
            String userPassword = result.getString("password");
            //Checks password 
            if(password.equals(userPassword)){
                // Sets username and userId to be used in updates
                LogManData.setUserNameLoggedIn(userName);
                LogManData.setUserIdLoggedIn(result.getInt("userId"));
                // Sets username attempt to true in log and closes
                outputFile.print("True\n");
                outputFile.close();
                //Checks for appointment within the next fifteen minutes
                Appointment conflicting = DBInteractionAppointment.checkForAppointmentInTheNextFifteenMinutes(ZonedDateTime.now());
                // Displays meeting that starts in the next fifteen minutes
                if (!(conflicting.getAppointmentId() == -10010)){
                    displayWarningOkMessage("There is a Meeting that starts in "
                            + "the next 15 Minutes\n" + "with " 
                            + conflicting.getCustomerName() + "\n Starts: "
                            + conflicting.getStart() + " Ends: "
                            + conflicting.getEnd()+"\n Title: "
                            + conflicting.getTitle());
                }
                //Password was correct moves on to main menu
                sceneLoader("/view/MainMenu.fxml", event);
            }else {
                try{
                    // Error Message for incorrect password in forgien langauge
                    rb2  = ResourceBundle.getBundle("controller/Nat",Locale.getDefault());
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                        rb2.getString("Password") + " " + rb2.getString("is") +
                                " " + rb2.getString("incorrect") + ".");
                    Optional<ButtonType> resultBtn = alert.showAndWait();
                    LogManData.setUserNameLoggedIn(""); 
                }catch(Exception e){
                    // Error Message for incorrect password in English
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Password is incorrect.");
                    Optional<ButtonType> resultBtn = alert.showAndWait();
                    LogManData.setUserNameLoggedIn("");
                }
                // Sets username attempt to false in log and closes file
                outputFile.print("False\n");
                outputFile.close(); 
            }
        }else{
            try{
                // Error Message for incorrect password in forgien langauge
                rb2 = ResourceBundle.getBundle("controller/Nat",Locale.getDefault());
                Alert alert = new Alert(Alert.AlertType.ERROR,
                    rb2.getString("Username") + " " + rb2.getString("is") +
                           " " + rb2.getString("incorrect") + ".");
                Optional<ButtonType> resultBtn = alert.showAndWait();
                LogManData.setUserNameLoggedIn("");   
            }catch(Exception e){
                //User name not in Database
                Alert alert = new Alert(Alert.AlertType.ERROR,"Username is incorrect.");
                Optional<ButtonType> resultBtn = alert.showAndWait();
            }
            // Sets username attempt to false in log and closes file
            outputFile.print("False\n");
            outputFile.close();
            
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        ResourceBundle rb2;
        // Login screen can be converted to spanish if the users computer is set to spanish
        // Else leaves in default built in language which is english
        try{
            if (Locale.getDefault().getLanguage().equals("es")){
                rb2 = ResourceBundle.getBundle("controller/Nat",Locale.getDefault());
                AppBookTxtBlock.setText(rb2.getString("Appointment")
                    + " " + rb2.getString("Book"));
                usernameTxtBlock.setText(rb2.getString("Username") + ":");
                passwordTxtBlock.setText(rb2.getString("Password") + ":");
                logInButton.setText(rb2.getString("Login"));
                exitBtn.setText(rb2.getString("Exit"));
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }      
    }    
}
