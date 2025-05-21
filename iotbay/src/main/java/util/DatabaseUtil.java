package util;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ServiceLoader;

public class DatabaseUtil {
    // Database will be created in user's home directory under .derby/iotbay
    private static final String DERBY_HOME = System.getProperty("user.home") + File.separator + ".derby";
    private static final String DB_NAME = "iotbay";
    private static final String DB_PATH = DERBY_HOME + File.separator + DB_NAME;
    private static final String DB_URL = "jdbc:derby:" + DB_PATH + ";create=true";
    private static final String USER = "";
    private static final String PASS = "";
    private static boolean driverLoaded = false;

    static {
        // Set Derby system properties
        Properties sysProps = System.getProperties();
        sysProps.put("derby.system.home", DERBY_HOME);
        System.setProperties(sysProps);
    }

    public static void initializeDatabase() {
        System.out.println("Starting database initialization...");
        System.out.println("Database URL: " + DB_URL);
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Database Directory: " + DB_PATH);
        System.out.println("Derby System Home: " + DERBY_HOME);
        
        // Ensure Derby home directory exists
        try {
            File derbyHome = new File(DERBY_HOME);
            if (!derbyHome.exists()) {
                System.out.println("Creating Derby home directory: " + derbyHome.getAbsolutePath());
                if (derbyHome.mkdirs()) {
                    System.out.println("Derby home directory created successfully");
                } else {
                    System.err.println("Failed to create Derby home directory");
                    return;
                }
            } else {
                System.out.println("Derby home directory already exists");
            }
        } catch (Exception e) {
            System.err.println("Error creating Derby home directory: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Load Derby driver
        try {
            loadDerbyDriver();
        } catch (SQLException e) {
            System.err.println("Failed to load Derby driver: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Initialize database schema
        try (Connection conn = getConnection()) {
            System.out.println("Connected to database successfully");
            
            // Check if PRODUCTS table exists
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "PRODUCTS", null);
            
            if (!tables.next()) {
                System.out.println("PRODUCTS table does not exist. Creating it...");
                createProductsTable(conn);
            } else {
                System.out.println("PRODUCTS table already exists");
            }
            
            // Verify table was created
            verifyProductsTable(conn);
            
            // Check if USERS table exists
            tables = metaData.getTables(null, null, "USERS", null);
            
            if (!tables.next()) {
                System.out.println("USERS table does not exist. Creating it...");
                createUsersTable(conn);
            } else {
                System.out.println("USERS table already exists");
            }
            
            // Verify users table was created
            verifyUsersTable(conn);
            
            // Check if CART_ITEMS table exists
            tables = metaData.getTables(null, null, "CART_ITEMS", null);
            
            if (!tables.next()) {
                System.out.println("CART_ITEMS table does not exist. Creating it...");
                createCartItemsTable(conn);
            } else {
                System.out.println("CART_ITEMS table already exists");
            }
            
            // Create ACCESS_LOGS table
            tables = metaData.getTables(null, null, "ACCESS_LOGS", null);
            
            if (!tables.next()) {
                System.out.println("ACCESS_LOGS table does not exist. Creating it...");
                createAccessLogsTable(conn);
            } else {
                System.out.println("ACCESS_LOGS table already exists");
            }
            
            // Check if ORDERS table exists
            tables = metaData.getTables(null, null, "ORDERS", null);
            
            if (!tables.next()) {
                System.out.println("ORDERS table does not exist. Creating it...");
                createOrdersTable(conn);
            } else {
                System.out.println("ORDERS table already exists");
            }
            
            // Check if ORDER_ITEMS table exists
            tables = metaData.getTables(null, null, "ORDER_ITEMS", null);
            
            if (!tables.next()) {
                System.out.println("ORDER_ITEMS table does not exist. Creating it...");
                createOrderItemsTable(conn);
            } else {
                System.out.println("ORDER_ITEMS table already exists");
            }
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    private static void loadDerbyDriver() throws SQLException {
        if (!driverLoaded) {
            try {
                // Try ServiceLoader first
                ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
                boolean found = false;
                for (Driver driver : loader) {
                    System.out.println("Found JDBC driver: " + driver.getClass().getName());
                    if (driver.getClass().getName().equals("org.apache.derby.jdbc.EmbeddedDriver")) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    // Try direct loading
                    Class<?> driverClass = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                    Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
                    DriverManager.registerDriver(driver);
                    found = true;
                }

                if (!found) {
                    throw new SQLException("Could not load Derby driver");
                }
                driverLoaded = true;
                System.out.println("Derby driver loaded successfully");
            } catch (Exception e) {
                throw new SQLException("Failed to load Derby driver", e);
            }
        }
    }

    private static void createProductsTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE PRODUCTS (" +
                "PRODUCT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "NAME VARCHAR(255) NOT NULL, " +
                "IMAGE_URL VARCHAR(1024), " +
                "DESCRIPTION LONG VARCHAR, " +
                "PRICE DOUBLE NOT NULL, " +
                "QUANTITY INTEGER NOT NULL, " +
                "FAVOURITED BOOLEAN DEFAULT FALSE, " +
                "PRIMARY KEY (PRODUCT_ID))";
            
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Executing table creation SQL: " + createTableSQL);
            stmt.executeUpdate(createTableSQL);
            System.out.println("PRODUCTS table created successfully");
            
            // Insert sample data
            insertSampleProducts(conn);
        }
    }

    private static void createUsersTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE USERS (" +
            "USER_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "EMAIL VARCHAR(255) NOT NULL UNIQUE, " +
            "PASSWORD VARCHAR(255) NOT NULL, " +
            "FIRST_NAME VARCHAR(100) NOT NULL, " +
            "LAST_NAME VARCHAR(100) NOT NULL, " +
            "PHONE VARCHAR(20), " +
            "ADDRESS VARCHAR(255), " +
            "IS_STAFF BOOLEAN DEFAULT FALSE, " +
            "ACTIVE BOOLEAN DEFAULT TRUE, " +
            "PRIMARY KEY (USER_ID))";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Executing users table creation SQL: " + createTableSQL);
            stmt.executeUpdate(createTableSQL);
            System.out.println("USERS table created successfully");
            
            // Insert default admin user
            insertDefaultAdmin(conn);
        }
    }

    private static void insertDefaultAdmin(Connection conn) throws SQLException {
        String insertSQL = 
            "INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, IS_STAFF, ACTIVE) " +
            "VALUES ('admin@iotbay.com', 'admin', 'Admin', 'User', '1300 IOT BAY', 'IoTBay HQ', TRUE, TRUE)";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Inserting default admin user...");
            stmt.executeUpdate(insertSQL);
            System.out.println("Default admin user created successfully");
        }
    }

    private static void insertSampleProducts(Connection conn) throws SQLException {
        String[] insertStatements = {
            "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) " +
            "VALUES ('Smart Thermostat', 'https://m.media-amazon.com/images/I/5118X+rWiOL.jpg', " +
            "'Control your home temperature remotely with this smart thermostat.', 129.99, 50, FALSE)",
            
            "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) " +
            "VALUES ('Security Camera', 'https://thespystore.com.au/cdn/shop/products/1_91e20459-f870-4d6f-b9a9-afe34a9497ba.jpg', " +
            "'HD security camera with motion detection and night vision.', 89.99, 30, FALSE)",
            
            "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) " +
            "VALUES ('Smart Light Bulb', 'https://cdn11.bigcommerce.com/s-apgyqyq0gk/images/stencil/500x500/products/3434/4856/laser-10w-smart-white-bulb-b22-2295__40336.1725334359.jpg?c=1', " +
            "'Color-changing smart light bulb that can be controlled via app.', 29.99, 100, FALSE)",
            
            "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) " +
            "VALUES ('Smart Speaker', 'https://i5.walmartimages.com/asr/92a6e18c-c8e9-471f-bc92-f40cfa38f40b.5c900739f23c354c54e29fb7eb89b0ac.jpeg', " +
            "'Voice-controlled smart speaker with virtual assistant.', 79.99, 45, FALSE)",
            
            "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) " +
            "VALUES ('Door Lock', 'https://www.jbhifi.com.au/cdn/shop/products/608109-Product-0-I-638004961640652731.jpg', " +
            "'Smart door lock with fingerprint and PIN access.', 149.99, 20, FALSE)"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : insertStatements) {
                System.out.println("Inserting sample product...");
                stmt.executeUpdate(sql);
            }
            System.out.println("Sample products inserted successfully");
        }
    }

    private static void verifyProductsTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Try to select from the table
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCTS");
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("PRODUCTS table verified. Contains " + count + " records.");
            }
        }
    }

    private static void verifyUsersTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Try to select from the table
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM USERS");
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("USERS table verified. Contains " + count + " records.");
            }
        }
    }

    private static void createCartItemsTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE CART_ITEMS (" +
            "CART_ITEM_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "USER_ID INTEGER NOT NULL, " +
            "PRODUCT_ID INTEGER NOT NULL, " +
            "PRODUCT_NAME VARCHAR(255) NOT NULL, " +
            "QUANTITY INTEGER NOT NULL, " +
            "PRICE DECIMAL(10,2) NOT NULL, " +
            "PRIMARY KEY (CART_ITEM_ID), " +
            "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID), " +
            "FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID))";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating CART_ITEMS table...");
            stmt.executeUpdate(createTableSQL);
            System.out.println("CART_ITEMS table created successfully");
        }
    }

    private static void createAccessLogsTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE access_logs (" +
            "log_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "user_id INTEGER NOT NULL, " +
            "login_time TIMESTAMP NOT NULL, " +
            "logout_time TIMESTAMP, " +
            "ip_address VARCHAR(45) NOT NULL, " +
            "PRIMARY KEY (log_id), " +
            "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
            ")";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating ACCESS_LOGS table...");
            stmt.executeUpdate(createTableSQL);
            System.out.println("ACCESS_LOGS table created successfully");
        }
    }

    private static void createOrdersTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE orders (" +
            "order_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "user_id INTEGER NOT NULL, " +
            "order_date TIMESTAMP NOT NULL, " +
            "total_amount DOUBLE NOT NULL, " +
            "status VARCHAR(20) NOT NULL, " +
            "PRIMARY KEY (order_id), " +
            "FOREIGN KEY (user_id) REFERENCES users(user_id))";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating ORDERS table...");
            stmt.executeUpdate(createTableSQL);
            System.out.println("ORDERS table created successfully");
        }
    }

    private static void createOrderItemsTable(Connection conn) throws SQLException {
        String createTableSQL = 
            "CREATE TABLE order_items (" +
            "order_item_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "order_id INTEGER NOT NULL, " +
            "product_id INTEGER NOT NULL, " +
            "quantity INTEGER NOT NULL, " +
            "price DOUBLE NOT NULL, " +
            "PRIMARY KEY (order_item_id), " +
            "FOREIGN KEY (order_id) REFERENCES orders(order_id), " +
            "FOREIGN KEY (product_id) REFERENCES products(product_id))";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating ORDER_ITEMS table...");
            stmt.executeUpdate(createTableSQL);
            System.out.println("ORDER_ITEMS table created successfully");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            loadDerbyDriver();
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void resetAccessLogsTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Drop the existing table if it exists
            try {
                stmt.execute("DROP TABLE access_logs");
                } catch (SQLException e) {
                // Ignore if table doesn't exist
                if (!e.getSQLState().equals("42Y55")) {
                    throw e;
                }
            }
            
            // Create access_logs table with correct structure
            stmt.execute("CREATE TABLE access_logs (" +
                "log_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "user_id INTEGER NOT NULL, " +
                "login_time TIMESTAMP NOT NULL, " +
                "logout_time TIMESTAMP, " +
                "ip_address VARCHAR(45) NOT NULL, " +
                "PRIMARY KEY (log_id), " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id))");
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 