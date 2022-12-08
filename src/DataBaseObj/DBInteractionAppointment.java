/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author conor
 */
public class DBInteractionAppointment extends LogManData{
     
    private static String statement;
    private static ResultSet result;
    
    public static void deleteAppointmentFromDataBase(Appointment appointmentToDelete){
        // Deletes based on AppointmentId
        statement = "DELETE FROM appointment WHERE "
                + "appointmentId = " + appointmentToDelete.getAppointmentId() + ";";
        Query.makeQuery(statement);
    }
    
    public static Appointment updateAppointmentInDataBase(Appointment appointmentData, Appointment changedAppointment) throws SQLException, ParseException{
        // Updates appointment in database
        Appointment tempAppointment = new Appointment();
        //Returns Error code if appointment can't be added due to time conflict;
        try{
            if ((!appointmentData.getStart().equals(changedAppointment.getStart()))
                || (!appointmentData.getEnd().equals(changedAppointment.getEnd()))){
                // Check for conflicting appointment
                tempAppointment = checkForAppointmentTimeAvailibility(changedAppointment);
                if (tempAppointment.getAppointmentId() <= -9999)
                    //Returns error code if conflicthing appointment
                    return tempAppointment;
            }
        }catch (Exception e){
            //Returns error code parsing time failed user needs to enter correct format
            tempAppointment = new Appointment();
            tempAppointment.setAppointmentId(-10002);
            return tempAppointment;
        }
        // Checks if name of customer was changed     
        if (!appointmentData.getCustomerName().equals(changedAppointment.getCustomerName())){
            // Checks to see if updated customer name is in database
           statement = "SELECT customerId, customerName FROM "
                   + "customer WHERE customerName = '" + changedAppointment.getCustomerName() +"';";
            Query.makeQuery(statement);
            result = Query.getResult(); 
            // if updated customer name is not in database returns an error code
            // customer must already be in database
            if (!result.next()){
                tempAppointment.setAppointmentId(-10004);
                return tempAppointment;
            } else{
                // Gets new customer id and name and updates appointment table
                tempAppointment = new Appointment(changedAppointment);
                tempAppointment.setCustomerId(result.getInt("customerId"));
                tempAppointment.setCustomerName(result.getString("customerName"));
                statement = "UPDATE appointment SET "
                + "customerId = " + tempAppointment.getCustomerId() + ", "
                + "lastUpdate = '" + currentLocalTimeCovertedToGMT() + "', "
                + "lastUpdateBy = '" + getUserNameLoggedIn() + "' "
                + "WHERE appointmentId = " + appointmentData.getAppointmentId() + ";";
                Query.makeQuery(statement);
            }
        }
        // Checks if the appoint times have changed
        if ((!appointmentData.getStart().equals(changedAppointment.getStart()))
                || (!appointmentData.getEnd().equals(changedAppointment.getEnd()))){
            //Updates changed times in database
            statement = "UPDATE appointment SET "
                + "start = '" + localTimeCovertedToGMT(changedAppointment.getStartZoned()) + "', "
                + "end = '" + localTimeCovertedToGMT(changedAppointment.getEndZoned()) + "', "
                + "lastUpdate = '" + currentLocalTimeCovertedToGMT() + "', "
                + "lastUpdateBy = '" + getUserNameLoggedIn() + "' "
                + "WHERE appointmentId = " + appointmentData.getAppointmentId() + ";";
            Query.makeQuery(statement);
        }            
        // Checks if remaining appointment data was changed. Updated if needed
        if ((!appointmentData.getTitle().equals(changedAppointment.getTitle()))
                || (!appointmentData.getDescription().equals(changedAppointment.getDescription()))
                || (!appointmentData.getLocation().equals(changedAppointment.getLocation()))
                || (!appointmentData.getContact().equals(changedAppointment.getContact()))
                || (!appointmentData.getType().equals(changedAppointment.getType()))
                || (!appointmentData.getUrl().equals(changedAppointment.getUrl()))
                ){
            statement = "UPDATE appointment SET "
                    + "title = '" + changedAppointment.getTitle() + "', "
                    + "description = '" + changedAppointment.getDescription() + "', "
                    + "location = '" + changedAppointment.getLocation() + "', "
                    + "contact = '" + changedAppointment.getContact() + "', "
                    + "type = '" + changedAppointment.getType() + "', "
                    + "url = '" + changedAppointment.getUrl() + "', "
                    + "lastUpdate = '" + currentLocalTimeCovertedToGMT() + "', "
                    + "lastUpdateBy = '" + getUserNameLoggedIn() + "' "
                    + "WHERE appointmentId = " + appointmentData.getAppointmentId() + ";";
            Query.makeQuery(statement);
        }
        tempAppointment = new Appointment(changedAppointment);
        tempAppointment.setAppointmentId(appointmentData.getAppointmentId());
        tempAppointment.setCustomerName(appointmentData.getCustomerName());
        return tempAppointment; 
    }
    
    public static Appointment checkForAppointmentInTheNextFifteenMinutes(ZonedDateTime check) throws SQLException, ParseException{
        // Returns appointment if starts within the next fifteen minutes
        // Returns -10010 if no appointment was found
        Appointment conflicting = new Appointment();
        conflicting.setAppointmentId(-10010);
        ZonedDateTime tempStart;
        String tempDate = localTimeCovertedToGMT(check.minusDays(1));
        // Makes a new object to search for 48 hours
        String tempDatePlusTwentyFour = localTimeCovertedToGMT(check.plusDays(1));
        ZonedDateTime checkPlusSixteen = check.plusMinutes(16);
        //Gathers all appointments from the day of appointment trying to enter
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE start >= '" + tempDate + "' AND "
                + "end <= '" + tempDatePlusTwentyFour + "';"; 
        Query.makeQuery(statement);
        result = Query.getResult();
        
        // Searches all results for conflicting appointment times and returns 
        // The Appointment of the conflicting if found
        while (result.next()){
            // Coverts to ZonedDateTime for easier compairson
            tempStart = gmtTimeCovertedToLocal(result.getString("start"));
            // Returns conflicting if found 
            if ((tempStart.compareTo(check) > 0)
                    && (tempStart.compareTo(checkPlusSixteen)) < 0){
                conflicting = new Appointment(result);
                return conflicting;
            }
        }
        return conflicting;
    }
    

    public static Appointment checkForAppointmentTimeAvailibility(Appointment appointment) throws SQLException, ParseException{
        // returns conflicting appointment if there is one
        // returns copy of appointment if it is not
        // appointment can't be added due to start being after end returns
        //      -10001 as appointment.getAppointmentId()
        // returns -10002 if there is a error in the date fields in AppointmentId
        String tempDate;
        ZonedDateTime tempStart; 
        ZonedDateTime tempEnd;
        Appointment tempAppointment;
        // Check for Buinsess hours 05:59-22:01
        // Returns Error code if outside business hours
        if ((appointment.getStartZoned().getHour() < 7)
                ||(appointment.getStartZoned().getHour() >20)
                ||(appointment.getEndZoned().getHour() < 7)
                ||(appointment.getEndZoned().getHour() > 20)
                ||!(appointment.getStartZoned().getYear() == appointment.getEndZoned().getYear())
                ||!(appointment.getStartZoned().getDayOfYear() == appointment.getEndZoned().getDayOfYear())){
            tempAppointment = new Appointment();
            tempAppointment.setAppointmentId(-9999);
            return tempAppointment;
        }
        // Check it start is equal to end
        //if (appointment.getStart().equals(appointment.getEnd()))
        // The start before the end check returns error code
        if (appointment.getStartZoned().compareTo(appointment.getEndZoned()) >= 0){
            tempAppointment = new Appointment();
            tempAppointment.setAppointmentId(-10001);
            return tempAppointment;
        }
        tempDate = localTimeCovertedToGMT(appointment.getStartZoned().minusDays(1));
        // Makes a new object to search for 48 hours
        String tempDatePlusTwentyFour = localTimeCovertedToGMT(appointment.getStartZoned().plusDays(1));
        //Gathers all appointments from the day of appointment trying to enter
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE start >= '" + tempDate + "' AND "
                + "end <= '" + tempDatePlusTwentyFour + "';"; 
        Query.makeQuery(statement);
        result = Query.getResult();
        // Searches all results for conflicting appointment times and returns 
        // The Appointment of the conflicting if found
        while (result.next()){
            // Coverts to ZonedDateTime for easier compairson
            tempDate = result.getString("start");
            tempStart = gmtTimeCovertedToLocal(tempDate);
            tempDate = result.getString("end");
            tempEnd = gmtTimeCovertedToLocal(tempDate);
            int appointmentId = result.getInt("appointmentId");
            //Meetings cannot start, end, or encapculate meeting
            //Needs to check if they start and end at the same time
            //    To ensure that one meeting can end at the start of another
            if  ((((appointment.getStartZoned().compareTo(tempStart) <0)
                    && (appointment.getEndZoned().compareTo(tempStart) > 0)) 
                        || ((appointment.getStartZoned().compareTo(tempEnd) < 0)
                        && (appointment.getEndZoned().compareTo(tempEnd) > 0) )
                        || ((appointment.getStartZoned().compareTo(tempStart) > 0)
                        && (appointment.getEndZoned().compareTo(tempEnd) < 0))
                        || (appointment.getStartZoned().compareTo(tempStart) == 0)
                        || appointment.getEndZoned().compareTo(tempEnd) == 0)
                        && !(appointment.getAppointmentId() == appointmentId)){
                    tempAppointment = new Appointment(result);
                    tempAppointment.setAppointmentId(-10003);
                    return tempAppointment;
                }
            }
                System.out.println(appointment.getStartZoned() +"LJKSDFLK:DJF");
        return tempAppointment = new Appointment(appointment);
    }
    
    public static Appointment addToAppointmentTableGetPrimaryKey(Appointment appointment) throws SQLException, ParseException{
        //Adds appointment to database
        Appointment tempAppointment;
        // Checks appointment times for availibility
        tempAppointment = checkForAppointmentTimeAvailibility(appointment);
        //Returns Error code if appointment can't be added to time conflict;
        if (tempAppointment.getAppointmentId() <= -9999)
            return tempAppointment;
        // Insert appointment into database
        statement = "INSERT INTO appointment (customerId, userId, title, "
                + "description, location, contact, type, url, start,"
                + "end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES("
                + appointment.getCustomerId() + ", " 
                + appointment.getUserId() + ", "
                + "'" + appointment.getTitle() + "', "
                + "'" + appointment.getDescription() + "', " 
                + "'" + appointment.getLocation() + "', "
                + "'" + appointment.getContact() + "', "
                + "'" + appointment.getType() + "', " 
                + "'" + appointment.getUrl() + "', "
                + "'" + localTimeCovertedToGMT(appointment.getStartZoned()) + "', "
                + "'" + localTimeCovertedToGMT(appointment.getEndZoned()) + "', "// Converts local to GMT
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "', " 
                + "'" + currentLocalTimeCovertedToGMT() + "', " 
                + "'" + getUserNameLoggedIn() + "');";
        Query.makeQuery(statement);
        //After insert fetchs primary key

        statement = "SELECT appointmentId, customerName FROM appointment INNER "
                + "JOIN customer ON appointment.customerId = customer.customerId "
                + "WHERE end = '" + localTimeCovertedToGMT(appointment.getEndZoned()) 
                + "' AND start = '" + localTimeCovertedToGMT(appointment.getStartZoned()) +"';";
        Query.makeQuery(statement);
        result = Query.getResult();
        result.next();
        // Adds primary key and customer name to object
        tempAppointment = new Appointment(appointment);
        tempAppointment.setAppointmentId(result.getInt("appointmentId"));
        tempAppointment.setCustomerName(result.getString("customerName"));
        return tempAppointment;
    }
    
     public static ObservableList<Appointment> getAppointmentFromTitleSearch(String searchName) throws SQLException, ParseException{
        // Appointment search from Title can do partial searches
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Delcare object to hold customer data
        Appointment appointment;
        //Search statement for database
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE title LIKE '" + searchName + "%';";
        Query.makeQuery(statement);
        result = Query.getResult();
        // Retrivies corisponding appointment informaion from table and puts it in object
        while (result.next()){
            appointment = new Appointment(result);
            // Adds complete customer to array
            appointmentList.add(appointment);
        }
        return appointmentList;
     }
     
     public static ObservableList<Appointment> getAppointmentFromNameSearch(String searchName) throws SQLException, ParseException{
        // Appointment search from Customer Name can do partial searches 
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Delcare object to hold appoint data
        Appointment appointment;
        //Search statement for database
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE customerName LIKE '" + searchName + "%';";
        Query.makeQuery(statement);
        result = Query.getResult();
        // Retrivies corisponding appointment informaion from table and puts it in object
        while (result.next()){
            appointment = new Appointment(result);
            // Adds complete customer to array
            appointmentList.add(appointment);
        }
        return appointmentList;
     }
     
     public static ObservableList<Appointment> getAllAppointments() throws SQLException, ParseException{
        // Retrieves all appointments from database
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Delcare object to hold appointment data
        Appointment appointment;
        // Statement to retrieve appointments
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId;";
        Query.makeQuery(statement);
        result = Query.getResult();
        // Retrivies corisponding appointment informaion from table and puts it in object
        while (result.next()){
            appointment = new Appointment(result);
            // Adds complete customer to array
            appointmentList.add(appointment);
        }
        return appointmentList;
     }
     
     public static ObservableList<Appointment> getWeekAppointments(String initialDate) throws SQLException, ParseException{
        // Returns all appointments for a desired week based on a initial start date
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Delcare object to hold customer data
        Appointment tempAppointment;
        // Varible for parseError
        ZonedDateTime parseErrorCheck = ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        // Checks to make sure date is in correct format otherwise returns a list with one appointment
        // start date of 0000-01-01 00:00:00:00 of UCT
       
        
        ZonedDateTime initialZonedDate = gmtTimeCovertedToLocal(initialDate + " 00:00");

        if (parseErrorCheck.equals(initialZonedDate)){
            tempAppointment = new Appointment();
            tempAppointment.setStart(parseErrorCheck);
            appointmentList.add(tempAppointment);
            return appointmentList;
        }
        // Statement to retrive appintments 
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE start >= '" + localTimeCovertedToGMT(initialZonedDate) + "' AND "
                + "end <= '" + localTimeCovertedToGMT(initialZonedDate.plusDays(8)) + "';"; 
        Query.makeQuery(statement);
        result = Query.getResult();
        // Retrivies corisponding appointment informaion from table and puts it in object
        while (result.next()){
            tempAppointment = new Appointment(result);
            // Adds complete customer to array
            appointmentList.add(tempAppointment);
        }    
        return appointmentList;
     }
     
     public static ObservableList<Appointment> getMonthAppointments(String initialDate) throws SQLException, ParseException{
        // Returns all appointments for a desired month based on a initial start date
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Delcare object to hold customer data
        Appointment tempAppointment;
        String statement;
        ResultSet results;
        // Varible for parseError
        ZonedDateTime parseErrorCheck = ZonedDateTime.of(0000,01,01,00,00,00,00,ZoneId.of("UCT"));
        // Checks to make sure date is in correct format otherwise returns a list with one appointment
        // start date of 0000-01-01 00:00:00:00 of UCT
        ZonedDateTime initialZonedDate = gmtTimeCovertedToLocal(initialDate + " 00:00");
        if (parseErrorCheck.equals(initialZonedDate)){
            tempAppointment = new Appointment();
            tempAppointment.setStart(parseErrorCheck);
            appointmentList.add(tempAppointment);
            return appointmentList;
        }
        // Statement to retrive appintments 
        statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE start >= '" + localTimeCovertedToGMT(initialZonedDate) + "' AND "
                + "end <= '" + localTimeCovertedToGMT(initialZonedDate.plusDays(32)) + "';";  
        Query.makeQuery(statement);
        results = Query.getResult();
        // Retrivies corisponding appointment informaion from table and puts it in object
        while (results.next()){  
            tempAppointment = new Appointment(results);
            // Adds complete customer to array
            appointmentList.add(tempAppointment);
        }   
        return appointmentList;
     }
     
      public static ObservableList<Appointment> sortAppointmentListByStart(ObservableList<Appointment> unsortedList){
         // Sorts the appointment list by start date and time
         // Uses a lambda expression for search case 
         // Using a Lambda allows us to skip making a sperate comparator in a method
         // The lambda experssion (Appointment a1,Appointment a2) -> a1.getStart().compareTo(a2.getStart()
         // is converted to an compartor object used for the sorting algorithm 
          ObservableList<Appointment> sortedList = unsortedList.sorted(
                  (Appointment a1,Appointment a2) -> a1.getStart().compareTo(a2.getStart()));
          return sortedList;
      }
      
      public static int[] getNumberOfAppointmentsByMonth() throws SQLException, ParseException{
          // Returns the count of appointments for each month in an array of integers
          ObservableList<Appointment> appointmentList = getAllAppointments();
          int[] monthCount = new int[12]; 
          for (int x =0; x < appointmentList.size(); x++){
              //Inceases corrisponding month count of appointment in list
              monthCount[appointmentList.get(x).getStartZoned().getMonthValue()-1]++;
          }
          return monthCount;
      }
      
      public static Stack<Integer> getUserIds() throws SQLException{
          // Returns a stack of userIds
          String statement = "SELECT userId FROM user;";
          Query.makeQuery(statement);
          ResultSet result = Query.getResult();
          Stack<Integer> userStack = new Stack();
          // Retrivies corisponding appointment informaion from table and puts it in object
          while(result.next()){
              userStack.push(result.getInt("userId"));
          }
          return userStack;
      }
      
      public static ObservableList<Appointment> getAppointmentsByUserId(int userId) throws SQLException, ParseException{
          // Retrieves all appointments for a designated user
          Appointment appointment;
          ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
          // Statement to retrieve appointments from database
          String statement = "SELECT * FROM appointment INNER JOIN customer ON "
                + "appointment.customerId = customer.customerId "
                + "WHERE userId = " + userId + ";";
          Query.makeQuery(statement);
          ResultSet result = Query.getResult();
          // Retrivies corisponding appointment informaion from table and puts it in object
          while (result.next()){
            appointment = new Appointment(result);
            // Adds complete customer to array
            appointmentList.add(appointment);
          }
          // Sorts list base on start date and time
          ObservableList<Appointment> sortedList = sortAppointmentListByStart(appointmentList);
          return sortedList;
      }
      
      public static String geUserNameByUserId(int userId) throws SQLException{
          // returns username base on a userId
          String statement = "SELECT userName FROM user WHERE"
                  + " userId = " + userId +";";
          Query.makeQuery(statement);
          ResultSet result = Query.getResult();
          result.next();
          return result.getString("userName");
      }
}
