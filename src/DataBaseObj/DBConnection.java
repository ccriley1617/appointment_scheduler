/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Conor Riley
 */
public class DBConnection {
    
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String venderName = ":mysql:";
    private static final String ipAddress ="//3.227.166.251/U05Xes";
    // JDBC URL
    private static final String jdbcURL = protocol + venderName + ipAddress;
    // Driver interface reference
    private static final String MYSQLJDBCDriver =  "com.mysql.jdbc.Driver";
    public static Connection conn = null;
    // Sets username and password
    private static final String userName = "U05Xes"; 
    private static final String password = "53688632559"; 
    // Connects to Database
    public static Connection startConnection(){
        try{
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection succesful.");
        }
        catch(ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn; 
    }
    
    public static void closeConnection(){
        // Closes database connection
        try{
            conn.close();
            System.out.println("Connection closed.");
        }
        catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }  
}
