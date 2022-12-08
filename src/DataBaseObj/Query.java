/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseObj;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author conor
 */
public class Query {
    
    private static String query;
    private static Statement statement;
    private static ResultSet result;
    
    public static void makeQuery(String q){
        // Queries database with string statement
        query = q;
        try {
            // Create Statement object
            statement = DBConnection.conn.createStatement();
            // Determine Query execution
            if (query.toLowerCase().startsWith("select")){
                result = statement.executeQuery(q);
            }
            if (query.toLowerCase().startsWith("delete") ||
                    query.toLowerCase().startsWith("insert") ||
                    query.toLowerCase().startsWith("update"))
                statement.executeUpdate(q);     
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }
    
    public static ResultSet getResult(){
        return result;
    }
    
}
