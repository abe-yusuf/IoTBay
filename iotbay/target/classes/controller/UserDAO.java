package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class UserDAO {
    private Connection conn;
    
    public UserDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Find user by email
    public User findUser(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        
        User user = null;
        if (rs.next()) {
            user = new User(
                rs.getInt("userID"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("email"),
                rs.getString("password")
            );
        }
        
        rs.close();
        stmt.close();
        return user;
    }
    
    // Get user by ID
    public User getUserById(int userID) throws SQLException {
        String query = "SELECT * FROM users WHERE userID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userID);
        ResultSet rs = stmt.executeQuery();
        
        User user = null;
        if (rs.next()) {
            user = new User(
                rs.getInt("userID"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("email"),
                rs.getString("password")
            );
        }
        
        rs.close();
        stmt.close();
        return user;
    }
    
    // Check if email exists (for registration validation)
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT userID FROM users WHERE email = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        
        boolean exists = rs.next();
        
        rs.close();
        stmt.close();
        return exists;
    }
    
    // Check if email exists for a different user (for update validation)
    public boolean emailExists(String email, int excludeUserID) throws SQLException {
        String query = "SELECT userID FROM users WHERE email = ? AND userID != ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setInt(2, excludeUserID);
        ResultSet rs = stmt.executeQuery();
        
        boolean exists = rs.next();
        
        rs.close();
        stmt.close();
        return exists;
    }
    
    // Add user (alias for createUser for convenience)
    public int addUser(User user) throws SQLException {
        return createUser(user);
    }
    
    // Add user and return success status
    public boolean addUserSuccess(User user) throws SQLException {
        try {
            int userID = createUser(user);
            return userID > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Create new user
    public int createUser(User user) throws SQLException {
        String query = "INSERT INTO users (fname, lname, email, password) VALUES (?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, user.getFname());
        stmt.setString(2, user.getLname());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPassword());
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }
        
        int userID;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userID = generatedKeys.getInt(1);
                user.setUserID(userID);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
        
        stmt.close();
        return userID;
    }
    
    // Update user
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET fname = ?, lname = ?, email = ?, password = ? WHERE userID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, user.getFname());
        stmt.setString(2, user.getLname());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPassword());
        stmt.setInt(5, user.getUserID());
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Authenticate user (login)
    public User authenticateUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        
        User user = null;
        if (rs.next()) {
            user = new User(
                rs.getInt("userID"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("email"),
                rs.getString("password")
            );
        }
        
        rs.close();
        stmt.close();
        return user;
    }
    
    // Delete user by ID
    public boolean deleteUser(int userID) throws SQLException {
        String query = "DELETE FROM users WHERE userID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userID);
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    // Get all users (for admin functionality)
    public java.util.List<User> getAllUsers() throws SQLException {
        java.util.List<User> users = new java.util.ArrayList<>();
        String query = "SELECT * FROM users ORDER BY userID";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            User user = new User(
                rs.getInt("userID"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("email"),
                rs.getString("password")
            );
            users.add(user);
        }
        
        rs.close();
        stmt.close();
        return users;
    }
}