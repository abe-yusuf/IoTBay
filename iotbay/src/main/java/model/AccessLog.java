package model;

import java.io.Serializable;
import java.util.Date;

public class AccessLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int logID;
    private int userID;
    private String action;
    private Date timestamp;
    private String ipAddress;
    
    // Default constructor
    public AccessLog() {
        this.timestamp = new Date();
    }
    
    // Constructor without ID (for creating new logs)
    public AccessLog(int userID, String action, String ipAddress) {
        this.userID = userID;
        this.action = action;
        this.timestamp = new Date();
        this.ipAddress = ipAddress;
    }
    
    // Constructor with all fields
    public AccessLog(int logID, int userID, String action, Date timestamp, String ipAddress) {
        this.logID = logID;
        this.userID = userID;
        this.action = action;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }
    
    // Getters and Setters
    public int getLogID() {
        return logID;
    }
    
    public void setLogID(int logID) {
        this.logID = logID;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    @Override
    public String toString() {
        return "AccessLog{" +
                "logID=" + logID +
                ", userID=" + userID +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}