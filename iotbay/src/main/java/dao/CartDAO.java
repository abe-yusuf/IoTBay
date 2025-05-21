package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cart;
import model.CartItem;
import util.DatabaseUtil;

public class CartDAO {
    public Cart getCart(int userId) throws SQLException {
        Cart cart = new Cart();
        List<CartItem> items = new ArrayList<>();
        
        String sql = "SELECT ci.cart_item_id, ci.product_id, p.name, ci.quantity, ci.price " +
                    "FROM cart_items ci " +
                    "JOIN products p ON ci.product_id = p.product_id " +
                    "WHERE ci.user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CartItem item = new CartItem(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                items.add(item);
            }
            
            cart.setItems(items);
            return cart;
        } catch (SQLException e) {
            // If the table doesn't exist yet, return an empty cart
            if (e.getSQLState().equals("42X05")) {
                return cart;
            }
            throw e;
        }
    }
    
    public void addToCart(int userId, CartItem item) throws SQLException {
        // First check if the item already exists in the cart
        String checkSql = "SELECT cart_item_id FROM cart_items WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart_items (user_id, product_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, item.getProductId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Item exists, update quantity
                updateStmt.setInt(1, item.getQuantity());
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, item.getProductId());
                updateStmt.executeUpdate();
            } else {
                // Item doesn't exist, insert new
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, item.getProductId());
                insertStmt.setString(3, item.getProductName());
                insertStmt.setInt(4, item.getQuantity());
                insertStmt.setDouble(5, item.getPrice());
                insertStmt.executeUpdate();
            }
        }
    }
    
    public void updateCartItemQuantity(int userId, int productId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        }
    }
    
    public void removeFromCart(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
    
    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
} 