package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.AccessLogDAO;
import dao.OrderDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AccessLog;
import model.Order;
import model.OrderStatus;
import model.User;
import util.DatabaseUtil;

@WebServlet("/account/*")
public class AccountServlet extends HttpServlet {
    private UserDAO userDAO;
    private AccessLogDAO accessLogDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        accessLogDAO = new AccessLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        try {
            // Get user profile
            User user = userDAO.getUser(userId);
            request.setAttribute("user", user);
            
            // Get search parameters
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            
            // Get access logs with search
            List<AccessLog> accessLogs;
            if (fromDate != null || toDate != null) {
                accessLogs = accessLogDAO.searchAccessLogs(userId, fromDate, toDate);
            } else {
                accessLogs = accessLogDAO.getUserAccessLogs(userId);
            }
            
            request.setAttribute("accessLogs", accessLogs);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            
            request.getRequestDispatcher("/WEB-INF/views/account/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving user profile", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "update":
                    updateProfile(request, userId);
                    request.setAttribute("message", "Profile updated successfully!");
                    break;
                case "change-password":
                    changePassword(request, userId);
                    request.setAttribute("message", "Password changed successfully!");
                    break;
                case "deactivate":
                    deactivateAccount(request, response, userId);
                    return; // Return early as we're redirecting
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }
            
            // Refresh user data and access logs
            User user = userDAO.getUser(userId);
            List<AccessLog> accessLogs = accessLogDAO.getUserAccessLogs(userId);
            
            request.setAttribute("user", user);
            request.setAttribute("accessLogs", accessLogs);
            
            request.getRequestDispatcher("/WEB-INF/views/account/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error updating profile", e);
        }
    }

    private void updateProfile(HttpServletRequest request, int userId) throws SQLException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        User user = userDAO.getUser(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);
        
        userDAO.updateUser(user);
    }

    private void changePassword(HttpServletRequest request, int userId) throws SQLException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        
        User user = userDAO.getUser(userId);
        if (userDAO.validatePassword(user.getEmail(), currentPassword)) {
            user.setPassword(newPassword);
            userDAO.updateUser(user);
        } else {
            throw new SQLException("Current password is incorrect");
        }
    }

    private void deactivateAccount(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            
            // Create DAOs with transaction connection
            UserDAO txUserDAO = new UserDAO(conn);
            OrderDAO txOrderDAO = new OrderDAO(conn);
            
            // Get user
            User user = txUserDAO.getUser(userId);
            if (user == null) {
                throw new SQLException("User not found");
            }
            
            // Deactivate account
            user.setActive(false);
            txUserDAO.updateUser(user);
            
            // Cancel all user's orders
            List<Order> userOrders = txOrderDAO.getOrdersByUserId(userId);
            for (Order order : userOrders) {
                if (!order.getStatus().equals("CANCELLED")) {
                    txOrderDAO.updateOrderStatus(order.getOrderId(), OrderStatus.CANCELLED);
                }
            }
            
            conn.commit();
            
            // Invalidate the session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            // Redirect to login page with message
            response.sendRedirect(request.getContextPath() + "/auth/login?message=Your account has been deactivated. Contact support to reactivate.");
            
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
} 