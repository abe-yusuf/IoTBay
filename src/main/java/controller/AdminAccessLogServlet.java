package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.AccessLogDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AccessLog;
import model.User;

@WebServlet("/admin/users/logs")
public class AdminAccessLogServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is staff
        Integer staffId = (Integer) request.getSession().getAttribute("userId");
        Boolean isStaff = (Boolean) request.getSession().getAttribute("isStaff");
        
        if (staffId == null || !isStaff) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            UserDAO userDAO = new UserDAO();
            AccessLogDAO accessLogDAO = new AccessLogDAO();
            
            // Get user details
            User targetUser = userDAO.getUser(userId);
            if (targetUser == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Get current user to check if they are admin
            User currentUser = userDAO.getUser(staffId);
            boolean isAdmin = "admin@iotbay.com".equals(currentUser.getEmail());
            
            // Prevent staff from viewing other staff logs, but allow them to view their own
            if (targetUser.isStaff() && !isAdmin && targetUser.getUserId() != staffId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff members cannot view other staff members' logs");
                return;
            }
            
            // Get date range parameters
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            
            // Get access logs
            List<AccessLog> logs = accessLogDAO.searchAccessLogs(userId, fromDate, toDate);
            
            // Set attributes for the JSP
            request.setAttribute("user", targetUser);
            request.setAttribute("accessLogs", logs);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            
            // Forward to the view
            request.getRequestDispatcher("/WEB-INF/views/admin/user-logs.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
} 