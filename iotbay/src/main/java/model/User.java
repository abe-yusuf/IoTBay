package model;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;        // Added userID field
    private String fname;
    private String lname;
    private String email;
    private String password;
    
    // Constructors
    public User() {}
    
    public User(String fname, String lname, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }
    
    // Constructor with userID
    public User(int userID, String fname, String lname, String email, String password) {
        this.userID = userID;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }
     
    // Getters & Setters
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getFname() {
        return fname;
    }
    
    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getLname() {
        return lname;
    }
    
    public void setLname(String lname) {
        this.lname = lname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Helper method to get full name
    public String getFullName() {
        return fname + " " + lname;
    }
    

    public boolean isAdmin() {
        return false; 
    }
    
}