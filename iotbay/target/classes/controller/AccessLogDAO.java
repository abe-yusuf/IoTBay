package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.AccessLog;

public class AccessLogDAO {
    private Connection conn;
    
    public AccessLogDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Create a new log entry
    public int createLogEntry(AccessLog log) throws SQLException {
        String query = "INSERT INTO access_logs (userID, action, timestamp, ipAddress) VALUES (?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, log.getUserID());
        stmt.setString(2, log.getAction());
        stmt.setTimestamp(3, new java.sql.Timestamp(log.getTimestamp().getTime()));
        stmt.setString(4, log.getIpAddress());
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new SQLException("Creating log entry failed, no rows affected.");
        }
        
        int logID;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                logID = generatedKeys.getInt(1);
                log.setLogID(logID);
            } else {
                throw new SQLException("Creating log entry failed, no ID obtained.");
            }
        }
        
        stmt.close();
        return logID;
    }
    
    // Get all log entries
    public List<AccessLog> getAllLogs() throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String query = "SELECT * FROM access_logs ORDER BY timestamp DESC";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AccessLog log = new AccessLog(
                rs.getInt("logID"),
                rs.getInt("userID"),
                rs.getString("action"),
                rs.getTimestamp("timestamp"),
                rs.getString("ipAddress")
            );
            logs.add(log);
        }
        
        rs.close();
        stmt.close();
        return logs;
    }
    
    // Get log entries for a specific user
    public List<AccessLog> getLogsByUserId(int userID) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String query = "SELECT * FROM access_logs WHERE userID = ? ORDER BY timestamp DESC";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userID);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AccessLog log = new AccessLog(
                rs.getInt("logID"),
                rs.getInt("userID"),
                rs.getString("action"),
                rs.getTimestamp("timestamp"),
                rs.getString("ipAddress")
            );
            logs.add(log);
        }
        
        rs.close();
        stmt.close();
        return logs;
    }
    
    // Get log entries within a date range
    public List<AccessLog> getLogsByDateRange(Date startDate, Date endDate) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String query = "SELECT * FROM access_logs WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
        stmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AccessLog log = new AccessLog(
                rs.getInt("logID"),
                rs.getInt("userID"),
                rs.getString("action"),
                rs.getTimestamp("timestamp"),
                rs.getString("ipAddress")
            );
            logs.add(log);
        }
        
        rs.close();
        stmt.close();
        return logs;
    }
    
    // Get log entries by action type
    public List<AccessLog> getLogsByAction(String action) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        String query = "SELECT * FROM access_logs WHERE action = ? ORDER BY timestamp DESC";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, action);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AccessLog log = new AccessLog(
                rs.getInt("logID"),
                rs.getInt("userID"),
                rs.getString("action"),
                rs.getTimestamp("timestamp"),
                rs.getString("ipAddress")
            );
            logs.add(log);
        }
        
        rs.close();
        stmt.close();
        return logs;
    }
    
    // Delete log entries older than a certain date
    public int deleteOldLogs(Date cutoffDate) throws SQLException {
        String query = "DELETE FROM access_logs WHERE timestamp < ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setTimestamp(1, new java.sql.Timestamp(cutoffDate.getTime()));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected;
    }
}