/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import DataBaseObj.Customer;
import DataBaseObj.DBInteractionCustomer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Conor Riley
 */
public class AddCustomerController extends JavaFXGeneralController implements Initializable {

    @FXML
    private TextField customerNameTxt;

    @FXML
    private TextField addressTxt;

    @FXML
    private TextField address2Txt;

    @FXML
    private TextField cityTxt;

    @FXML
    private TextField countryTxt;

    @FXML
    private TextField postalCodeTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private RadioButton activeYesRBtn;

    @FXML
    private RadioButton activeNoRBtn;

    @FXML
    void onActionDisplayCustomerManager(ActionEvent event) throws IOException {
        // Confirms and returns User to Customer Manager Screne
        Optional<ButtonType> result = displayConfirmationYesNoMessage("This will"
                + " clear all values and return to CustomerManager.\n Continue?");
        if (result.isPresent() && result.get()== ButtonType.OK)
             sceneLoader("/view/CustomerManager.fxml", event);
    }

    @FXML
    void onActionSaveCustomer(ActionEvent event) {
        // Adds Customer to database
        Customer customerData = new Customer();
        //Checks to Active radio buttons selected
        if ((!activeYesRBtn.isSelected())&&(!activeNoRBtn.isSelected())){
            displayWarningOkMessage("Please make a selection for Active.");
        }
        else{
            try{
                // Gets values from text fields
                // Checks for appropriate values in each field
                long phoneInt = Long.parseLong(phoneTxt.getText()); 
                customerData.setCustomerName(customerNameTxt.getText());
                customerData.setAddress(addressTxt.getText());
                customerData.setAddress2(address2Txt.getText()); 
                customerData.setPostalCode(Integer.parseInt(postalCodeTxt.getText()));
                customerData.setPhone(String.valueOf(phoneInt));
                customerData.setCity(cityTxt.getText());
                customerData.setCountry(countryTxt.getText());
                customerData.setActive(0);
                if(activeYesRBtn.isSelected())
                    customerData.setActive(1);
                if ((customerData.getCustomerName().isEmpty() || 
                    customerData.getCustomerName().length()>45)
                    || (customerData.getAddress().isEmpty() 
                    || customerData.getAddress().length()>50)
                    || (customerData.getAddress2().length()>50)
                    || (customerData.getCity().isEmpty() 
                    || customerData.getCity().length()>50)
                    || (customerData.getCountry().isEmpty() 
                    || customerData.getCountry().length()>50)
                    || (customerData.getPostalCode() < 1) 
                    || (customerData.getPostalCode() > 1000000000)
                    || (customerData.getPhone().isEmpty() 
                    || customerData.getPhone().length()>20)){
                    // Warning message for invalid entry
                    displayWarningOkMessage("Please Enter a valid value for each field.");
                }else{
                    //All information was correct and customer was added to database
                    DBInteractionCustomer.addCustomerToDataBase(customerData);              
                    displayConfirmationOkMessage("Customer has been saved to database.");
                    customerNameTxt.setText(null);
                    addressTxt.setText(null);
                    address2Txt.setText(null);
                    cityTxt.setText(null);
                    countryTxt.setText(null);
                    postalCodeTxt.setText(null);
                    phoneTxt.setText(null);
                    activeYesRBtn.setSelected(false);
                    activeNoRBtn.setSelected(false);
                }
            }catch (Exception e){
                // Coudn't parse int for phone number so requires re-entry
                displayWarningOkMessage("Please Enter a valid value for each field.\n"
                        + "Phone numbers and Postal code as a continues integer.\n"
                + "Ex. 123456789\n");
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
