# IoTBay E-Commerce Application

An e-commerce web application for IoT devices built with Java EE and Apache Derby.

## Project Structure

```
iotbay/
├── src/
│   ├── main/
│   │   ├── java/                    # Java source files
│   │   │   ├── controller/          # Servlet controllers
│   │   │   ├── dao/                 # Data Access Objects
│   │   │   ├── model/              # Domain models
│   │   │   └── util/               # Utility classes
│   │   ├── resources/              # Application resources
│   │   │   ├── sql/               # SQL scripts
│   │   │   └── images/            # Image resources
│   │   └── webapp/                # Web application files
│   │       ├── resources/         # Static resources
│   │       │   ├── css/          # CSS stylesheets
│   │       │   └── js/           # JavaScript files
│   │       ├── static/           # Static assets
│   │       │   └── images/       # Image files
│   │       └── WEB-INF/          # Protected web resources
│   │           ├── views/        # JSP view files
│   │           │   ├── shared/   # Shared templates
│   │           │   ├── auth/     # Authentication views
│   │           │   ├── cart/     # Shopping cart views
│   │           │   └── orders/   # Order management views
│   │           └── web.xml       # Web application config
│   └── test/                     # Test files
└── pom.xml                       # Maven configuration

```

## Database Configuration

The application uses Apache Derby as its database. The database is automatically initialized on startup with the following location:
- Development: `~/iotbay/db`

## Building and Running

1. Prerequisites:
   - Java 17 or higher
   - Maven 3.6 or higher
   - Apache Tomcat 10 or higher

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Deploy the WAR file:
   - Copy `target/iotbay.war` to Tomcat's webapps directory
   - Or deploy through your IDE's server integration

4. Access the application:
   - URL: `http://localhost:8080/iotbay`

## Development Guidelines

1. **Database Access**:
   - Use DAOs for database operations
   - All SQL queries should be in prepared statements
   - Database initialization happens in `DatabaseInitListener`

2. **Web Resources**:
   - JSP files go in `WEB-INF/views/`
   - Static resources (CSS, JS, images) go in `webapp/resources/`
   - Shared templates are in `WEB-INF/views/shared/`

3. **Controllers**:
   - One servlet per major feature
   - Use appropriate HTTP methods (GET, POST, etc.)
   - Validate all user input

4. **Security**:
   - All user-specific pages require authentication
   - Passwords are hashed before storage
   - Use prepared statements to prevent SQL injection 