# IoTBay E-Commerce Platform

## Project Overview
IoTBay is a comprehensive e-commerce platform specialized in IoT (Internet of Things) devices and related products. The application is built using Java EE technologies and follows a Model-View-Controller (MVC) architecture pattern.

## Prerequisites
- Java Development Kit (JDK) 17 or later
- Apache Maven 3.6 or later
- Apache Tomcat 10.0 or later

## Quick Start

1. Clone the repository:
```bash
git clone [your-repository-url]
cd iotbay
```

2. Build the project:
```bash
mvn clean install
```

3. Deploy the WAR file:
- Copy `target/iotbay.war` to your Tomcat's webapps directory
- Or deploy through your IDE's Tomcat integration

4. Access the application:
- Open your browser and navigate to `http://localhost:8080/iotbay`
- Default admin credentials: 
  - Email: admin@iotbay.com
  - Password: admin

## Database Setup
The application uses Apache Derby as its database. The database will be automatically:
- Created in `~/.derby/iotbay` directory (user's home directory)
- Initialized with required tables
- Populated with sample data

No manual database setup is required as Derby is included in the project dependencies.

## Individual Contributions
### Team Member Responsibilities
- **User Access Management**: [Team Member Name]
  - Implementation of user authentication
  - User profile management
  - Access control and authorization
  
- **IoT Device Catalogue**: [Team Member Name]
  - Product catalog implementation
  - Device management interface
  - Search and filter functionality
  
- **Order Management**: [Team Member Name]
  - Shopping cart functionality
  - Order processing system
  - Order history tracking

## Technology Stack
- **Backend**: Java EE (Jakarta EE)
- **Database**: Apache Derby (embedded)
- **Web Server**: Apache Tomcat
- **Frontend**: JSP (JavaServer Pages), JSTL, HTML, CSS
- **Build Tool**: Maven

## Project Structure
```
iotbay/
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/    # Servlet controllers
│       │   ├── dao/          # Data Access Objects
│       │   ├── model/        # Domain models
│       │   └── util/         # Utility classes
│       ├── resources/
│       │   └── sql/          # SQL scripts
│       └── webapp/
│           ├── WEB-INF/
│           │   └── views/    # JSP views
│           └── resources/    # Static resources
└── pom.xml                   # Maven configuration
```

## Core Features

### User Management
- **User Registration**: New users can create accounts with personal details
- **Authentication**: Secure login system with session management
- **Profile Management**: Users can view and update their profile information
- **Account Deactivation**: Administrators can deactivate user accounts
- **User Search**: Advanced search functionality for user management

### Product Management
- **Product Catalog**: Browse and view available IoT devices
- **Product Details**: Detailed product information and specifications
- **Product Search**: Search products by various criteria
- **Inventory Management**: Staff can manage product inventory
- **Product Updates**: Staff can modify product details and pricing

### Shopping Features
- **Shopping Cart**: Add/remove products, update quantities
- **Checkout Process**: Secure order placement
- **Order History**: View past orders and their status
- **Order Management**: Staff can process and manage orders

### Administrative Features
- **User Administration**: Manage user accounts and permissions
- **Staff Management**: Assign and manage staff roles
- **Order Processing**: Handle order fulfillment and tracking
- **Inventory Control**: Monitor and update product stock levels

## Key Components

### Data Access Objects (DAO)
- **UserDAO**: Handles user-related database operations
  - User creation and authentication
  - Profile updates and management
  - User search functionality
  - Account status management

- **ProductDAO**: Manages product-related operations
  - Product listing and details
  - Inventory management
  - Product updates and deletion

- **OrderDAO**: Handles order processing
  - Order creation and management
  - Order history tracking
  - Order status updates

### Controllers (Servlets)
- **AdminServlet**: Handles administrative functions
- **AuthServlet**: Manages user authentication
- **ProductServlet**: Controls product-related operations
- **OrderServlet**: Processes order-related requests
- **CartServlet**: Manages shopping cart functionality

### Models
- **User**: Represents user data and attributes
- **Product**: Contains product information
- **Order**: Represents order details
- **CartItem**: Shopping cart item representation

### Utilities
- **DatabaseUtil**: Database connection management
- **AuthenticationUtil**: Security and authentication helpers
- **ValidationUtil**: Input validation utilities

## Security Features
- Password protection for user accounts
- Role-based access control (RBAC)
- Session management
- Input validation and sanitization
- Secure database operations

## Security & Validation Implementation

### Server-Side Validation (ValidationUtil.java)
- **Email Validation**: 
  - Strict format checking for valid email addresses
  - Domain validation
  - Local part validation
- **Password Requirements**:
  - Minimum 8 characters
  - Must contain uppercase and lowercase letters
  - Must include numbers
  - Special character validation
- **Name Validation**: 
  - Length checks (2-50 characters)
  - Character set validation
  - Prevent special characters
- **Phone Number**: 
  - Australian format validation
  - Supports formats: +61, 04XX, (02), etc.
  - Area code validation
  - Length verification
- **Address Validation**: 
  - Length validation (5-200 characters)
  - Format verification
  - Special character handling
- **Price Validation**: 
  - Decimal format with 2 decimal places
  - Range validation
  - Currency symbol handling
- **Quantity Validation**: 
  - Positive integer checks
  - Range validation
  - Stock level verification
- **HTML Sanitization**:
  - XSS prevention
  - Special character escaping
  - HTML tag filtering
  - Script injection prevention

### Client-Side Validation (validation.js)
- **Real-time Form Validation**:
  - Instant feedback as users type
  - Visual indicators for field status
  - Password strength meter
  - Cross-field validation
- **Error Feedback**:
  - Consolidated error summary at form top
  - Field-specific error messages
  - Clear validation status icons
  - Helpful error resolution hints
- **Input Sanitization**:
  - Client-side character filtering
  - Whitespace handling
  - Special character escaping
  - Maximum length enforcement

### Enhanced Registration Form (register.jsp)
- **User Interface**:
  - Responsive design for all devices
  - Clear field grouping
  - Visual hierarchy
  - Consistent styling
- **Validation Features**:
  - Real-time validation feedback
  - Clear error indicators
  - Success confirmation
  - Field completion progress
- **User Experience**:
  - Required field indicators (*)
  - Password strength indicator
  - Helpful tooltips
  - Clear error resolution steps
- **Form Elements**:
  - Properly labeled fields
  - Placeholder text
  - Help text for complex fields
  - Accessible form controls

### Security Implementation (AuthServlet.java)
- **Input Processing**:
  - Complete server-side validation
  - Input sanitization
  - XSS prevention
  - SQL injection protection
- **Error Handling**:
  - Detailed error logging
  - User-friendly error messages
  - Validation error collection
  - Secure error display
- **Session Management**:
  - Secure session handling
  - Session timeout
  - Session fixation prevention
  - Proper logout procedures
- **Authentication**:
  - Secure password handling
  - Failed login attempt tracking
  - Account lockout protection
  - Secure password reset process

### Validation Workflow
1. **Client-Side**:
   - Immediate user feedback
   - Prevent invalid submissions
   - Reduce server load
   - Enhance user experience

2. **Server-Side**:
   - Complete validation recheck
   - Additional security checks
   - Database constraint validation
   - Final data sanitization

3. **Error Handling**:
   - Clear error messages
   - Validation status tracking
   - Error logging and monitoring
   - Security event auditing

### Security Best Practices
- Input validation on both client and server
- Data sanitization before processing
- Secure session management
- Protection against common web vulnerabilities
- Regular security auditing
- Comprehensive error logging
- Secure authentication flow
- Access control enforcement

## Database Schema
The application uses Apache Derby with the following key tables:
- `users`: User account information
- `products`: Product catalog data
- `orders`: Order information
- `order_items`: Individual items in orders
- `cart_items`: Shopping cart contents

## Future Enhancements
1. Implementation of password hashing for improved security
2. Advanced product search filters
4. Order tracking system
5. Customer review and rating system
6. Product recommendations
7. Email notifications
8. Analytics dashboard for administrators

## Development Guidelines
- Follow MVC architecture pattern
- Implement proper error handling and logging
- Maintain separation of concerns
- Use prepared statements for database operations
- Follow Java coding conventions
- Document all major components and functions

## Testing
- Integration tests for database operations
- User interface testing
- Security testing
- Performance testing

## Deployment
1. Configure Apache Tomcat server
2. Set up Apache Derby database
3. Deploy WAR file to Tomcat
4. Configure environment variables
5. Verify database connectivity
6. Test all functionality in production environment

## Reporting Metrics
- User registration and activity
- Sales and revenue tracking
- Product performance
- Order processing times
- System performance metrics
- Error rates and types
- User engagement metrics

## MVC Implementation Details

### View Components
- **JSP Pages**:
  - User registration and login forms
  - Product catalog displays
  - Shopping cart interface
  - Order management screens
  - Admin control panels
- **Styling**: Responsive CSS with modern design principles
- **Client-side Validation**: JavaScript form validation

### Controller Components
- **Servlet Implementation**:
  - Request handling and routing
  - Session management
  - Form data processing
  - Business logic coordination
- **Authentication Flow**:
  - Login/logout processing
  - Session tracking
  - Access control enforcement

### Model Components
- **Data Objects**:
  - User entity management
  - Product catalog representation
  - Order processing logic
- **Database Interactions**:
  - CRUD operations
  - Transaction management
  - Data validation

## User Stories and CRUD Mappings

### User Access Management
- **Create**: New user registration
- **Read**: User profile viewing
- **Update**: Profile information modification
- **Delete**: Account deactivation

### IoT Device Catalogue
- **Create**: Add new products to catalog
- **Read**: View product details
- **Update**: Modify product information
- **Delete**: Remove products from catalog

### Order Management
- **Create**: Place new orders
- **Read**: View order history
- **Update**: Modify order status
- **Delete**: Cancel orders

## Non-Functional Requirements

### Security Implementation
- **Authentication**:
  - Secure password handling
  - Session management
  - Token-based authentication
- **Authorization**:
  - Role-based access control
  - Permission management
  - Resource protection
- **Data Protection**:
  - Input validation
  - SQL injection prevention
  - XSS protection

### Performance Metrics
- **Response Time**:
  - Page load < 2 seconds
  - Database queries < 1 second
- **Concurrent Users**:
  - Support for 100+ simultaneous users
  - Efficient connection pooling
- **Resource Usage**:
  - Optimized memory utilization
  - Efficient database queries
  - Caching implementation

## Testing Framework

### Unit Testing
- JUnit test cases for core functionality
- Mock objects for isolated testing
- Coverage reports

### Integration Testing
- End-to-end workflow testing
- Component interaction verification
- Database operation validation

### User Acceptance Testing
- Feature functionality verification
- User interface testing
- Cross-browser compatibility
- Mobile responsiveness

### Performance Testing
- Load testing results
- Stress testing outcomes
- Scalability assessment

## Defect Management
### Known Issues and Resolutions
- Issue tracking and categorization
- Resolution status
- Impact assessment
- Mitigation strategies

## Project Timeline
### Development Phases
1. Requirements Analysis
2. Design Phase
3. Implementation
4. Testing
5. Deployment
6. Maintenance

## Deployment Guide
1. Configure Apache Tomcat server
2. Set up Apache Derby database
3. Deploy WAR file to Tomcat
4. Configure environment variables
5. Verify database connectivity
6. Test all functionality in production environment

## Monitoring and Metrics
- User registration and activity tracking
- Sales and revenue analytics
- System performance monitoring
- Error rate tracking
- User engagement metrics

This documentation provides a comprehensive overview of the IoTBay e-commerce platform, covering all aspects required for detailed project reporting and analysis.

## Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
[Your License]

## Recent Updates

### Search Functionality UI Improvements (March 2024)
- **Access Logs Search**:
  - Redesigned search form with centered layout
  - Improved date range picker alignment
  - Added consistent styling with website's orange theme (#F96E46)
  - Enhanced visual feedback on button hover states

- **Orders Search**:
  - Implemented unified search interface with three criteria:
    - Order ID lookup
    - From Date filter
    - To Date filter
  - All search fields aligned horizontally for better space utilization
  - Centered search button placement below inputs
  - Added clear search functionality with proper state handling
  - Maintained consistent styling with the platform's design language

### UI/UX Enhancements
- **Form Styling**:
  - Standardized input field heights (40px)
  - Consistent padding and border radius across all inputs
  - Improved text alignment and spacing
  - Enhanced visual hierarchy with proper label placement

- **Button Improvements**:
  - Updated primary action buttons with orange theme color
  - Added hover effects with subtle elevation
  - Implemented consistent button sizing
  - Enhanced click feedback with transitions

- **Layout Optimization**:
  - Better use of whitespace in search forms
  - Improved responsive behavior
  - Consistent gap spacing between elements
  - Enhanced visual balance in form layouts
