package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:derby:/Users/malious/Documents/Study/IoTBay-1/iotbay/iotbayDB;create=true";
    private static final String USER = "";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}