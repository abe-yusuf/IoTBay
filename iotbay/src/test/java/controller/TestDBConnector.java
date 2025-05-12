package controller;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple test class for testing the DBConnector
 */
public class TestDBConnector {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try {
            // Attempt to get a database connection
            Connection conn = DBConnector.getConnection();
            
            // Check if connection is successful
            if (conn != null) {
                System.out.println("Database connection successful!");
                
                // Display connection information
                System.out.println("Connected to: " + conn.getMetaData().getURL());
                System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("Driver: " + conn.getMetaData().getDriverName());
                
                // Test if connection is valid
                if (conn.isValid(5)) {
                    System.out.println("Connection is valid and active.");
                } else {
                    System.out.println("Connection is not valid!");
                }
                
                // Close the connection
                conn.close();
                System.out.println("Connection closed successfully.");
            } else {
                System.out.println("Failed to establish a connection - connection is null.");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found! Error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error! Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}