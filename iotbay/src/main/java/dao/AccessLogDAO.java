package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.AccessLog;
import util.DatabaseUtil;

public class AccessLogDAO {
    public void logAccess(int userId, String action) throws SQLException {
        String sql = "INSERT INTO access_logs (user_id, action, timestamp) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // ... existing code ...
        }
    }
    
    public List<AccessLog> getUserAccessLogs(int userId) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM access_logs WHERE user_id = ? ORDER BY login_time DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                AccessLog log = new AccessLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("login_time"),
                    rs.getTimestamp("logout_time"),
                    rs.getString("ip_address")
                );
                logs.add(log);
            }
        }
        return logs;
    }

    public void addAccessLog(AccessLog log) throws SQLException {
        String sql = "INSERT INTO access_logs (user_id, login_time, logout_time, ip_address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, log.getUserId());
            stmt.setTimestamp(2, log.getLoginTime());
            stmt.setTimestamp(3, log.getLogoutTime());
            stmt.setString(4, log.getIpAddress());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                log.setLogId(rs.getInt(1));
            }
        }
    }

    public void updateLogoutTime(int logId, Timestamp logoutTime) throws SQLException {
        String sql = "UPDATE access_logs SET logout_time = ? WHERE log_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, logoutTime);
            stmt.setInt(2, logId);
            stmt.executeUpdate();
        }
    }

    public List<AccessLog> getAllAccessLogs() throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM access_logs ORDER BY login_time DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                AccessLog log = new AccessLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("login_time"),
                    rs.getTimestamp("logout_time"),
                    rs.getString("ip_address")
                );
                logs.add(log);
            }
        }
        return logs;
    }

    public List<AccessLog> searchAccessLogs(int userId, String fromDate, String toDate) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM access_logs WHERE user_id = ? "
        );
        
        List<Object> params = new ArrayList<>();
        params.add(userId);
        
        // Add date range filter if provided
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append("AND login_time >= ? ");
            params.add(java.sql.Date.valueOf(fromDate.trim()));
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append("AND login_time < ? + 1 DAY ");
            params.add(java.sql.Date.valueOf(toDate.trim()));
        }
        
        sql.append("ORDER BY login_time DESC");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                AccessLog log = new AccessLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("login_time"),
                    rs.getTimestamp("logout_time"),
                    rs.getString("ip_address")
                );
                logs.add(log);
            }
            return logs;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
} 