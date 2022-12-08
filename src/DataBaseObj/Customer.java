/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

/**
 *
 * @author conor
 */
public class Customer extends DBInteractionCustomer{
    
    // Varibles for customer data 
    
    private String customerName;
    private int active;
    private String address;
    private String address2;
    private int postalCode;
    private String phone;
    private String city;
    private String country;
    
    
    // Varibles for Primary and foreign keys
    private int customerId;
    private int addressId;
    private int cityId;
    private int countryId;
    
    
    //Constructors

    public Customer(String customerName, int active, String address, String address2, int postalCode, String phone, String city, String country) {
        this.customerName = customerName;
        this.active = active;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
        this.country = country;
    }

    public Customer() {
    }
    
    public Customer(Customer customer){
        customerName = customer.getCustomerName();
        active = customer.getActive();
        address = customer.getAddress();
        address2 = customer.getAddress2();
        postalCode = customer.getPostalCode();
        phone = customer.getPhone();
        city = customer.getCity();
        country = customer.getCountry();
        customerId = customer.getCustomerId();
        addressId = customer.getAddressId();
        cityId = customer.getCityId();
        countryId = customer.getCountryId();
    }
    
    
    
    
    //Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    
}
