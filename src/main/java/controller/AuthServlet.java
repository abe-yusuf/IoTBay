package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

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
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (pathInfo) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            default:
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

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            boolean isStaff = "true".equals(request.getParameter("isStaff"));
            
            // Check if email already exists
            if (userDAO.getUserByEmail(email) != null) {
                request.setAttribute("error", "Email already exists");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                return;
            }
            
            // If registering as staff, verify admin credentials
            if (isStaff) {
                String adminEmail = request.getParameter("adminEmail");
                String adminPassword = request.getParameter("adminPassword");
                
                if (adminEmail == null || adminPassword == null || 
                    !adminEmail.equals("admin@iotbay.com") || !adminPassword.equals("admin")) {
                    request.setAttribute("error", "Invalid admin credentials");
                    request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                    return;
                }
            }

            // Create new user
            User user = new User(0, email, password, firstName, lastName, phone, address, isStaff, true);
            user = userDAO.createUser(user);

            // Set success message
            request.setAttribute("message", "Registration successful! Please login.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error registering user", e);
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
} 