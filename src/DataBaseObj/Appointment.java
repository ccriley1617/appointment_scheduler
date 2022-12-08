/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 *
 * @author conor
 */
public class Appointment extends DBInteractionAppointment{
    
    private int appointmentId;
    private int customerId;
    private int userId;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;

    public Appointment(int appointmentId, int customerId, String title, 
            String description, String location, String contact, String type, 
            String url, ZonedDateTime start, ZonedDateTime end, int userId) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.userId = userId;
    }

    public Appointment() {
    }
    
    public Appointment(Appointment appointment) {
        // Appointment copier
        appointmentId = appointment.getAppointmentId();
        customerId = appointment.getCustomerId();
        userId = appointment.getUserId();
        title = appointment.getTitle();
        description = appointment.getDescription();
        location = appointment.getLocation();
        contact = appointment.getContact();
        type = appointment.getType();
        url = appointment.getUrl();
        start = appointment.getStartZoned();
        end = appointment.getEndZoned();
        customerName = appointment.getCustomerName();
        
    }
    
    public Appointment(ResultSet result) throws SQLException, ParseException{
        // Used when retrieving appointments from the database
        appointmentId = result.getInt("appointmentId");
        customerId = result.getInt("customerId");
        userId = result.getInt("userId");
        title = result.getString("title");
        description = result.getString("description");
        location = result.getString("location");
        contact = result.getString("contact");
        type = result.getString("type");
        url = result.getString("url");
        start = gmtTimeCovertedToLocal(result.getString("start"));//Converts GMT to local
        end = gmtTimeCovertedToLocal(result.getString("end"));//Converts GMT to local
        customerName = result.getString("customerName");
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        // returns time as a string
        String start = convertZonedToString(this.start);
        return start;
    }

    public void setStart(String start) {
        // Converts inputstring in to ZonedDateTime OBJECT of local timezone
        try{
            LocalDate date = LocalDate.of(Integer.parseInt(start.substring(0, 4)), 
            Integer.parseInt(start.substring(5,7)), 
            Integer.parseInt(start.substring(8,10)));
            LocalTime time = LocalTime.of(Integer.parseInt(start.substring(11,13)), 
            Integer.parseInt(start.substring(14,16)));
            ZonedDateTime gmtDateTime = ZonedDateTime.of(date,time,
                ZoneId.of(TimeZone.getDefault().getID()));
            this.start = gmtDateTime;
        }catch (Exception e){
            System.out.println("Error:" + e.getMessage());
            this.start= ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        }
    }

    public String getEnd() {
        //Retuns date and time converted to a string
        String end = convertZonedToString(this.end);
        return end;
    }

    public void setEnd(String end) {
        // Converts inputstring in to ZonedDateTime OBJECT of local timezone
        try{
            LocalDate date = LocalDate.of(Integer.parseInt(end.substring(0, 4)), 
            Integer.parseInt(end.substring(5,7)), 
            Integer.parseInt(end.substring(8,10)));
            LocalTime time = LocalTime.of(Integer.parseInt(end.substring(11,13)), 
            Integer.parseInt(end.substring(14,16)));
            ZonedDateTime gmtDateTime = ZonedDateTime.of(date,time,
                ZoneId.of(TimeZone.getDefault().getID()));
            this.end = gmtDateTime;
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            this.end= ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ZonedDateTime getStartZoned() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEndZoned() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    
    
    
}
