package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.DatabaseUtil;

public class UserDAO {
    
    private Connection getConnection() throws SQLException {
        try {
            return DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            throw e;
        }
    }

    public User createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone, address, is_staff, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());
            stmt.setBoolean(7, user.isStaff());
            stmt.setBoolean(8, user.isActive());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
            return user;
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_staff"),
                    rs.getBoolean("active")
                );
            }
            return null;
        }
    }

    public User getUser(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_staff"),
                    rs.getBoolean("active")
                );
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_staff"),
                    rs.getBoolean("active")
                );
                users.add(user);
            }
            return users;
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET " +
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "phone = ?, " +
                    "address = ?, " +
                    "password = ?, " +
                    "active = ? " +
                    "WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPassword());
            stmt.setBoolean(6, user.isActive());
            stmt.setInt(7, user.getUserId());
            
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public boolean validateLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND active = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean validatePassword(String email, String password) throws SQLException {
        String sql = "SELECT password FROM users WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // In a real application, you should use password hashing
                return password.equals(rs.getString("password"));
            }
        }
        return false;
    }

    public User findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_staff"),
                    rs.getBoolean("active")
                );
            }
            return null;
        }
    }

    public List<User> searchUsers(String searchTerm) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE " +
                    "CHAR(user_id) LIKE ? OR " +
                    "LOWER(first_name) LIKE ? OR " +
                    "LOWER(last_name) LIKE ? OR " +
                    "LOWER(email) LIKE ? " +
                    "ORDER BY user_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_staff"),
                    rs.getBoolean("active")
                );
                users.add(user);
            }
            return users;
        }
    }
} 