package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.OrderDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderStatus;
import model.User;
import util.DatabaseUtil;
import util.ValidationUtil;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        
        // Check if user is logged in and is staff
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        if (!Boolean.TRUE.equals(isStaff)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Default admin dashboard
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else if (pathInfo.equals("/users")) {
                // User management
                String searchTerm = request.getParameter("search");
                List<User> users;
                
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    users = userDAO.searchUsers(searchTerm.trim());
                } else {
                    users = userDAO.getAllUsers();
                }
                
                // Add current user to request for access control in JSP
                User currentUser = userDAO.getUser(userId);
                request.setAttribute("currentUser", currentUser);
                
                request.setAttribute("users", users);
                request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
            } else if (pathInfo.equals("/products")) {
                // Product management - redirect to products page
                response.sendRedirect(request.getContextPath() + "/products");
            } else if (pathInfo.equals("/orders")) {
                // Order management
                List<Order> orders = orderDAO.getAllOrders();
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error in admin operations", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        
        // Check if user is logged in and is staff
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        if (!Boolean.TRUE.equals(isStaff)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        
        try {
            if (pathInfo.equals("/users")) {
                switch (action) {
                    case "update":
                        updateUser(request, response);
                        break;
                    case "edit":
                        editUser(request, response);
                        break;
                    case "delete":
                        deleteUser(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else if (pathInfo.equals("/orders")) {
                switch (action) {
                    case "update-status":
                        updateOrderStatus(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error in admin operations", e);
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            
            // Create DAOs with transaction connection
            UserDAO txUserDAO = new UserDAO(conn);
            OrderDAO txOrderDAO = new OrderDAO(conn);
            
            int targetUserId = Integer.parseInt(request.getParameter("userId"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
            
            User user = txUserDAO.getUser(targetUserId);
            if (user != null) {
                // Get current user's email to check if they are admin
                int currentUserId = (Integer) request.getSession().getAttribute("userId");
                User currentUser = txUserDAO.getUser(currentUserId);
                boolean isAdmin = "admin@iotbay.com".equals(currentUser.getEmail());
                
                // Prevent modification of admin account
                if ("admin@iotbay.com".equals(user.getEmail())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin account cannot be modified");
                    return;
                }
                
                // Prevent staff from modifying other staff accounts
                if (user.isStaff() && !isAdmin) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff members cannot modify other staff accounts");
                    return;
                }
                
                // If deactivating the account
                if (!isActive) {
                    // Cancel all user's orders
                    List<Order> userOrders = txOrderDAO.getOrdersByUserId(targetUserId);
                    for (Order order : userOrders) {
                        if (!order.getStatus().equals("CANCELLED")) {
                            txOrderDAO.updateOrderStatus(order.getOrderId(), OrderStatus.CANCELLED);
                        }
                    }
                }
                
                user.setActive(isActive);
                txUserDAO.updateUser(user);
            }
            
            conn.commit();
            response.sendRedirect(request.getContextPath() + "/admin/users");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int targetUserId = Integer.parseInt(request.getParameter("userId"));
        
        // Get user before deletion to check if it's admin or staff
        User user = userDAO.getUser(targetUserId);
        if (user != null) {
            // Get current user's email to check if they are admin
            int currentUserId = (Integer) request.getSession().getAttribute("userId");
            User currentUser = userDAO.getUser(currentUserId);
            boolean isAdmin = "admin@iotbay.com".equals(currentUser.getEmail());
            
            if ("admin@iotbay.com".equals(user.getEmail())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin account cannot be deleted");
                return;
            }
            
            // Prevent staff from deleting other staff accounts
            if (user.isStaff() && !isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff members cannot delete other staff accounts");
                return;
            }
            
            userDAO.deleteUser(targetUserId);
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
    
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String statusStr = request.getParameter("status");
        OrderStatus status = OrderStatus.valueOf(statusStr);
        
        orderDAO.updateOrderStatus(orderId, status);
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
    
    private void editUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int targetUserId = Integer.parseInt(request.getParameter("userId"));
        
        // Get user before editing to check if it's admin or staff
        User user = userDAO.getUser(targetUserId);
        if (user != null) {
            // Get current user's email to check if they are admin
            int currentUserId = (Integer) request.getSession().getAttribute("userId");
            User currentUser = userDAO.getUser(currentUserId);
            boolean isAdmin = "admin@iotbay.com".equals(currentUser.getEmail());
            
            if ("admin@iotbay.com".equals(user.getEmail())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin account cannot be modified");
                return;
            }
            
            // Prevent staff from editing other staff accounts
            if (user.isStaff() && !isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff members cannot modify other staff accounts");
                return;
            }
            
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setAddress(address);
            
            // Only admin can change passwords
            if (isAdmin && password != null && !password.trim().isEmpty()) {
                // Validate password
                if (!ValidationUtil.isValidPassword(password)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                        "Password must be at least 8 characters long and contain uppercase, lowercase, and numbers");
                    return;
                }
                user.setPassword(password);
            }
            
            userDAO.updateUser(user);
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
} 