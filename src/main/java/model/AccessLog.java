package model;

import java.sql.Timestamp;

public class AccessLog {
    private int logId;
    private int userId;
    private Timestamp loginTime;
    private Timestamp logoutTime;
    private String ipAddress;

    public AccessLog(int logId, int userId, Timestamp loginTime, Timestamp logoutTime, String ipAddress) {
        this.logId = logId;
        this.userId = userId;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.ipAddress = ipAddress;
    }

    // Default constructor
    public AccessLog() {
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Timestamp getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Timestamp logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
} 