package util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context initialized - Starting database initialization");
        try {
            // Initialize the database
            DatabaseUtil.initializeDatabase();
            
            // Reset the access_logs table
            DatabaseUtil.resetAccessLogsTable();
        } catch (Exception e) {
            System.err.println("Error during database initialization in context listener:");
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed
        System.out.println("Context destroyed - Performing cleanup");
    }
} 