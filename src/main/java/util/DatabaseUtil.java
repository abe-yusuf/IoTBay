package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:derby:/Users/malious/Documents/Study/IoTBay-1/iotbay/iotbayDB;create=true";
    private static final String USER = "";
    private static final String PASS = "";

    public static void initializeDatabase() {
        System.out.println("Starting database initialization...");
        System.out.println("Database URL: " + DB_URL);
        
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Derby embedded driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading Derby embedded driver: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Connected to database successfully");
            
            // Manually execute the CREATE TABLE statement
            String createTableSQL = "CREATE TABLE PRODUCTS (" +
                "PRODUCT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "NAME VARCHAR(255) NOT NULL, " +
                "IMAGE_URL VARCHAR(1024), " +
                "DESCRIPTION LONG VARCHAR, " +
                "PRICE DOUBLE NOT NULL, " +
                "QUANTITY INTEGER NOT NULL, " +
                "FAVOURITED BOOLEAN DEFAULT FALSE, " +
                "PRIMARY KEY (PRODUCT_ID))";
            
            try {
                System.out.println("Executing SQL: " + createTableSQL);
                stmt.execute(createTableSQL);
                System.out.println("Table created successfully");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    // Table already exists error
                    System.out.println("Table PRODUCTS already exists");
                } else {
                    System.err.println("Error creating table: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Check if the table already has data
            try {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCTS");
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Sample data already exists in PRODUCTS table. Skipping data insertion.");
                    return;
                }
            } catch (SQLException e) {
                System.err.println("Error checking for existing data: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Manually insert sample data
            String[] insertStatements = {
                "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Thermostat', 'https://example.com/images/thermostat.jpg', 'Control your home temperature remotely with this smart thermostat.', 129.99, 50, FALSE)",
                "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Security Camera', 'https://example.com/images/camera.jpg', 'HD security camera with motion detection and night vision.', 89.99, 30, FALSE)",
                "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Light Bulb', 'https://example.com/images/bulb.jpg', 'Color-changing smart light bulb that can be controlled via app.', 29.99, 100, FALSE)",
                "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Speaker', 'https://example.com/images/speaker.jpg', 'Voice-controlled smart speaker with virtual assistant.', 79.99, 45, FALSE)",
                "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Door Lock', 'https://example.com/images/lock.jpg', 'Smart door lock with fingerprint and PIN access.', 149.99, 20, FALSE)"
            };
            
            for (String insertSQL : insertStatements) {
                try {
                    System.out.println("Executing SQL: " + insertSQL);
                    stmt.execute(insertSQL);
                    System.out.println("Data inserted successfully");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) {
                        // Duplicate key error - can be ignored for inserts
                        System.out.println("Data already exists");
                    } else {
                        System.err.println("Error inserting data: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("DatabaseUtil: DB_URL = " + DB_URL);
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
} 