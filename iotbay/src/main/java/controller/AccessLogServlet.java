package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dao.AccessLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AccessLog;

@WebServlet("/access-logs/*")
public class AccessLogServlet extends HttpServlet {
    private AccessLogDAO accessLogDAO;

    public AccessLogServlet() {
        this.accessLogDAO = new AccessLogDAO();
    }

    @Override
    public void init() throws ServletException {
        if (accessLogDAO == null) {
            accessLogDAO = new AccessLogDAO();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        
        try {
            List<AccessLog> logs;
            if (isStaff != null && isStaff) {
                logs = accessLogDAO.getAllAccessLogs();
            } else {
                logs = accessLogDAO.getUserAccessLogs(userId);
            }
            request.setAttribute("accessLogs", logs);
            request.getRequestDispatcher("/WEB-INF/views/access-logs.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving access logs", e);
        }
    }

    public void logAccess(HttpServletRequest request, Integer userId) throws ServletException {
        try {
            // Ensure DAO is initialized
            if (accessLogDAO == null) {
                accessLogDAO = new AccessLogDAO();
            }
            
            AccessLog log = new AccessLog(
                0, // ID will be generated
                userId,
                new Timestamp(System.currentTimeMillis()),
                null, // logout time will be set when user logs out
                request.getRemoteAddr()
            );
            accessLogDAO.addAccessLog(log);
            request.getSession().setAttribute("currentAccessLogId", log.getLogId());
        } catch (SQLException e) {
            // Convert to ServletException to better handle the error
            throw new ServletException("Error logging access: " + e.getMessage(), e);
        }
    }

    public void logLogout(HttpServletRequest request) throws ServletException {
        try {
            // Ensure DAO is initialized
            if (accessLogDAO == null) {
                accessLogDAO = new AccessLogDAO();
            }
            
            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer logId = (Integer) session.getAttribute("currentAccessLogId");
                if (logId != null) {
                    accessLogDAO.updateLogoutTime(logId, new Timestamp(System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error updating logout time: " + e.getMessage(), e);
        }
    }
} 