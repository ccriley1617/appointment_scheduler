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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author conor
 */
public class DeleteCustomerController extends JavaFXGeneralController implements Initializable {

    private ObservableList<Customer> tempCustomerList;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<Customer> deleteTableView;

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
    private TableColumn<Customer, String> activeCol;

    @FXML
    void onActionDeleteCustomer(ActionEvent event) {
        // Deletes selected customer from database
        // Conformation that selected customer will be deleted from database
        Optional<ButtonType> result = displayConfirmationYesNoMessage(
                "This will delete the selected Customer Name and Address from database\nContinue?");
        if (result.isPresent() && result.get()== ButtonType.OK){
            try{
                // Deletes selected customer
                Customer tempCustomer = deleteTableView.getSelectionModel().getSelectedItem();
                DBInteractionCustomer.deleteCustomerFromDataBase(tempCustomer);
                tempCustomerList.remove(tempCustomer);
                deleteTableView.setItems(tempCustomerList);
            }catch (Exception e){
                // Error message for non-selection
                displayWarningOkMessage("Please select a customer to delete.");
            }
        }
    }

    @FXML
    void onActionDisplayCustomerManager(ActionEvent event) throws IOException {
        sceneLoader("/view/CustomerManager.fxml", event);
    }

    @FXML
    void onActionSearchCustomers(ActionEvent event) throws SQLException {
        // Uses user input to search database by name        
        String searchString = searchTxt.getText();
        if (!searchString.isEmpty()){
            tempCustomerList = DBInteractionCustomer.getCustomerFromNameSearch(searchString);
            deleteTableView.setItems(tempCustomerList);  
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
