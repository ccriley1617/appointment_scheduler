/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author conor
 */
public class DBInteractionCustomer extends LogManData{
    
    private static String statement;;
    
    public static void addCustomerToDataBase(Customer customerData) {
        //Adds customer in reverse order to preserve forgien keys
        try{
            customerData.setCountryId(getPrimaryKeyFromCountryTableOrAddCountryToTable(customerData));
            customerData.setCityId(getPrimaryKeyFromCityTableOrAddCityToTable(customerData));
            customerData.setAddressId(addToAddressTableGetPrimaryKey(customerData));
            customerData.setCustomerId(addToCustomerTableGetPrimaryKey(customerData));            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static void deleteCustomerFromDataBase(Customer customerData){
        //Deletes customer from database
        statement = "DELETE FROM customer WHERE "
                + "customerId =" + customerData.getCustomerId();
        Query.makeQuery(statement);  
    }
    
    public static Customer updateCustomerInDataBase(Customer customerData, Customer changedCustomer) throws SQLException{
        //Updates customer in database if data was changed
        Customer returnCustomer = new Customer(changedCustomer);
        //Checks if customer name was changed updates
        if ((!customerData.getCustomerName().equals(changedCustomer.getCustomerName()))
                || (customerData.getActive() != changedCustomer.getActive())){
            statement = "UPDATE customer SET "
                    + "customerName = '" + changedCustomer.getCustomerName() + "', "
                    + "active = " + changedCustomer.getActive() + ", "
                    + "lastUpdate = '" + currentLocalTimeCovertedToGMT() + "', "
                    + "lastUpdateBy = '" + getUserNameLoggedIn() + "' "
                    + "WHERE customerId = " + changedCustomer.getCustomerId() + ";";
            Query.makeQuery(statement);   
        }
        //Checks if address was updated was changed updates
        if ((!customerData.getAddress().equals(changedCustomer.getAddress()))
                || (!customerData.getAddress2().equals(changedCustomer.getAddress2()))
                || !(customerData.getPostalCode() == (changedCustomer.getPostalCode()))
                || (!customerData.getPhone().equals(changedCustomer.getPhone()))){
            statement = "UPDATE address SET "
                    + "address = '" + changedCustomer.getAddress() + "', "
                    + "address2 = '" + changedCustomer.getAddress2() + "', "
                    + "postalCode = '" + changedCustomer.getPostalCode() + "', "
                    + "phone = '" + changedCustomer.getPhone() + "', "
                    + "lastUpdate = '" + currentLocalTimeCovertedToGMT() + "', "
                    + "lastUpdateBy = '" + getUserNameLoggedIn() + "' "
                    + "WHERE addressId = " + changedCustomer.getAddressId() + ";";
            Query.makeQuery(statement);
        }
        // Checks if country was updated was changed updates
        if ((!customerData.getCountry().equals(changedCustomer.getCountry()))){
            // Checks country in apperiate tables gets forgein key if in table
            // Otherwise adds missing country and returns new forgien key
            returnCustomer.setCountryId(getPrimaryKeyFromCountryTableOrAddCountryToTable(returnCustomer));  
            returnCustomer.setCityId(getPrimaryKeyFromCityTableOrAddCityToTable(returnCustomer));
            // Updates customer country in table
            statement = "UPDATE address SET "
                    + "cityId = " + returnCustomer.getCityId() + " "
                    + "WHERE addressId = " + returnCustomer.getAddressId() +";";
            Query.makeQuery(statement);
        }else if ((!customerData.getCity().equals(changedCustomer.getCity()))){
            // Checks city in apperiate tables gets forgein key if in table
            // Otherwise adds missing city and returns new forgien key
            returnCustomer.setCityId(getPrimaryKeyFromCityTableOrAddCityToTable(returnCustomer));
            // Updates customer city in table
            statement = "UPDATE address SET "
                    + "cityId = " + returnCustomer.getCityId() + " "
                    + "WHERE addressId = " + returnCustomer.getAddressId() +";";
            Query.makeQuery(statement);
        }
        return returnCustomer;
        
    }
    
    private static int addToCustomerTableGetPrimaryKey(Customer customerData) throws SQLException{
        // Inserts customer into table and returns primary key
        ResultSet result;
        // Insert
        statement = "INSERT INTO customer(customerName, addressId, active, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
                + "'" + customerData.getCustomerName() + "', " 
                + "'" + customerData.getAddressId() + "', "
                + "'" + customerData.getActive() + "', "
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "', " 
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "');";
        Query.makeQuery(statement);
        // Primary key retrieval
        statement = "SELECT customerId FROM customer WHERE "
                + "customerName = '" + customerData.getCustomerName() +"';";
        Query.makeQuery(statement);
        result = Query.getResult();
        result.next();
        return result.getInt("customerId");  
    }
    
    private static int addToAddressTableGetPrimaryKey(Customer customerData) throws SQLException{
        // Inserts address into table and returns primary key
        ResultSet result;
        // Insert
        String statement = "INSERT INTO address(address, address2, cityId, "
                + "postalCode, phone,createDate,"
                + "createdBy, lastUpdate, lastUpdateBy) VALUES("
                + "'" + customerData.getAddress() + "', " 
                + "'" + customerData.getAddress2() +"', "
                + "'" + customerData.getCityId() + "', " 
                + "'" + customerData.getPostalCode() +"', "
                + "'" + customerData.getPhone() + "', "
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "', " 
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "');";
        Query.makeQuery(statement);
        // Primary key retrieval
        statement = "SELECT addressId FROM address WHERE " 
                + "address = '" +customerData.getAddress() 
                + "' AND address2 = '" + customerData.getAddress2() 
                + "' AND postalCode = '" + customerData.getPostalCode() + "';";
        Query.makeQuery(statement);
        result = Query.getResult();
        result.next();
        return result.getInt("addressId"); 
    }
    
    private static int getPrimaryKeyFromCityTableOrAddCityToTable(Customer customerData) throws SQLException{
        //SQL statement to check if city is in table
        String statement = "SELECT cityId FROM city WHERE " 
                + "city = '" + customerData.getCity() + "' AND "
                + "countryId = " + customerData.getCountryId() +";";
        Query.makeQuery(statement);
        ResultSet result = Query.getResult();
        // If city is not in database it is added
        if (!result.next()) {
            // Insert
            statement = "INSERT INTO city(city, countryId, createDate, "
                    + "createdBy, lastUpdate, lastUpdateBy) VALUES("
                    + "'" + customerData.getCity() + "', " 
                    + "'" + customerData.getCountryId() +"', "
                    + "'" + currentLocalTimeCovertedToGMT() + "', " 
                    + "'" + getUserNameLoggedIn() + "', " 
                    + "'" + currentLocalTimeCovertedToGMT() + "', " 
                    + "'" + getUserNameLoggedIn() + "');";
            Query.makeQuery(statement);
            // Primary key retrieval
            statement = "SELECT cityId FROM city WHERE " +
                    "city = '" + customerData.getCity() + "' AND "
                    + "countryId = '" + customerData.getCountryId() + "';";
            Query.makeQuery(statement);
            result = Query.getResult();
            result.next();
        }
        // Primary key added to object
        return result.getInt("cityId");
    }
    
     private static int getPrimaryKeyFromCountryTableOrAddCountryToTable(Customer customerData) throws SQLException{
          //SQL statement to check if city is in table
        statement = "SELECT countryId FROM country WHERE " + 
                "country = '" + customerData.getCountry() + "';";
        Query.makeQuery(statement);
        ResultSet result = Query.getResult();
        // If city is not in database it is added
        if (!result.next()) {
            statement = "INSERT INTO country(country, createDate, "
                    + "createdBy, lastUpdate, lastUpdateBy) VALUES("
                    + "'" + customerData.getCountry() + "', " 
                    + "'" + currentLocalTimeCovertedToGMT() + "', "
                    + "'" + getUserNameLoggedIn() + "', " 
                    + "'" + currentLocalTimeCovertedToGMT() + "', "
                    + "'" + getUserNameLoggedIn() + "');";
            Query.makeQuery(statement);
            //Retrivies new Primary Key
            statement = "SELECT countryId FROM country WHERE " + 
                "country = '" + customerData.getCountry() + "';";
            Query.makeQuery(statement); 
            result = Query.getResult(); 
            result.next();
        }        
        // Primary key added to object
        return result.getInt("countryId");
     }
     
     public static ObservableList<Customer> getCustomerFromNameSearch(String searchName) throws SQLException{
        // Searches database by customer name. Can do partial searches returns a list
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        // Delcare object to hold customer data
        Customer cusData;
        String statement;
        ResultSet results, searchNameResults;
        // Retrieves search results from database
        statement = "SELECT * FROM customer WHERE customerName LIKE '"
                + searchName + "%';";
        Query.makeQuery(statement);
        searchNameResults = Query.getResult();
        // Retrivies corisponding Customer name from table and puts it in object
        while (searchNameResults.next()){
            cusData = new Customer();
            cusData.setCustomerName(searchNameResults.getString("customerName"));
            cusData.setAddressId(searchNameResults.getInt("addressId"));
            cusData.setActive(searchNameResults.getInt("active"));
            cusData.setCustomerId(searchNameResults.getInt("customerId"));
            // Retrivies corisponding address from table and puts it in object
            statement = "SELECT * FROM address WHERE addressId ='" 
                    + cusData.getAddressId() + "';";
            Query.makeQuery(statement);
            results = Query.getResult();
            results.next();
            cusData.setAddress(results.getString("address"));
            cusData.setAddress2(results.getString("address2"));
            cusData.setCityId(results.getInt("cityId"));
            cusData.setPostalCode(results.getInt("postalCode"));
            cusData.setPhone(results.getString("phone"));
            // Retrivies corisponding city from table and puts it in object
            statement = "SELECT * FROM city WHERE cityId ='"
                    + cusData.getCityId() + "';";
            Query.makeQuery(statement);
            results = Query.getResult();
            results.next();
            cusData.setCity(results.getString("city"));
            cusData.setCountryId(results.getInt("countryID"));
            // Retrivies corisponding country from table and puts it in object
            statement = "SELECT * FROM country WHERE countryId ='"
                    + cusData.getCountryId() + "';";
            Query.makeQuery(statement);
            results = Query.getResult();
            results.next();
            cusData.setCountry(results.getString("country"));
            // Adds complete customer to array
            customerList.add(cusData);
        }
        return customerList;
     }
     
     
     public static ObservableList<Customer> getCustomerMailinglist() throws SQLException{
         // Returns list of Customers for mailing list
         ObservableList<Customer> customerList = FXCollections.observableArrayList();
         // Retrieves all customer and corrisponding address from database
         String statement = "SELECT customerName, address, address2, postalCode,"
                 + " city, country FROM customer INNER JOIN address ON "
                 + "customer.addressId = address.addressId INNER JOIN "
                 + "city ON address.cityId = city.cityId INNER JOIN "
                 + "country ON city.countryId = country.countryId "
                 + "WHERE active = 1";
         Query.makeQuery(statement);
         ResultSet   results = Query.getResult();
         // Adds all customers from the result data object to the list
         int count = 0;
         while (results.next()){
             customerList.add(new Customer());
             customerList.get(count).setCustomerName(results.getString("customerName"));
             customerList.get(count).setAddress(results.getString("address"));
             customerList.get(count).setAddress2(results.getString("address2"));
             customerList.get(count).setPostalCode(results.getInt("postalCode"));
             customerList.get(count).setCity(results.getString("city"));
             customerList.get(count).setCountry(results.getString("country"));
             count++;
         }
         // Sorts the customer list by customer by customer name
         // Uses a lambda expression for search case
         // Using a Lambda allows us to skip making a sperate comparator in a method
         // The lambda experssion (Customer c1,Customer c2) -> c1.getCustomerName().compareToIgnoreCase(c2.getCustomerName()
         // is converted to an compartor object used for the sorting algorithm 
         ObservableList<Customer> sortedList = customerList.sorted(
                  (Customer c1,Customer c2) -> c1.getCustomerName().compareToIgnoreCase(c2.getCustomerName()));
         return sortedList;
     }
     
}
