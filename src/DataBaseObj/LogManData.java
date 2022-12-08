/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 *
 * @author conor
 */
public abstract class LogManData {
    // Declare Log Managament Data varibles
    private static String userNameLoggedIn;
    private static int userIdLoggedIn;
    //Converts the local time to a GMT Sting. Is static method can be called independantly of class
    public static String currentLocalTimeCovertedToGMT (){
        // Converts local time to string for entry into database
        ZonedDateTime localDateTime = ZonedDateTime.now();//Gets local time
        Instant localToGMT = localDateTime.toInstant();//Coverts to GMT with instance
        ZoneId GMTZoneId = ZoneId.of("UCT");//Loads GMT Zone ID
        ZonedDateTime GMTTime= localToGMT.atZone(GMTZoneId);//Makes ZonedDateTime object to hold local time coverted to GMT
        String date = String.valueOf(GMTTime.toLocalDate());//Splits date and time into strings
        String time = String.valueOf(GMTTime.toLocalTime());
        String datePlusTime = date + " " + time;
        return datePlusTime;
    }
    //Coverts a given time in ZonedDateTime to GMT String for adding to
    //  database. 
    public static String localTimeCovertedToGMT(ZonedDateTime dateTime){
        // Converts local time to gmt from a ZonedDateTime object to a String
        // Used to move from inside the program to a string for the database
        String timeGMT = "";
        String date, time;
        //Creates instance to convert to GMT
        Instant localToGMT = dateTime.toInstant();
        ZoneId GMTZoneId = ZoneId.of("UCT");//Loads GMT Zone ID
        ZonedDateTime GMTTime= localToGMT.atZone(GMTZoneId);//Makes ZonedDateTime object to hold local time coverted to GMT
        date = String.valueOf(GMTTime.toLocalDate());//Splits date and time into strings
        time = String.valueOf(GMTTime.toLocalTime());
        timeGMT = date + " " + time;
        return timeGMT;
    }

    public static ZonedDateTime gmtTimeCovertedToLocal(String dateTime){
        //Coverts a GMT time string to Local time ZonedTimeData for adding to
        //  objects within program. 
        ZonedDateTime localTime;
        ZoneId GMTZoneId = ZoneId.of("UCT");//Loads GMT Zone ID
        //Converts string to usable date objects
        try{
            LocalDate dateGMT = LocalDate.of(Integer.parseInt(dateTime.substring(0, 4)), 
            Integer.parseInt(dateTime.substring(5,7)), 
            Integer.parseInt(dateTime.substring(8,10)));
            LocalTime timeGMT = LocalTime.of(Integer.parseInt(dateTime.substring(11,13)), 
            Integer.parseInt(dateTime.substring(14,16)));
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime gmtDateTime = ZonedDateTime.of(dateGMT,timeGMT,
                GMTZoneId);
            //Coverts to Local with instance
            Instant gmtToLocal = gmtDateTime.toInstant();
            //Makes ZonedDateTime object to hold local time coverted to GMT
            localTime= gmtToLocal.atZone(localZoneId);
            return localTime;
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            localTime =ZonedDateTime.of(0000,01,01,00,00,00,00,GMTZoneId);
            return localTime;
        }
    }
    
    public static ResultSet getPasswordFromUserName(String userName){
        //Checks database for userName and retrivies password
         // Objects to hold text field values to check agaist username and password
        ResultSet result;
        String statement;
        //Get password from DataBase for correct userName
        statement = "SELECT password, userId FROM user WHERE "
            + "userName = '" + userName +"';";
        Query.makeQuery(statement);
        result = Query.getResult();
        return result;
     }
    
    public static String convertZonedToString(ZonedDateTime date){
        // Converts a zoned date object to a string used for display or database entry
        int monthValue = date.getMonthValue();
        String monthValueString;
        // For proper formating 01,09,10 for month
        if(monthValue < 10)
            monthValueString = "0" + monthValue;
        else
            monthValueString= String.valueOf(monthValue);
        // For proper formating 01,09,10 for day
        int dayValue = date.getDayOfMonth();
        String dayValueString;
        if(dayValue < 10)
            dayValueString = "0" + dayValue;
        else
            dayValueString= String.valueOf(dayValue);
        // For proper formating 01,09,10 for hour
        int hourValue = date.getHour();
        String hourValueString;
        if(hourValue < 10)
            hourValueString = "0" + hourValue;
        else
            hourValueString= String.valueOf(hourValue);
        // For proper formating 01,09,10 for minute
        int minValue = date.getMinute();
        String minValueString;
        if(minValue < 10)
            minValueString = "0" + minValue;
        else
            minValueString= String.valueOf(minValue);
        // Return string with converted data
        String start = date.getYear() + "-" + monthValueString + 
                "-" + dayValueString + " " + hourValueString +
                ":" + minValueString;
        return start;
    }
    

    public static String getUserNameLoggedIn() {
        return userNameLoggedIn;
    }

    public static void setUserNameLoggedIn(String userNameLoggedIn) {
        LogManData.userNameLoggedIn = userNameLoggedIn;
    }

    public static int getUserIdLoggedIn() {
        return userIdLoggedIn;
    }

    public static void setUserIdLoggedIn(int userIdLoggedIn) {
        LogManData.userIdLoggedIn = userIdLoggedIn;
    }
    
    
}
