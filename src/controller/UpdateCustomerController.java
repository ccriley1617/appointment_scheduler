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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class UpdateCustomerController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Customer> tempCustomerList;
    
    private Customer tempCustomer;
    
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
    private RadioButton activeNoRBTn;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<Customer> updateTableView;

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
    void onActionDisplayCustomerManager(ActionEvent event) throws IOException {
        //Conferms and returns User to Customer Manager Screne
        Optional<ButtonType> result = displayConfirmationYesNoMessage("This will"
                + " clear all values and return to CustomerManager.\n Continue?");
        if (result.isPresent() && result.get()== ButtonType.OK)
            sceneLoader("/view/CustomerManager.fxml", event);
    }

    @FXML
    void onActionSearchCustomers(ActionEvent event) throws SQLException {
        //Seaches Database based on customer name and updates table
        String searchString = searchTxt.getText();
        if (!searchString.isEmpty()){
            tempCustomerList = DBInteractionCustomer.getCustomerFromNameSearch(searchString);
            updateTableView.setItems(tempCustomerList);
        }
    }

    @FXML
    void onActionSelectCustomerFromTable(ActionEvent event) {
        // Gets selected customer and displays in the corrisponding field for editing
        try{
            tempCustomer = updateTableView.getSelectionModel().getSelectedItem();
            customerNameTxt.setText(String.valueOf(tempCustomer.getCustomerName()));
            addressTxt.setText(String.valueOf(tempCustomer.getAddress()));
            address2Txt.setText(String.valueOf(tempCustomer.getAddress2()));
            cityTxt.setText(String.valueOf(tempCustomer.getCity()));
            countryTxt.setText(String.valueOf(tempCustomer.getCountry()));
            postalCodeTxt.setText(String.valueOf(tempCustomer.getPostalCode()));            
            phoneTxt.setText(String.valueOf(tempCustomer.getPhone()));
            if(tempCustomer.getActive() == 1)
                activeYesRBtn.setSelected(true);
            else
                activeNoRBTn.setSelected(true);
        }catch(Exception e){
            // Error message if no customer was selected
            displayWarningOkMessage("Please select a customer to update.");
        }
    }

    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws SQLException {
        // Updates customer data in database
        // In case they try to update without a pre-selected customer
        if(tempCustomer.getCustomerId() > -10000)
        {
            try{
                // Gets values from text fields
                // Checks for appropriate values in each field
                long phoneInt = Long.parseLong(phoneTxt.getText()); 
                Customer changedCustomer = new Customer();
                //Puts database forgien keys from old customer object into new object
                changedCustomer.setCustomerId(tempCustomer.getCustomerId());
                changedCustomer.setAddressId(tempCustomer.getAddressId());
                changedCustomer.setCityId(tempCustomer.getCityId());
                changedCustomer.setCountryId(tempCustomer.getCountryId());
                //Gets changed data if any
                changedCustomer.setCustomerName(customerNameTxt.getText());
                changedCustomer.setAddress(addressTxt.getText());
                changedCustomer.setAddress2(address2Txt.getText()); 
                changedCustomer.setPostalCode(Integer.parseInt(postalCodeTxt.getText()));
                changedCustomer.setPhone(String.valueOf(phoneInt));
                changedCustomer.setCity(cityTxt.getText());
                changedCustomer.setCountry(countryTxt.getText());
                changedCustomer.setActive(0);
                if(activeYesRBtn.isSelected())
                    changedCustomer.setActive(1);
                // Updates customer in database
                tempCustomer = DBInteractionCustomer.updateCustomerInDataBase(tempCustomer, changedCustomer);
                // Clears table and customer varibles and update for new search
                tempCustomerList.clear();
                updateTableView.setItems(tempCustomerList);
                tempCustomer = new Customer();
                // In case they try to update without a pre-selected customer
                tempCustomer.setCustomerId(-10005);
                // Clears text fields for next update to be selected
                customerNameTxt.clear();
                addressTxt.clear();
                address2Txt.clear();
                cityTxt.clear();
                countryTxt.clear();
                postalCodeTxt.clear();
                phoneTxt.clear();
            }catch (Exception e){
                //Phone number could be parsed to an int displays error messege for re-entry
                displayWarningOkMessage("Please enter phone number and postal code as a continues integer.\n"
                + "Ex. 123456789");
            }
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tempCustomer = new Customer();
        //In case they try to update without a pre-selected customer
        tempCustomer.setCustomerId(-10005);
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
