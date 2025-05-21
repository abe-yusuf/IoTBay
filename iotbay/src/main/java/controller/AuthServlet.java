package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.ValidationUtil;

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO;
    private AccessLogServlet accessLogServlet;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        accessLogServlet = new AccessLogServlet();
        accessLogServlet.init(); // Ensure AccessLogServlet is properly initialized
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        switch (pathInfo) {
            case "/login":
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if ("/register".equals(action)) {
            handleRegistration(request, response);
        } else if ("/login".equals(action)) {
            handleLogin(request, response);
        } else if ("/logout".equals(action)) {
            handleLogout(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (userDAO.validateLogin(email, password)) {
                User user = userDAO.getUserByEmail(email);
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("isStaff", user.isStaff());
                
                try {
                    // Log access
                    accessLogServlet.logAccess(request, user.getUserId());
                } catch (ServletException e) {
                    // Log the error but don't prevent login
                    System.err.println("Warning: Failed to log access: " + e.getMessage());
                }

                // Check if there's a redirect URL in the session
                String redirectUrl = (String) session.getAttribute("redirectUrl");
                if (redirectUrl != null) {
                    session.removeAttribute("redirectUrl");
                    
                    // Check if there was a cart action pending
                    String cartAction = (String) session.getAttribute("cartAction");
                    if (cartAction != null) {
                        // Reconstruct the cart action
                        String productId = (String) session.getAttribute("productId");
                        String productName = (String) session.getAttribute("productName");
                        String quantity = (String) session.getAttribute("quantity");
                        String price = (String) session.getAttribute("price");
                        
                        // Clear the session attributes
                        session.removeAttribute("cartAction");
                        session.removeAttribute("productId");
                        session.removeAttribute("productName");
                        session.removeAttribute("quantity");
                        session.removeAttribute("price");
                        
                        // Redirect to cart with the pending action
                        response.sendRedirect(redirectUrl + "?action=" + cartAction +
                                           "&productId=" + productId +
                                           "&productName=" + productName +
                                           "&quantity=" + quantity +
                                           "&price=" + price);
                        return;
                    }
                    
                    response.sendRedirect(redirectUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products");
                }
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error logging in", e);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        // Server-side validation
        List<String> errors = new ArrayList<>();
        
        if (!ValidationUtil.isValidEmail(email)) {
            errors.add("Invalid email address");
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, and numbers");
        }
        
        if (!ValidationUtil.isValidName(firstName)) {
            errors.add("First name must be between 2 and 50 characters");
        }
        
        if (!ValidationUtil.isValidName(lastName)) {
            errors.add("Last name must be between 2 and 50 characters");
        }
        
        if (phone != null && !phone.isEmpty() && !ValidationUtil.isValidPhone(phone)) {
            errors.add("Invalid phone number format");
        }
        
        if (address != null && !address.isEmpty() && !ValidationUtil.isValidAddress(address)) {
            errors.add("Address must be between 5 and 200 characters");
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("error", String.join(", ", errors));
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Sanitize inputs
            email = ValidationUtil.sanitizeHtml(email);
            firstName = ValidationUtil.sanitizeHtml(firstName);
            lastName = ValidationUtil.sanitizeHtml(lastName);
            phone = ValidationUtil.sanitizeHtml(phone);
            address = ValidationUtil.sanitizeHtml(address);
            
            // Check if user already exists
            User existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                request.setAttribute("error", "Email already registered");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                return;
            }
            
            // Create new user
            User user = new User();
            user.setEmail(email);
            user.setPassword(password); // In production, hash the password
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setStaff(false);
            user.setActive(true);
            
            userDAO.createUser(user);
            
            // Log the successful registration
            System.out.println("New user registered: " + email);
            
            // Redirect to login page with success message
            response.sendRedirect(request.getContextPath() + "/auth/login?registered=true");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Log logout time
            accessLogServlet.logLogout(request);
        } catch (ServletException e) {
            // Log the error but don't prevent logout
            System.err.println("Warning: Failed to log logout: " + e.getMessage());
        }
        
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Log logout time
            accessLogServlet.logLogout(request);
            
            // Invalidate session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/auth/login?logout=true");
        } catch (ServletException e) {
            // Log the error but don't prevent logout
            System.err.println("Warning: Failed to log logout: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/auth/login?logout=true");
        }
    }
} 